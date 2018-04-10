package V2VM.elements;

import V2VM.CFG.CFG;
import cs132.vapor.ast.VBranch;
import cs132.vapor.ast.VVarRef;

public class EBranch extends Element {
    VBranch statement;
    public EBranch(VBranch statement) {
        super(statement);
        this.statement = statement;
    }

    @Override
    public String toVapor(CFG cfg) {
        String ret = super.toVapor(cfg) + tab + "if" + (statement.positive ? "" : "0") + " ";
        if (statement.value instanceof VVarRef.Local)
            ret += n.accessor_vars.get(0).reg;
        else
            ret += statement.value;
        ret += " goto " + statement.target + "\n";
        return ret;
    }
}
