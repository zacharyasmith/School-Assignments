package V2VM.elements;

import V2VM.CFG.CFG;
import V2VM.Register;
import V2VM.RegisterAllocator;
import V2VM.Variable;
import cs132.vapor.ast.VAddr;
import cs132.vapor.ast.VCall;
import cs132.vapor.ast.VOperand;
import cs132.vapor.ast.VVarRef;

public class ECall extends Element {
    VCall statement;
    public ECall(VCall statement) {
        super(statement);
        this.statement = statement;
    }

    @Override
    public String toVapor(CFG cfg) {
        String beginning = super.toVapor(cfg);
        String ret = "";

        // handle the call
        String call = tab + "call ";
        int accessor_index = 0;
        if (this.statement.addr instanceof VAddr.Var) {
            Variable.Interval i = n.accessor_vars.get(accessor_index++).getIntervalAt(statement.sourcePos.line);
            beginning += i.spillBefore(statement.sourcePos.line, true);
            call += i.getRegister(statement.sourcePos.line);
        } else
            call += this.statement.addr.toString();
        call += "\n";

        // backup all 8 vars in local
        int j = cfg.spill_stack_size;
        for (Register r : cfg.used_regs)
            ret += tab + "local[" + j++ + "] = " + r + "\n";

        // args list
        int v_index = 0;
        int out_count = 0;
        for (VOperand o : this.statement.args) {
            // spill before use
            String before = "";
            String operand = "";
            // use either arg register or out array
            if (v_index < RegisterAllocator.arg_regs.size())
                operand += tab + RegisterAllocator.arg_regs.get(v_index++) + " = ";
            else
                operand += tab + "out[" + out_count++ + "] = ";
            // argument stored in register or is primitive
            if (o instanceof VVarRef.Local) {
                Variable.Interval i = n.accessor_vars.get(accessor_index++).getIntervalAt(statement.sourcePos.line);
                before += i.spillBefore(statement.sourcePos.line, true);
                operand += i.getRegister(statement.sourcePos.line);
            } else
                operand += o.toString();
            // finish with newline
            ret += before + operand + "\n";
        }

        // append call
        ret += call;

        // restore temps
        j = cfg.spill_stack_size;
        for (Register r : cfg.used_regs)
            ret += tab + r + " = local[" + j++ + "]\n";

        // retrieve return val
        if (this.statement.dest != null) {
            Variable.Interval i = n.assignment.getIntervalAt(statement.sourcePos.line);
            ret += i.spillBefore(statement.sourcePos.line, false);
            ret += tab + i.register + " = $v0\n";
            ret += i.spillAfter();
        }
        return beginning + ret;
    }
}
