package V2VM;

import java.util.ArrayList;
import java.util.HashSet;

public class RegisterAllocator {
    // public static register lists
    public static ArrayList<Register> regs;
    public static ArrayList<Register> arg_regs;
    private ArrayList<Register> temp_arg_regs;
    private ArrayList<Variable> vars;
    private ArrayList<Register> temp_regs;
    public HashSet<Register> used_regs = new HashSet<>();
    private int regs_count;
    // Sorted by increasing endpoint
    private ArrayList<Variable> active;
    int stack = 0;
    public RegisterAllocator(ArrayList<Variable> vars) {
        this.vars = vars;
        this.temp_regs = new ArrayList<>(regs);
        this.regs_count = this.temp_regs.size();
        this.temp_arg_regs = new ArrayList<>(arg_regs);
    }

    private void enableRegister(Register r) {
        temp_regs.add(r);
        temp_regs.sort(new Register.RegisterSort());
    }

    private Register getNextRegister() {
        assert !temp_regs.isEmpty();
        Register ret = temp_regs.get(0);
        used_regs.add(ret);
        temp_regs.remove(0);
        return ret;
    }

    private Register getNextArgRegister() {
        if (temp_arg_regs.size() == 0)
            return new StackedRegister("");
        Register ret = temp_arg_regs.get(0);
        temp_arg_regs.remove(0);
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
            if (v.begin == 0) {
                v.arg = getNextArgRegister();
            }
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
