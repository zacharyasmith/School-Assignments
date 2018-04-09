package V2VM;

import java.util.ArrayList;
import java.util.HashSet;

public class RegisterAllocator {
    private ArrayList<Variable> vars;
    private ArrayList<Register> regs;
    public HashSet<Register> used_regs = new HashSet<>();
    private int regs_count;
    // Sorted by increasing endpoint
    private ArrayList<Variable> active;
    int stack = 0;
    public RegisterAllocator(ArrayList<Variable> vars, ArrayList<String> regs) {
        this.vars = vars;
        this.regs = new ArrayList<>();
        for (String r : regs)
            enableRegister(new Register(r));
        this.regs_count = this.regs.size();
        // order by increasing start point
        this.vars.sort(new Variable.VariableStartComparator());
    }

    private void enableRegister(Register r) {
        regs.add(r);
        regs.sort(new Register.RegisterSort());
    }

    private Register getNextRegister() {
        assert !regs.isEmpty();
        Register ret = regs.get(0);
        used_regs.add(ret);
        regs.remove(0);
        return ret;
    }

    private void makeActive(Variable v) {
        active.add(v);
        active.sort(new Variable.VariableEndComparator());
    }

    public void LinearScanRegisterAllocation() {
        active = new ArrayList<>();
        for (Variable v : vars) {
            if (v.begin == v.end)
                continue;
            ExpireOldIntervals(v);
            if (active.size() == regs_count)
                SpillAtInterval(v);
            else {
                v.reg = getNextRegister();
                makeActive(v);
            }
        }
    }

    private void ExpireOldIntervals(Variable i) {
        for (Variable v : new ArrayList<>(active)) {
            if (v.end < 0 || v.end > i.begin)
                return;
            active.remove(v);
            enableRegister(v.reg);
        }
    }

    private void SpillAtInterval(Variable i) {
        assert !active.isEmpty();
        Variable spill = active.get(active.size() - 1);
        if (spill.end > i.end) {
            i.reg = spill.reg;
            spill.stack_location = stack++;
            active.remove(spill);
            makeActive(i);
        } else
            i.stack_location = stack++;
    }

}
