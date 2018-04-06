package V2VM.CFG;

import V2VM.Variable;
import cs132.vapor.ast.*;

import java.util.ArrayList;

/**
 * One CFG for each function.
 */
public class CFG {
    public ArrayList<Variable> vars = new ArrayList<>();
    public String fname;
    public int size = 0;
    private VFunction function;
    private Node start = null;
    public CFG(String[] vars, VFunction function) {
        this.fname = function.ident;
        this.function = function;
        for (int i = 0; i < vars.length; i++) {
            this.vars.add(new Variable(vars[i]));
        }
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
                br.branch_in = curr;
            } else if (curr.instr instanceof VGoto && ((VGoto) curr.instr).target instanceof VAddr.Label) {
                Node br = get(instrIndexOfLabel(((VAddr.Label) ((VGoto) curr.instr).target).label.ident));
                curr.branch_out = br;
                br.branch_in = curr;
            }
        }
    }

    @Override
    public String toString() {
        String ret = "CFG for " + fname + "\n";
        Node curr = start;
        while (curr != null) {
            ret += curr;
            curr = curr.next;
        }
        return ret;
    }
}
