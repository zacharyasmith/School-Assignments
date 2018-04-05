package V2VM.CFG;

import V2VM.Variable;
import cs132.vapor.ast.VBranch;

import java.util.ArrayList;

/**
 * One CFG for each function.
 */
public class CFG {
    public ArrayList<Variable> vars = new ArrayList<>();
    public String fname;
    private Node start = null;
    public int size = 0;
    public CFG(String[] vars, String fname) {
        this.fname = fname;
        for (int i = 0; i < vars.length; i++) {
            this.vars.add(new Variable(vars[i]));
        }
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
