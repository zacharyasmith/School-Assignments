package VM2M.elements;

import VM2M.MipsFunction;
import cs132.vapor.ast.*;

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
        else if (statement.source instanceof VLitInt)
            ret += "li " + statement.dest + " " + statement.source;
        else if (statement.source instanceof VLabelRef)
            ret += "la " + statement.dest + " " + ((VLabelRef) statement.source).ident;
        return ret + '\n';
    }
}
