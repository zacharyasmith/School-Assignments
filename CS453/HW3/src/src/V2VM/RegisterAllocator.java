package V2VM;

import java.util.ArrayList;

public class RegisterAllocator {
    private ArrayList<Variable> vars;
    private ArrayList<Register> regs;
    // Sorted by increasing endpoint
    private ArrayList<Variable> active;
    int stack = 0;
    public RegisterAllocator(ArrayList<Variable> vars, ArrayList<String> regs) {
        this.vars = vars;
        this.regs = new ArrayList<>();
        for (String r : regs)
            this.regs.add(new Register(r));
        // order by increasing start point
        this.vars.sort(new Variable.VariableStartComparator());
    }

    private Register getNextRegister() {
        assert !regs.isEmpty();
        Register ret = regs.get(0);
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
            ExpireOldIntervals(v);
            if (active.size() == regs.size())
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
            regs.add(v.reg);
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
