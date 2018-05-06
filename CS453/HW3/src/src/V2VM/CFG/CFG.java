package V2VM.CFG;

import V2VM.Register;
import V2VM.RegisterAllocator;
import V2VM.Variable;
import cs132.vapor.ast.*;

import java.util.ArrayList;
import java.util.HashSet;

/**
 * One CFG for each function.
 */
public class CFG {
    public ArrayList<Variable> vars;
    public ArrayList<Variable.Interval> intervals = new ArrayList<>();
    public String fname;
    public int size = 0;
    public int extra_params = 0;
    public int out_count = 0;
    public VFunction function;
    public HashSet<Register> used_regs;
    public boolean calls_func = false;
    private Node start = null;
    public BasicBlockContainer bbc = new BasicBlockContainer();

    public CFG(String[] vars, VFunction function) {
        this.fname = function.ident;
        this.function = function;
        this.vars = arrToVars(vars);
        // define parameters
        for (int i = 0; i < function.params.length; i++) {
            VVarRef.Local p = function.params[i];
            searchVar(p.ident).is_paramater = true;
        }
        // define how many more params than param regs
        if (function.params.length - RegisterAllocator.arg_regs.size() > 0)
            this.extra_params = function.params.length - RegisterAllocator.arg_regs.size();
    }

    public static ArrayList<Variable> arrToVars(String[] vars) {
        ArrayList<Variable> ret = new ArrayList<>();
        for (int i = 0; i < vars.length; i++) {
            ret.add(new Variable(vars[i]));
        }
        return ret;
    }

    public static ArrayList<Register> arrToRegs(String[] regs) {
        ArrayList<Register> ret = new ArrayList<>();
        for (int i = 0; i < regs.length; i++) {
            ret.add(new Register(regs[i]));
        }
        return ret;
    }

    public int instrIndexOfLabel(String label) {
        for (VCodeLabel l : function.labels) {
            if (l.ident.equals(label))
                return l.instrIndex;
        }
        return -1;
    }

    public Variable searchVar(String var) {
        for (Variable v : vars) {
            if (v.name.equals(var))
                return v;
        }
        return null;
    }

    public void addLast(Node n) {
        if (start == null)
            start = n;
        else {
            Node last = getLast();
            last.next = n;
            n.in = last;
        }
        size++;
    }

    /**
     * @param i : 0-based indexing
     * @return the node at that index in linked list
     */
    public Node get(int i) {
        if (size == 0 || i < 0 || i >= size)
            return null;
        Node ret = start;
        Node test;
        int curr = 0;
        while (curr != i) {
            curr++;
            test = ret.next;
            if (test == null)
                test = ret.goto_next;
            ret = test;
        }
        return ret;
    }

    public Node getLast() {
        return get(size - 1);
    }

    public void normalize() {
        // normalize in/out flow
        for (int i = 0; i < size; i++) {
            Node curr = get(i);
            if (curr.instr instanceof VBranch) {
                Node br = get(((VBranch) curr.instr).target.getTarget().instrIndex);
                curr.branch_out = br;
                br.branch_in.add(curr);
                br.branch_in_ident.add(((VBranch) curr.instr).target.ident);
            } else if (curr.instr instanceof VGoto
                    && ((VGoto) curr.instr).target instanceof VAddr.Label) {
                Node br = get(instrIndexOfLabel(((VAddr.Label) ((VGoto) curr.instr).target).label.ident));
                curr.branch_out = br;
                br.branch_in.add(curr);
                br.branch_in_ident.add(((VAddr.Label) ((VGoto) curr.instr).target).label.ident);
                // remove in/out
                curr.goto_next = curr.next;
                curr.next.goto_in = curr;
                curr.next.in = null;
                curr.next = null;
            }
        }

        // create BBs
        createBBs();
        bbc.sortAsc();
        
        // liveness analysis
        for (int i = bbc.size() - 1; i >= 0; i--) {
            BasicBlock bb = bbc.get(i);
            bb.computeLiveness();
        }

        // normalize variables
        for (Variable v : vars) {
            v.normalize();
            intervals.addAll(v.intervals);
        }
    }

    private void createBBs() {
        // create basic blocks
        BasicBlock curr = new BasicBlock();
        bbc.add(curr);
        boolean bypass = false;
        for (int i = 0; i < size; i++) {
            // get current
            Node n = get(i);
            if (!n.branch_in.isEmpty()) {
                // branched into
                // make new basic block
                BasicBlock next = new BasicBlock();
                bbc.add(next);
                if (curr.getLast() != null && curr.getLast().next != null) {
                    curr.bb_out.add(next);
                    next.bb_in.add(curr);
                }
                // move along
                curr = next;
                // add this to current
                if (!bypass) {
                    curr.add(n);
                    n.bb = curr;
                }
                bypass = true;
                // process all inputs
                for (Node in_n : n.branch_in) {
                    // add each bb if being branched from behind
                    BasicBlock branch_in = bbc.getAtLine(in_n.line_number);
                    if (branch_in != null && !curr.bb_in.contains(branch_in)) {
                        branch_in.bb_out.add(curr);
                        curr.bb_in.add(branch_in);
                    }
                }
            }

            if (n.branch_out != null) {
                // branching out of this
                // finish this bb
                // connect this bb to bb_out
                BasicBlock bb_out = bbc.getAtLine(n.branch_out.line_number);
                if (bb_out != null && !curr.bb_out.contains(bb_out)) {
                    // add this to input
                    bb_out.bb_in.add(curr);
                    curr.bb_out.add(bb_out);
                }
                // add n to curr
                if (!bypass) {
                    curr.add(n);
                    n.bb = curr;
                }
                bypass = true;
                if (n.next != null) {
                    // create next
                    BasicBlock next = new BasicBlock();
                    bbc.add(next);
                    curr.bb_out.add(next);
                    next.bb_in.add(curr);
                    curr = next;
                }
            }

            if (!bypass) {
                curr.add(n);
                n.bb = curr;
            }
            bypass = false;
        }
    }

    @Override
    public String toString() {
        String ret = "CFG for " + fname + "\n";
        ret += "Vars:\n";
        for (Variable v : vars)
            ret += v + "\n";
        ret += "Basic Blocks:\n" + bbc;
        return ret;
    }

    public static int indexOfHelper(Object[] list, Object i) {
        for (int j = 0; j < list.length; j++)
            if (list[j].equals(i))
                return j;
        return -1;
    }
}
