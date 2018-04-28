package V2VM.CFG;

import V2VM.elements.EGoto;
import V2VM.elements.Element;
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
    public ArrayList<Node> branch_in = new ArrayList<>();
    public ArrayList<String> branch_in_ident = new ArrayList<>();
    public Node next = null;
    public Node branch_out = null;
    public int line_number;
    public Element element;
    public ArrayList<Variable> accessor_vars = new ArrayList<>();
    public Variable assignment = null;
    // goto placeholder
    public boolean is_goto = false;
    public Node goto_next = null;
    public Node goto_in = null;
    public Node(int line, Element e) {
        this.instr = e.i;
        this.element = e;
        this.element.n = this;
        this.line_number = line;
        if (e instanceof EGoto)
            is_goto = true;
    }

    public void setAssignment(Variable v) {
        v.assign.add(line_number);
        assignment = v;
    }

    public void addAccessor(Variable v) {
        v.access.add(line_number);
        accessor_vars.add(v);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Node node = (Node) o;

        return line_number == node.line_number;
    }

    @Override
    public int hashCode() {
        return line_number;
    }

    @Override
    public String toString() {
        String ret = element.getClass().getSimpleName() + " ";
        // Inputs
        if (in != null)
            ret += in.line_number + " ";
        if (goto_in != null)
            ret += "[" + goto_in.line_number + "] ";
        if (in == null && goto_in == null)
            ret += "start ";
        if (!branch_in.isEmpty()) {
            ret += "{";
            for (Node n : branch_in)
                ret += n.line_number + ",";
            ret += "}";
        }
        // node line number
        ret +=  "-> " + line_number;
        if (next != null || branch_out != null || goto_next != null) {
            ret += " ->";
            // outputs
            if (next != null)
                ret += " " + next.line_number;
            if (branch_out != null)
                ret += " {" + branch_out.line_number + "}";
            if (goto_next != null)
                ret += " [" + goto_next.line_number + "]";
        }
        return ret;
    }
}
