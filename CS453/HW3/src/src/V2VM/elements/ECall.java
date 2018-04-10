package V2VM.elements;

import V2VM.CFG.CFG;
import V2VM.Register;
import cs132.vapor.ast.VAddr;
import cs132.vapor.ast.VCall;
import cs132.vapor.ast.VOperand;
import cs132.vapor.ast.VVarRef;

import java.util.ArrayList;

public class ECall extends Element {
    VCall statement;
    public ECall(VCall statement) {
        super(statement);
        this.statement = statement;
    }

    @Override
    public String toVapor(CFG cfg) {
        // TODO switch to vapor m
        // TODO callee stuff
        String ret = super.toVapor(cfg);
        // handle the call
        String call = tab + "call ";
        int accessor_index = 0;
        if (this.statement.addr instanceof VAddr.Var)
            call += n.accessor_vars.get(accessor_index++).reg;
        else
            call += this.statement.addr.toString();
        call += "\n";
        // handle args
        // args list
        String[] regs = {"a0", "a1", "a2", "a3"};
        ArrayList<Register> vars = CFG.arrToRegs(regs);
        int v_index = 0;
        for (VOperand o : this.statement.args) {
            if (v_index < vars.size())
                ret += tab + vars.get(v_index++) + " = ";
            if (o instanceof VVarRef.Local)
                ret += n.accessor_vars.get(accessor_index++).reg;
            else
                ret += o.toString();
            ret += "\n";
        }
        if (this.statement.dest != null)
            call += tab + n.assignment.reg + " = $v0\n";
        return ret + call;
    }
}
