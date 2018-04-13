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
    public String fname;
    public int size = 0;
    public int extra_params = 0;
    public int out_count = 0;
    public VFunction function;
    public HashSet<Register> used_regs;
    public boolean calls_func = false;
    private Node start = null;
    public CFG(String[] vars, VFunction function) {
        this.fname = function.ident;
        this.function = function;
        this.vars = arrToVars(vars);
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
        int curr = 0;
        while (curr != i) {
            curr++;
            ret = ret.next;
        }
        return ret;
    }

    public Node getLast() {
        return get(size - 1);
    }

    public void normalize() {
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
            }
        }
        for (Variable v : vars)
            v.normalize();
    }

    @Override
    public String toString() {
        String ret = "CFG for " + fname + "\n";
        ret += "Vars:\n";
        for (Variable v : vars)
            ret += v + "\n";
        Node curr = start;
        while (curr != null) {
            ret += curr + "\n";
            curr = curr.next;
        }
        return ret;
    }

    public static int indexOfHelper(Object[] list, Object i) {
        for (int j = 0; j < list.length; j++)
            if (list[j].equals(i))
                return j;
        return -1;
    }
}
