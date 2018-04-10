package V2VM.elements;

import V2VM.CFG.CFG;
import cs132.vapor.ast.VGoto;

public class EGoto extends Element {
    VGoto statement;
    public EGoto(VGoto statement) {
        super(statement);
        this.statement = statement;
    }

    @Override
    public String toVapor(CFG cfg) {
        return super.toVapor(cfg) + tab + "goto " + statement.target + "\n";
    }
}
