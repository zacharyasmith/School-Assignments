package V2VM;

import java.util.ArrayList;

public class Register {
    public String reg;
    public ArrayList<Variable> vars;
    public Register(String reg) {
        this.reg = reg;
    }

    @Override
    public String toString() {
        return reg;
    }
}
