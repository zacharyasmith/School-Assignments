package VM2M.elements;

import VM2M.MipsFunction;
import cs132.vapor.ast.VCall;

public class ECall extends Element {
    VCall statement;
    public ECall(VCall statement) {
        super(statement);
        this.statement = statement;
    }

    @Override
    public String toMIPS(MipsFunction f) {
        String ret = super.toMIPS(f) + tab;
        ret += "jalr " + statement.addr;
        return ret + '\n';
    }
}
