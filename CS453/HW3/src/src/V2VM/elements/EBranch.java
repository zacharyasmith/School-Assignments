package V2VM.elements;

import V2VM.CFG.CFG;
import cs132.vapor.ast.VBranch;

public class EBranch extends Element {
    VBranch statement;
    public EBranch(VBranch statement) {
        this.statement = statement;
    }

    @Override
    public String toVapor(CFG cfg) {
        return null;
    }
}
