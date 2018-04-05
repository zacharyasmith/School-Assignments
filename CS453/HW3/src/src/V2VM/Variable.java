package V2VM;

import java.util.ArrayList;

public class Variable {
    public ArrayList<Integer> assign = new ArrayList<>();
    public ArrayList<Integer> access = new ArrayList<>();
    // -1 as default if not assigned before use
    // set to 0 for if a parameter
    public int begin = -1;
    // -1 iff begin==-1 throughout function
    public int end = -1;
    public String name;
    public Variable (String name) {
        this.name = name;
    }
}
