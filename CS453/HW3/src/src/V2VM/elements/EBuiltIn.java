package V2VM.elements;

import V2VM.CFG.CFG;
import cs132.vapor.ast.VBuiltIn;

public class EBuiltIn extends Element {
    VBuiltIn statement;
    public EBuiltIn(VBuiltIn statement) {
        this.statement = statement;
    }

    @Override
    public String toVapor(CFG cfg) {
        return null;
    }
}
