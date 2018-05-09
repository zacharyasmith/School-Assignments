package V2VM.elements;

import V2VM.CFG.CFG;
import V2VM.Variable;
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
        String before = super.toVapor(cfg);
        String ret = tab + "if" + (statement.positive ? "" : "0") + " ";
        if (statement.value instanceof VVarRef.Local) {
            Variable.Interval i = n.accessor_vars.get(0).getIntervalAt(statement.sourcePos.line);
            before += i.spillBefore(statement.sourcePos.line, true);
            ret += i.getRegister(statement.sourcePos.line);
        } else
            ret += statement.value;
        ret += " goto " + statement.target + "\n";
        return before + ret;
    }
}
