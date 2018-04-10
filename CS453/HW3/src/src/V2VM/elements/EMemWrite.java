package V2VM.elements;

import V2VM.CFG.CFG;
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
        String ret = super.toVapor(cfg) + tab + "[";
        int index = 0;
        if (statement.dest instanceof VMemRef.Global) {
            if (((VMemRef.Global) statement.dest).base instanceof VAddr.Var)
                ret += n.accessor_vars.get(index++).reg;
            else
                ret += statement.dest.toString();
            if (((VMemRef.Global) statement.dest).byteOffset != 0)
                ret += " + " + ((VMemRef.Global) statement.dest).byteOffset;
        }
        ret += "] = ";
        if (statement.source instanceof VVarRef.Local)
            ret += n.accessor_vars.get(index).reg;
        else
            ret += statement.source;
        ret += "\n";
        return ret;
    }
}
