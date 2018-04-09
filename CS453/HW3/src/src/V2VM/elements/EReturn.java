package V2VM.elements;

import V2VM.CFG.CFG;
import cs132.vapor.ast.VReturn;

public class EReturn implements Element {
    VReturn statement;
    public EReturn(VReturn statement) {
        this.statement = statement;
    }

    @Override
    public String toVapor(CFG cfg) {
        return null;
    }
}
