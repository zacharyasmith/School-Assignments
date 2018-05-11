package VM2M.elements;

import VM2M.MipsFunction;
import cs132.vapor.ast.VLabelRef;
import cs132.vapor.ast.VLitInt;
import cs132.vapor.ast.VMemRef;
import cs132.vapor.ast.VMemWrite;

public class EMemWrite extends Element {
    VMemWrite statement;
    public EMemWrite(VMemWrite statement) {
        super(statement);
        this.statement = statement;
    }

    @Override
    public String toMIPS(MipsFunction f) {
        String begin = super.toMIPS(f) + tab;
        String fnc = "sw";
        String src = statement.source.toString();
        String rst = "";
        if (statement.dest instanceof VMemRef.Global) {
            rst += ((VMemRef.Global) statement.dest).byteOffset + "(" +
                    ((VMemRef.Global) statement.dest).base + ")";
        } else if (statement.dest instanceof VMemRef.Stack){
            if (((VMemRef.Stack) statement.dest).region == VMemRef.Stack.Region.Local) {
                rst += f.stack_out * 4 + ((VMemRef.Stack) statement.dest).index * 4;
                rst += "($sp)";
            } else if (((VMemRef.Stack) statement.dest).region == VMemRef.Stack.Region.In) {
                rst += ((VMemRef.Stack) statement.dest).index * 4;
                rst += "($fp)";
            } else { // Out
                rst += ((VMemRef.Stack) statement.dest).index * 4;
                rst += "($sp)";
            }
        }
        if (statement.source instanceof VLabelRef) {
            begin += "la $t9 " + ((VLabelRef) statement.source).ident + '\n' + tab;
            src = "$t9";
        } else if (statement.source instanceof VLitInt) {
            begin += "li $t9 " + statement.source + '\n' + tab;
            src = "$t9";
        }
        return begin + fnc + " " + src + " " + rst + '\n';
    }
}
