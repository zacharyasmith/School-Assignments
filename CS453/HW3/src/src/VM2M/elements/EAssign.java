package VM2M.elements;

import VM2M.MipsFunction;
import cs132.vapor.ast.VAssign;
import cs132.vapor.ast.VVarRef;

public class EAssign extends Element {
    VAssign statement;
    public EAssign(VAssign statement) {
        super(statement);
        this.statement = statement;
    }

    @Override
    public String toMIPS(MipsFunction f) {
        String ret = super.toMIPS(f) + tab;
        if (statement.source instanceof VVarRef.Register)
            ret += "move " + statement.dest + " " + statement.source;
        else
            ret += "li " + statement.dest + " " + statement.source;
        return ret + '\n';
    }
}
