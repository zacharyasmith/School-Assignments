package VM2M.elements;

import VM2M.MipsFunction;
import cs132.vapor.ast.VAddr;
import cs132.vapor.ast.VGoto;

public class EGoto extends Element {
    VGoto statement;
    public EGoto(VGoto statement) {
        super(statement);
        this.statement = statement;
    }

    @Override
    public String toMIPS(MipsFunction f) {
        if (statement.target instanceof VAddr.Label)
            return super.toMIPS(f) + tab + "j " + ((VAddr.Label)statement.target).label.ident + "\n";
        throw new RuntimeException("Should have been a VAddr.Label");
    }
}
