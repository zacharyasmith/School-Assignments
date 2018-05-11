package VM2M.elements;

import VM2M.MipsFunction;
import cs132.vapor.ast.VAddr;
import cs132.vapor.ast.VCall;
import cs132.vapor.ast.VFunction;

public class ECall extends Element {
    VCall statement;
    public ECall(VCall statement) {
        super(statement);
        this.statement = statement;
    }

    @Override
    public String toMIPS(MipsFunction f) {
        String ret = super.toMIPS(f) + tab;
        if (statement.addr instanceof VAddr.Label) {
            ret += "jal " + ((VAddr.Label<VFunction>) statement.addr).label.ident;
        } else {
            ret += "jalr " + statement.addr.toString();
        }
        return ret + '\n';
    }
}
