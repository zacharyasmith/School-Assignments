package V2VM;

import java.util.ArrayList;

public class Variable {
    public ArrayList<Integer> assign = new ArrayList<>();
    public ArrayList<Integer> access = new ArrayList<>();
    // -1 as default if not assigned before use
    // set to 0 for if a parameter
    public int begin = -1;
    public int end = -1;
    public String name;
    public Variable (String name) {
        this.name = name;
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
        if (!access.isEmpty())
            // treat as global
            return;
        begin = 0;
    }

    @Override
    public String toString() {
        String ret = name + "[" + begin + "," + end + "]" + ": Assign: ";
        for (Integer i : assign)
            ret += i.toString() + " ";
        ret += "Access: ";
        for (Integer i : access)
            ret += i.toString() + " ";
        return ret;
    }
}
