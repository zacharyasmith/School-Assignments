package V2VM;

import java.util.ArrayList;

public class RegisterAllocator {
    private ArrayList<Variable> vars;
    private ArrayList<String> regs;
    // Sorted by increasing endpoint
    private ArrayList<Variable> active = new ArrayList<>();
    public RegisterAllocator(ArrayList<Variable> vars, ArrayList<String> regs) {
        this.vars = vars;
        this.regs = new ArrayList<>(regs);
        // order by increasing start point
        this.vars.sort(new VariableStartComparator());
    }

    public void LinearScanRegisterAllocation() {
        for (Variable v : vars) {
            ExpireOldIntervals(v);

        }
    }

    private void ExpireOldIntervals(Variable i) {

    }

    private void SpillAtInterval(Variable i) {

    }

}
