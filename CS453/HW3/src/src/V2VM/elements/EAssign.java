package V2VM.elements;

import V2VM.CFG.CFG;
import cs132.vapor.ast.VAssign;

public class EAssign implements Element {
    VAssign statement;
    public EAssign(VAssign statement) {
        this.statement = statement;
    }

    @Override
    public String toVapor(CFG cfg) {
        return null;
    }
}
