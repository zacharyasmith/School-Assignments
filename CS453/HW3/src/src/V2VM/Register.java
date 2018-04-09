package V2VM;

import java.util.ArrayList;
import java.util.Comparator;

public class Register {
    public String reg;
    public ArrayList<Variable> vars;
    public Register(String reg) {
        this.reg = reg;
    }

    @Override
    public int hashCode() {
        return reg.hashCode();
    }

    @Override
    public String toString() {
        return reg;
    }

    public static class RegisterSort implements Comparator<Register> {
        @Override
        public int compare(Register o1, Register o2) {
            return o1.reg.charAt(1) - o2.reg.charAt(1);
        }
    }
}
