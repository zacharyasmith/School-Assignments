package VM2M.elements;

import VM2M.CFG.CFG;
import VM2M.Variable;
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
        Variable.Interval lhs = n.assignment.getIntervalAt(statement.sourcePos.line);
        ret += lhs.spillBefore(statement.sourcePos.line,false);
        if (statement.source instanceof VVarRef.Local) {
            Variable.Interval rhs = n.accessor_vars.get(0).getIntervalAt(statement.sourcePos.line);
            ret += rhs.spillBefore(statement.sourcePos.line,true);
            ret += tab + lhs.register + " = " + rhs.getRegister(statement.sourcePos.line);
        } else {
            ret += tab + lhs.getRegister(statement.sourcePos.line) + " = " + statement.source;
        }
        ret += lhs.spillAfter();
        ret += "\n";
        return ret;
    }
}
