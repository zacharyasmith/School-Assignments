package V2VM.elements;

import V2VM.CFG.CFG;
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
        String ret = super.toVapor(cfg) + tab;
        if (statement.dest != null)
            ret += n.assignment.reg + " = [";
        if (statement.source instanceof VMemRef.Global) {
            if (((VMemRef.Global) statement.source).base instanceof VAddr.Var)
                ret += n.accessor_vars.get(0).reg;
            else
                ret += ((VMemRef.Global) statement.source).base.toString();
            if (((VMemRef.Global) statement.source).byteOffset != 0)
                ret += " + " + ((VMemRef.Global) statement.source).byteOffset / 4;
        }
        ret += "]\n";
        return ret;
    }
}
