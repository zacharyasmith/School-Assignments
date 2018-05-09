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
    private ArrayList<Variable.Interval> intervals = new ArrayList<>();
    public int spill_stack = 0;
    // input stack register
    private int stack_register = 0;
    public RegisterAllocator(HashSet<Variable.Interval> intervals,
                             ArrayList<Variable> vars) {
        // add all intervals and sort by start time
        for (Variable.Interval i : intervals)
            this.intervals.add(i);
        this.intervals.sort(new Variable.IntervalStartComparator());
        this.temp_regs = new ArrayList<>(regs);
        this.regs_count = this.temp_regs.size();
        this.temp_arg_regs = new ArrayList<>(arg_regs);
        // go over each variable and assign arg register
        // ** only if a parameter
        for (Variable v : vars)
            if (v.is_paramater)
                v.arg = getNextArgRegister();
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
            return new StackedRegister(stack_register++);
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
            if (active.size() == regs_count)
                SpillAtInterval(i);
            else {
                i.register = getNextRegister();
                makeActive(i);
            }
        }
    }

    private void ExpireOldIntervals(Variable.Interval i) {
        active.sort(new Variable.IntervalEndComparator());
        for (Variable.Interval v : new ArrayList<>(active)) {
            if (v.end < 0 || v.end > i.start)
                return;
            active.remove(v);
            enableRegister(v.register);
        }
    }

    private void SpillAtInterval(Variable.Interval i) {
        assert !active.isEmpty();
        active.sort(new Variable.IntervalEndComparator());
        Variable.Interval spill = active.get(active.size() - 1);
        if (spill.end > i.end) {
            i.stack_borrower = true;
            if (spill.register == null)
                System.out.println("!");
            i.register = spill.register;
            i.stack_location = spill_stack;
            spill.stack_location = spill_stack++;
            active.remove(spill);
            makeActive(i);
        } else
            i.stack_location = spill_stack++;
    }

}
