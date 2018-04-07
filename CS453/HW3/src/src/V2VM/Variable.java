package V2VM;

import java.util.ArrayList;
import java.util.Comparator;

public class Variable {
    public ArrayList<Integer> assign = new ArrayList<>();
    public ArrayList<Integer> access = new ArrayList<>();
    // -1 as default if not assigned before use
    // set to 0 for if a parameter
    public int begin = -1;
    public int end = -1;
    public String name;
    public Register reg = null;
    public int stack_location = -1;
    public Variable (String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Variable variable = (Variable) o;

        return name.equals(variable.name);
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }

    public void normalize() {
        if (!assign.isEmpty()) {
            begin = assign.get(0);
            if (!access.isEmpty()) {
                end = access.get(access.size() - 1);
                return;
            }
            // assigned but not used.
            end = begin;
            return;
        }
        if (!access.isEmpty()) {
            // global or parameter
            begin = 0;
            end = access.get(access.size() - 1);
            return;
        }
        // parameter that was not used
        begin = 0;
        end = 0;
    }

    @Override
    public String toString() {
        String ret = (reg == null ? "" : reg  + ": ");
        ret += name + "[" + begin + "," + end + "," + stack_location + "]" + ": Assign: ";
        for (Integer i : assign)
            ret += i.toString() + " ";
        ret += "Access: ";
        for (Integer i : access)
            ret += i.toString() + " ";
        return ret;
    }

    public static class VariableEndComparator implements Comparator<Variable> {
        @Override
        public int compare(Variable o1, Variable o2) {
            return o1.end - o2.end;
        }
    }

    public static class VariableStartComparator implements Comparator<Variable> {
        @Override
        public int compare(Variable o1, Variable o2) {
            return o1.begin - o2.begin;
        }
    }
}
