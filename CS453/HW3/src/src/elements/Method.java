package elements;

import context.ContextObject;
import context.Symbol;
import java.util.ArrayList;

public class Method extends FunctionElement {
    private ContextObject c;
    private ArrayList<Symbol> list;

    public Method(ContextObject c, ArrayList<Symbol> list) {
        this.c = c;
        this.list = list;
    }

    @Override
    public String toVapor(String tab, int depth) {
        // declare
        String params = "";
        for (Symbol t : list) {

        }
        String ret = "func " + c.getVaporName() + "(this " + params + ")\n";
        // convert all elements
        ret += super.toVapor(tab, 1);
        ret += tab + "mem_error:\n";
        ret += Element.repeatTab(tab, 2) + "Error(“Mem exhausted”)\n";
        ret += Element.repeatTab(tab, 2) + "goto :exit\n";
        ret += tab + "exit:\n";
        ret += Element.repeatTab(tab, 2) + "ret\n";
        return ret + "\n";
    }
}

