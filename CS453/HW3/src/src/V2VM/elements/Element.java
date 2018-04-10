package V2VM.elements;

import V2VM.CFG.CFG;
import V2VM.CFG.Node;
import cs132.vapor.ast.VAddr;
import cs132.vapor.ast.VBranch;
import cs132.vapor.ast.VGoto;
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
        if (n.branch_in != null) {
            if (n.branch_in.instr instanceof VBranch) {
                ret += ((VBranch) n.branch_in.instr).target.ident + ":";
            } else if (n.branch_in.instr instanceof VGoto &&
                    ((VGoto) n.branch_in.instr).target instanceof VAddr.Label) {
                ret += ((VAddr.Label) ((VGoto) n.branch_in.instr).target).label.ident + ":";
            }
            ret += "\n";
        }
        return ret;
    }
}
