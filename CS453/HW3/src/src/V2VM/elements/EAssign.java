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
        String ret = super.toVapor(cfg);
        if (statement.source instanceof VVarRef.Local) {
            if (n.assignment.reg.equals(n.accessor_vars.get(0).reg)) return ret;
            ret += tab + n.assignment.reg + " = " + n.accessor_vars.get(0).reg;
        } else
            ret += tab + n.assignment.reg + " = " + statement.source;
        ret += "\n";
        return ret;
    }
}
