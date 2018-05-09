package V2VM.elements;

import V2VM.CFG.CFG;
import V2VM.Variable;
import cs132.vapor.ast.VAddr;
import cs132.vapor.ast.VMemRef;
import cs132.vapor.ast.VMemWrite;
import cs132.vapor.ast.VVarRef;

public class EMemWrite extends Element {
    VMemWrite statement;
    public EMemWrite(VMemWrite statement) {
        super(statement);
        this.statement = statement;
    }

    @Override
    public String toVapor(CFG cfg) {
        String before = super.toVapor(cfg);
        String ret = tab + "[";
        int index = 0;
        // LHS
        if (statement.dest instanceof VMemRef.Global) {
            if (((VMemRef.Global) statement.dest).base instanceof VAddr.Var) {
                Variable.Interval i = n.accessor_vars.get(index++).getIntervalAt(statement.sourcePos.line);
                before += i.spillBefore(statement.sourcePos.line, true);
                ret += i.getRegister(statement.sourcePos.line);
            } else
                ret += statement.dest.toString();
            if (((VMemRef.Global) statement.dest).byteOffset != 0)
                ret += " + " + ((VMemRef.Global) statement.dest).byteOffset;
        }
        ret += "] = ";
        // RHS
        if (statement.source instanceof VVarRef.Local) {
            Variable.Interval i = n.accessor_vars.get(index).getIntervalAt(statement.sourcePos.line);
            before += i.spillBefore(statement.sourcePos.line, true);
            ret += i.getRegister(statement.sourcePos.line);
        } else
            ret += statement.source;
        ret += "\n";
        return before + ret;
    }
}
