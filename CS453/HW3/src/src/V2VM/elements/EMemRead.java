package V2VM.elements;

import V2VM.CFG.CFG;
import cs132.vapor.ast.VMemRead;

public class EMemRead implements Element {
    VMemRead statement;
    public EMemRead(VMemRead statement) {
        this.statement = statement;
    }

    @Override
    public String toVapor(CFG cfg) {
        return null;
    }
}
