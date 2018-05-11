package VM2M.elements;

import VM2M.MipsFunction;
import cs132.vapor.ast.VReturn;

public class EReturn extends Element {
    VReturn statement;
    public EReturn(VReturn statement) {
        super(statement);
        this.statement = statement;
    }

    @Override
    public String toMIPS(MipsFunction f) {
        String ret = super.toMIPS(f);
        ret += f.epilogue();
        return ret;
    }
}
