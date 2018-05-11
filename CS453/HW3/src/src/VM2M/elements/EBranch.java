package VM2M.elements;

import VM2M.MipsFunction;
import cs132.vapor.ast.VBranch;

public class EBranch extends Element {
    VBranch statement;
    public EBranch(VBranch statement) {
        super(statement);
        this.statement = statement;
    }

    @Override
    public String toMIPS(MipsFunction f) {
        String ret = super.toMIPS(f) + tab;
        String branch = (statement.positive ? "bnez " : "beqz ");
        ret += branch + statement.value + " " + statement.target.ident;
        return ret + '\n';
    }
}
