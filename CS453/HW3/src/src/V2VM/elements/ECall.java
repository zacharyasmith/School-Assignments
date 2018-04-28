package V2VM.elements;

import V2VM.CFG.CFG;
import V2VM.Register;
import V2VM.RegisterAllocator;
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
        String ret = super.toVapor(cfg);
        // handle the call
        String call = tab + "call ";
        int accessor_index = 0;
        if (this.statement.addr instanceof VAddr.Var)
            call += n.accessor_vars.get(accessor_index++).reg;
        else
            call += this.statement.addr.toString();
        call += "\n";
        // backup all 8 vars in local
        int j = 0;
        for (Register r : cfg.used_regs)
            ret += tab + "local[" + j++ + "] = " + r + "\n";
        // args list
        int v_index = 0;
        int out_count = 0;
        for (VOperand o : this.statement.args) {
            if (v_index < RegisterAllocator.arg_regs.size())
                ret += tab + RegisterAllocator.arg_regs.get(v_index++) + " = ";
            else
                ret += tab + "out[" + out_count++ + "] = ";
            if (o instanceof VVarRef.Local)
                ret += n.accessor_vars.get(accessor_index++).reg;
            else
                ret += o.toString();
            ret += "\n";
        }
        // append call
        ret += call;
        // restore temps
        j = 0;
        for (Register r : cfg.used_regs)
            ret += tab + r + " = local[" + j++ + "]\n";
        // retrieve return val
        if (this.statement.dest != null)
            ret += tab + n.assignment.reg + " = $v0\n";
        return ret;
    }
}
