package VM2M.elements;

import VM2M.MipsFunction;
import cs132.vapor.ast.VMemRead;
import cs132.vapor.ast.VMemRef;

public class EMemRead extends Element {
    VMemRead statement;
    public EMemRead(VMemRead statement) {
        super(statement);
        this.statement = statement;
    }

    @Override
    public String toMIPS(MipsFunction f) {
        String ret = super.toMIPS(f) + tab;
        ret += "lw " + statement.dest + " ";
        if (statement.source instanceof VMemRef.Global)
            ret += ((VMemRef.Global) statement.source).byteOffset + "(" +
                    ((VMemRef.Global) statement.source).base + ")";
        else if(statement.source instanceof VMemRef.Stack){
            if (((VMemRef.Stack) statement.source).region == VMemRef.Stack.Region.Local) {
                ret += f.stack_out * 4 + ((VMemRef.Stack) statement.source).index * 4;
                ret += "($sp)";
            } else if (((VMemRef.Stack) statement.source).region == VMemRef.Stack.Region.In) {
                ret += ((VMemRef.Stack) statement.source).index * 4;
                ret += "($fp)";
            } else { // Out
                ret += ((VMemRef.Stack) statement.source).index * 4;
                ret += "($sp)";
            }
        }
        return ret + '\n';
    }
}
