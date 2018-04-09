package V2VM.elements;

import V2VM.CFG.CFG;
import cs132.vapor.ast.VCall;

public class ECall extends Element {
    VCall statement;
    public ECall(VCall statement) {
        this.statement = statement;
    }

    @Override
    public String toVapor(CFG cfg) {
        return null;
    }
}
