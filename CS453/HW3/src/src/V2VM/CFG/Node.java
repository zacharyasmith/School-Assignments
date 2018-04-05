package V2VM.CFG;

import V2VM.Variable;
import cs132.vapor.ast.VInstr;

import java.util.ArrayList;

/**
 * Each statement in Vapor will have a Node
 * according to it's line #, assignment variable,
 * accessor variable(s), input Node, and output Node.
 */
public class Node {
    public VInstr instr;
    public Node in = null;
    public Node branch_in = null;
    public Node next = null;
    public Node branch_out = null;
    public int line_number;
    public ArrayList<Variable> accessor_vars = new ArrayList<>();
    public Variable assignment = null;
    public Node(VInstr instr, int line) {
        this.instr = instr;
        this.line_number = line;
    }

    @Override
    public String toString() {
        String ret = "";
        if (in != null)
            ret += in.line_number + " ";
        else
            ret += "start ";
        if (branch_in != null)
            ret += branch_in.line_number + " ";
        ret +=  "-> " + line_number +
                (next != null || branch_out != null ? " -> " : "");
        if (next != null)
            ret += next.line_number;
        if (branch_out != null)
            ret += " " + branch_out.line_number;
        ret += '\n';
        return ret;
    }
}
