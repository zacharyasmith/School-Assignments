package V2VM;

import java.util.ArrayList;
import java.util.HashSet;

public class RegisterAllocator {
    // public static register lists
    public static ArrayList<Register> regs;
    public static ArrayList<Register> arg_regs;
    private ArrayList<Register> temp_arg_regs;
    private ArrayList<Register> temp_regs;
    public HashSet<Register> used_regs = new HashSet<>();
    private int regs_count;
    // Sorted by increasing endpoint
    private ArrayList<Variable.Interval> active;
    private ArrayList<Variable.Interval> intervals;
    int stack = 0;
    public RegisterAllocator(ArrayList<Variable.Interval> intervals) {
        // add all intervals and sort by start time
        this.intervals = intervals;
        intervals.sort(new Variable.IntervalStartComparator());
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
            return new StackedRegister();
        Register ret = temp_arg_regs.get(0);
        temp_arg_regs.remove(0);
        return ret;
    }

    private void makeActive(Variable.Interval v) {
        active.add(v);
        active.sort(new Variable.IntervalEndComparator());
    }

    public void LinearScanRegisterAllocation() {
        active = new ArrayList<>();
        for (Variable.Interval i : intervals) {
            ExpireOldIntervals(i);
            if (i.variable.is_paramater && i.variable.arg == null) {
                i.variable.arg = getNextArgRegister();
            }
            if (active.size() == regs_count)
                SpillAtInterval(i);
            else {
                i.register = getNextRegister();
                makeActive(i);
            }
        }
    }

    private void ExpireOldIntervals(Variable.Interval i) {
        for (Variable.Interval v : new ArrayList<>(active)) {
            if (v.end < 0 || v.end > i.start)
                return;
            active.remove(v);
            enableRegister(v.register);
        }
    }

    private void SpillAtInterval(Variable.Interval i) {
        assert !active.isEmpty();
        Variable.Interval spill = active.get(active.size() - 1);
        if (spill.end > i.end) {
            i.register = spill.register;
            i.stack_location = stack;
            spill.stack_location = stack++;
            active.remove(spill);
            makeActive(i);
        } else
            i.stack_location = stack++;
    }

}
