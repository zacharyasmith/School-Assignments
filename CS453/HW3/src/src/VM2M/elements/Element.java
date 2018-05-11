package VM2M.elements;

import VM2M.MipsFunction;
import VM2M.Node;
import cs132.vapor.ast.VCodeLabel;
import cs132.vapor.ast.VInstr;

public abstract class Element {
    public static final String tab = "  ";
    public Node n = null;
    public VInstr i;
    public Element(VInstr i) {
        this.i = i;
    }

    public String toMIPS(MipsFunction f) {
        String ret = "";
        int index = 0;
        for (VInstr i : f.f.body) {
            if (i.equals(this.i))
                break;
            index++;
        }
        for (VCodeLabel l : f.f.labels) {
            if (l.instrIndex > index)
                break;
            if (l.instrIndex == index)
                ret += l.ident + ":\n";
        }
        return ret;
    }
}
