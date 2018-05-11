package VM2M.elements;

import VM2M.MipsFunction;
import cs132.vapor.ast.VBranch;
import cs132.vapor.ast.VLitInt;

public class EBranch extends Element {
    VBranch statement;
    public EBranch(VBranch statement) {
        super(statement);
        this.statement = statement;
    }

    @Override
    public String toMIPS(MipsFunction f) {
        String ret = super.toMIPS(f);
        if (statement.value instanceof VLitInt) {
            if ((((VLitInt) statement.value).value > 0 && statement.positive) ||
                    (((VLitInt) statement.value).value == 0 && !statement.positive))
                ret += tab + "j " + statement.target.ident + '\n';
        } else {
            String branch = (statement.positive ? "bnez " : "beqz ");
            ret += tab + branch + statement.value + " " + statement.target.ident + '\n';
        }
        return ret;
    }
}
