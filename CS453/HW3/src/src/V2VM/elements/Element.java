package V2VM.elements;

import V2VM.CFG.CFG;
import V2VM.CFG.Node;
import cs132.vapor.ast.VInstr;

public abstract class Element {
    public static final String tab = "  ";
    public Node n = null;
    public VInstr i;
    public Element(VInstr i) {
        this.i = i;
    }

    public String toVapor(CFG cfg) {
        String ret = "";
        for (String ident : n.branch_in_ident)
            ret += ident + ":\n";
        return ret;
    }
}
