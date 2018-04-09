package V2VM.elements;

import V2VM.CFG.CFG;
import cs132.vapor.ast.VMemWrite;

public class EMemWrite extends Element {
    VMemWrite statement;
    public EMemWrite(VMemWrite statement) {
        this.statement = statement;
    }

    @Override
    public String toVapor(CFG cfg) {
        return null;
    }
}
