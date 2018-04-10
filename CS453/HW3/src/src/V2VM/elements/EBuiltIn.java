package V2VM.elements;

import V2VM.CFG.CFG;
import cs132.vapor.ast.VBuiltIn;
import cs132.vapor.ast.VOperand;
import cs132.vapor.ast.VVarRef;

public class EBuiltIn extends Element {
    VBuiltIn statement;
    public EBuiltIn(VBuiltIn statement) {
        super(statement);
        this.statement = statement;
    }

    @Override
    public String toVapor(CFG cfg) {
        String ret = super.toVapor(cfg) + tab;
        // store in register?
        if (this.statement.dest != null)
            ret += n.assignment.reg + " = ";
        ret += this.statement.op.name + "(";
        int i = 0;
        // args
        for (VOperand o : this.statement.args) {
            if (o instanceof VVarRef.Local)
                ret += n.accessor_vars.get(i++).reg;
            else
                ret += o;
            if (CFG.indexOfHelper(this.statement.args, o)
                    != this.statement.args.length - 1)
                ret += " ";
        }
        ret += ")\n";
        return ret;
    }
}
