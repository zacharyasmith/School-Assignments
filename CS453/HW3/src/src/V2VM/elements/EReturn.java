package V2VM.elements;

import V2VM.CFG.CFG;
import V2VM.Variable;
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
        String beginning = super.toVapor(cfg);
        String ret = "";
        if (statement.value != null) {
            ret += tab + "$v0 = ";
            if (statement.value instanceof VVarRef.Local) {
                Variable.Interval i = n.accessor_vars.get(0).getIntervalAt(statement.sourcePos.line);
                beginning += i.spillBefore(statement.sourcePos.line, true);
                ret += i.getRegister(statement.sourcePos.line);
            } else
                ret += statement.value;
            ret += "\n";
        }
        ret += tab + "ret\n";
        return beginning + ret;
    }
}
