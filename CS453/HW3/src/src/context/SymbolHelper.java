package context;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SymbolHelper {

    public ArrayList<Symbol> symt;
    public HashMap<ContextObject, ArrayList<Symbol>> sigt;
    public ArrayList<ContextObject> classes;

    public SymbolHelper(ArrayList<Symbol> symt,
                        HashMap<ContextObject, ArrayList<Symbol>> sigt,
                        ArrayList<ContextObject> objs) {
        this.symt = symt;
        this.sigt = sigt;
        this.classes = objs;
    }

    public void initClass(ContextObject c) {
        // disregard if already initiated
        if (c.classObject.isInit())
            return;
        // depth-first initialization
        boolean inherits = c instanceof InheritedContextObject;
        int wordsFromParents = 0;
        if (inherits) {
            initClass(((InheritedContextObject) c).inheritsFrom);
            wordsFromParents = ((InheritedContextObject) c).inheritsFrom.classObject.numWords();
        }
        // proceed to calculate class var count and function presence
        HashMap<ContextObject, ArrayList<Symbol>> funcs = searchSigt(c.classObject);
        ArrayList<Symbol> syms = searchSymt(c.classObject);
        c.classObject.init(syms.size() + wordsFromParents, !funcs.isEmpty(), inherits);
    }

    public ContextObject searchObjs(ContextObject o) {
        // execute search
        ContextObject ret = null;
        for (ContextObject s : classes) {
            if (s.classObject.equals(o.classObject)) {
                ret = s;
                break;
            }
        }
        if (ret == null)
            return null;
        return ret;
    }

    public HashMap<ContextObject, ArrayList<Symbol>> searchSigt(ClassObject c) {
        HashMap<ContextObject, ArrayList<Symbol>> ret = new HashMap<>();
        for (Map.Entry<ContextObject, ArrayList<Symbol>> curr : sigt.entrySet())
            if (curr.getKey().classObject.equals(c))
                ret.put(curr.getKey(), curr.getValue());
        return ret;
    }

    public ArrayList<Symbol> searchSymt(ClassObject c) {
        ArrayList<Symbol> ret = new ArrayList<>();
        for (Symbol curr : symt)
            // only interested in class level
            if (curr.context.methodName == null && curr.context.classObject.equals(c))
                ret.add(curr);
        return ret;
    }

    public void normalize() {
        // full inheritance tree for each class
        for (ContextObject curr : classes) {
            // continue if it doesn't inherit
            if (!(curr instanceof InheritedContextObject))
                continue;
            // can assume non-null value
            ContextObject newFrom = searchObjs(((InheritedContextObject) curr).inheritsFrom);
            ((InheritedContextObject) curr).inheritsFrom = newFrom;
        }
        // now init the classes
        for (ContextObject curr : classes)
            // init if necessary
            initClass(curr);
    }

    @Override
    public String toString() {
        String ret = "Symbol table ----------------------\n";
        for (Symbol entry : this.symt)
            ret += entry + "\n";
        ret += "Method signature table ------------\n";
        for (Map.Entry<ContextObject, ArrayList<Symbol>> entry : this.sigt.entrySet()) {
            ContextObject key = entry.getKey();
            ArrayList<Symbol> val = entry.getValue();
            ret += key;
            for (Symbol t : val)
                ret += "\n\t" + t;
            ret += "\n";
        }
        ret += "Classes table ---------------------\n";
        for (ContextObject i : this.classes)
            ret += i + "\n";
        return ret;
    }
}
