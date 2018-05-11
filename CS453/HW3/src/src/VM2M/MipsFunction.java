package VM2M;

import VM2M.elements.Element;
import cs132.vapor.ast.VFunction;

import java.util.ArrayList;

public class MipsFunction {
    public static final String tab = "  ";
    public int frame_size;
    public static int frame_offset = 8;
    public ArrayList<Element> elements = new ArrayList<>();
    public VFunction f;
    public int stack_local;
    public int stack_out;
    public int stack_in;

    public MipsFunction(VFunction f) {
        this.f = f;
        this.frame_size = (f.stack.local * 4) + (f.stack.out * 4) + frame_offset;
        this.stack_in = f.stack.in;
        this.stack_local = f.stack.local;
        this.stack_out = f.stack.out;
    }

    public void addElement(Element e) {
        this.elements.add(e);
    }

    public String prologue() {
        String ret = tab + "sw $fp -8($sp)\n";
        ret += tab + "move $fp $sp\n";
        ret += tab + "subu $sp $sp " + frame_size + "\n";
        ret += tab + "sw $ra -4($fp)\n";
        return ret;
    }

    public String epilogue() {
        String ret = tab + "lw $ra -4($fp)\n";
        ret += tab + "lw $fp -8($fp)\n";
        ret += tab + "addu $sp $sp " + frame_size + "\n";
        ret += tab + "jr $ra\n";
        return ret;
    }
}
