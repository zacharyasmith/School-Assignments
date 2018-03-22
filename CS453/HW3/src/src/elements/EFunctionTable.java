package elements;

import context.ContextObject;
import context.Symbol;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class EFunctionTable implements Element {
    private ArrayList<String> funcs = new ArrayList<>();
    private ContextObject context;
    private String prefix;

    public EFunctionTable(ContextObject c,
                          HashMap<ContextObject, ArrayList<Symbol>> sigt,
                          String prefix) {
        for (Map.Entry<ContextObject, ArrayList<Symbol>> curr : sigt.entrySet())
            funcs.add(curr.getKey().getVaporName());
        context = c;
        this.prefix = prefix;
    }

    public String toVapor(String tab, int depth) {
        String ret = "const " + prefix + context.classObject.className + ":\n";
        for (String f : funcs)
            ret += tab + ":" + f + "\n";
        return ret + "\n";
    }
}
