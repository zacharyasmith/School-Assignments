package V2VM.elements;

import V2VM.CFG.CFG;
import cs132.vapor.ast.VAssign;
import cs132.vapor.ast.VVarRef;

public class EAssign extends Element {
    VAssign statement;
    public EAssign(VAssign statement) {
        super(statement);
        this.statement = statement;
    }

    @Override
    public String toVapor(CFG cfg) {
        String ret = super.toVapor(cfg) + tab + n.assignment.reg + " = ";
        if (statement.source instanceof VVarRef.Local) {
            if (n.assignment.reg.equals(n.accessor_vars.get(0).reg)) return "";
            ret += n.accessor_vars.get(0).reg;
        } else
            ret += statement.source;
        ret += "\n";
        return ret;
    }
}
