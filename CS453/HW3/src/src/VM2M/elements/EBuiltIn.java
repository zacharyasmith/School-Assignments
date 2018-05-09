package VM2M.elements;

import VM2M.CFG.CFG;
import VM2M.Variable;
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
        String beginning = super.toVapor(cfg);
        String just_before_call = "";
        String ret = tab;
        Variable.Interval interval;
        // store in register?
        if (this.statement.dest != null) {
            interval = n.assignment.getIntervalAt(statement.sourcePos.line);
            just_before_call += interval.spillBefore(statement.sourcePos.line, false);
            ret += interval.register + " = ";
        }
        ret += this.statement.op.name + "(";
        int i = 0;
        // args
        for (VOperand o : this.statement.args) {
            if (o instanceof VVarRef.Local) {
                interval = n.accessor_vars.get(i++).getIntervalAt(statement.sourcePos.line);
                beginning += interval.spillBefore(statement.sourcePos.line, true);
                ret += interval.getRegister(statement.sourcePos.line);
            } else
                ret += o;
            if (CFG.indexOfHelper(this.statement.args, o)
                    != this.statement.args.length - 1)
                ret += " ";
        }
        ret += ")\n";
        // spill after
        if (this.statement.dest != null) {
            interval = n.assignment.getIntervalAt(statement.sourcePos.line);
            ret += interval.spillAfter();
        }
        return beginning + just_before_call + ret;
    }
}
