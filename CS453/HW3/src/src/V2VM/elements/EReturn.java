package V2VM.elements;

import V2VM.CFG.CFG;
import cs132.vapor.ast.VReturn;
import cs132.vapor.ast.VVarRef;

public class EReturn extends Element {
    VReturn statement;
    public EReturn(VReturn statement) {
        super(statement);
        this.statement = statement;
    }

    @Override
    public String toVapor(CFG cfg) {
        String ret = super.toVapor(cfg);
        if (statement.value != null) {
            ret += tab + "$v0 = ";
            if (statement.value instanceof VVarRef.Local)
                ret += n.accessor_vars.get(0).reg;
            else
                ret += statement.value;
            ret += "\n";
        }
        ret += tab + "ret\n";
        return ret;
    }
}
