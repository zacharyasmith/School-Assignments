package elements;

import context.ContextObject;

public class EMain extends EFunction {
    public EMain(ContextObject c) {
        super(c);
    }

    @Override
    public String toVapor(String tab, int depth) {
        // declare
        String ret = "func Main()\n";
        // convert all elements
        ret += super.toVapor(tab, 1);
        ret += tab + "goto :exit\n";
        ret += tab + "mem_error:\n";
        ret += Element.repeatTab(tab, 2) + "Error(\"Mem exhausted\")\n";
        ret += Element.repeatTab(tab, 2) + "goto :exit\n";
        ret += tab + "exit:\n";
        ret += Element.repeatTab(tab, 2) + "ret\n";
        return ret + "\n";
    }
}
