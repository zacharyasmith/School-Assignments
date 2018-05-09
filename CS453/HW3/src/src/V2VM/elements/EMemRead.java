package V2VM.elements;

import V2VM.CFG.CFG;
import V2VM.Variable;
import cs132.vapor.ast.VAddr;
import cs132.vapor.ast.VMemRead;
import cs132.vapor.ast.VMemRef;

public class EMemRead extends Element {
    VMemRead statement;
    public EMemRead(VMemRead statement) {
        super(statement);
        this.statement = statement;
    }

    @Override
    public String toVapor(CFG cfg) {
        String before = super.toVapor(cfg);
        String ret = "";
        // LHS
        if (statement.dest != null) {
            Variable.Interval i = n.assignment.getIntervalAt(statement.sourcePos.line);
            ret += i.spillBefore(statement.sourcePos.line, false);
            ret += tab + i.register + " = [";
        }
        // RHS
        if (statement.source instanceof VMemRef.Global) {
            if (((VMemRef.Global) statement.source).base instanceof VAddr.Var) {
                Variable.Interval i = n.accessor_vars.get(0).getIntervalAt(statement.sourcePos.line);
                before += i.spillBefore(statement.sourcePos.line, true);
                ret += i.getRegister(statement.sourcePos.line);
            } else
                ret += ((VMemRef.Global) statement.source).base.toString();
            if (((VMemRef.Global) statement.source).byteOffset != 0)
                ret += " + " + ((VMemRef.Global) statement.source).byteOffset;
        }
        ret += "]\n";
        // SPILL AFTER
        if (statement.dest != null) {
            Variable.Interval i = n.assignment.getIntervalAt(statement.sourcePos.line);
            ret += i.spillAfter();
        }
        return before + ret;
    }
}
