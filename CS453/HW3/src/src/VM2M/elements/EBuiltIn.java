package VM2M.elements;

import VM2M.MipsFunction;
import cs132.vapor.ast.VBuiltIn;
import cs132.vapor.ast.VLitInt;
import cs132.vapor.ast.VLitStr;
import cs132.vapor.ast.VOperand;

public class EBuiltIn extends Element {
    VBuiltIn statement;

    public EBuiltIn(VBuiltIn statement) {
        super(statement);
        this.statement = statement;
    }

    @Override
    public String toMIPS(MipsFunction f) {
        String ret = super.toMIPS(f);
        ArgsContainer ac = null;
        if (statement.args.length >= 2)
            ac = new ArgsContainer(statement.args);
        switch (statement.op.name) {
            case "HeapAllocZ":
                if (statement.args[0] instanceof VLitInt)
                    ret += tab + "li $a0 " + statement.args[0] + "\n";
                else
                    ret += tab + "move $a0 " + statement.args[0] + "\n";
                ret += tab + "jal _heapAlloc\n";
                if (statement.dest != null)
                    ret += tab + "move " + statement.dest + " $v0\n";
                break;
            case "PrintIntS":
                if (statement.args[0] instanceof VLitInt)
                    ret += tab + "li $a0 " + statement.args[0] + "\n";
                else
                    ret += tab + "move $a0 " + statement.args[0] + "\n";
                ret += tab + "jal _print\n";
                break;
            case "Error":
                ret += tab + "la $a0 " + f.errMsgLabel(((VLitStr) statement.args[0]).value) + "\n";
                ret += tab + "j _error\n";
                break;
            case "Add":
                if (ac.both_imm) {
                    ret += tab + "li " + statement.dest + " " + (ac.i0 + ac.i1) + "\n";
                } else {
                    ret += tab + "addu " + statement.dest + " " + ac.first +
                            " " + ac.second + "\n";
                }
                break;
            case "Sub":
                if (ac.both_imm) {
                    ret += tab + "li " + statement.dest + " " + (ac.i0 - ac.i1) + "\n";
                } else {
                    ret += tab + "subu " + statement.dest + " " + ac.first +
                            " " + ac.second + "\n";
                    if (ac.has_imm && ac.first != statement.args[0])
                        ret += tab + "mul " + statement.dest + " " +
                                statement.dest + " -1\n";
                }
                break;
            case "MulS":
                if (ac.both_imm) {
                    ret += tab + "li " + statement.dest + " " + (ac.i0 * ac.i1) + "\n";
                } else {
                    ret += tab + "mul " + statement.dest + " " + ac.first +
                            " " + ac.second + "\n";
                }
                break;
            case "Eq":
                ret += tab + "xor " + statement.dest + " " + ac.first +
                        " " + ac.second + "\n";
                ret += tab + "sltiu " + statement.dest + " " + statement.dest +
                        " 1\n";
                break;
            case "Lt":
                if (ac.both_imm) {
                    ret += tab + "li " + statement.dest + " " +
                            (Math.abs(ac.i0) < Math.abs(ac.i1) ? 1 : 0) + "\n";
                } else if (ac.has_imm) {
                    ret += tab + "li $t9 " + ac.second + "\n";
                    if (ac.first != statement.args[0]) {
                        ret += tab + "sltu " + statement.dest + " $t9 " + ac.first + "\n";
                    } else {
                        ret += tab + "sltu " + statement.dest + " " + ac.first + " $t9\n";
                    }
                } else {
                    ret += tab + "slt " + statement.dest + " " + statement.args[0] +
                            " " + statement.args[1] + "\n";
                }
                break;
            case "LtS":
                if (ac.both_imm) {
                    ret += tab + "li " + statement.dest + " " + (ac.i0 < ac.i1 ? 1 : 0) + "\n";
                } else if (ac.has_imm) {
                    ret += tab + "slti " + statement.dest + " " + ac.first +
                            " " + ac.second + "\n";
                } else {
                    ret += tab + "slt " + statement.dest + " " + statement.args[0] +
                            " " + statement.args[1] + "\n";
                }
                break;
            default:
                break;
        }
        return ret;
    }

    class ArgsContainer {
        boolean has_imm = false;
        boolean both_imm = false;
        VOperand first;
        VOperand second;
        int i0 = 0;
        int i1= 0;
        public ArgsContainer(VOperand[] args) {
            if (args[0] instanceof VLitInt || args[1] instanceof VLitInt) {
                has_imm = true;
                if (args[0] instanceof VLitInt && args[1] instanceof VLitInt) {
                    both_imm = true;
                    first = args[0];
                    second = args[1];
                    i0 = ((VLitInt) args[0]).value;
                    i1 = ((VLitInt) args[1]).value;
                    return;
                }
                if (args[0] instanceof VLitInt) {
                    first = args[1];
                    second = args[0];
                } else {
                    first = args[0];
                    second = args[1];
                }
            } else {
                first = args[0];
                second = args[1];
            }
        }
    }
}
