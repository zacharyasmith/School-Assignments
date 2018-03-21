package context;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SymbolHelper {

    public HashMap<Symbol, TypeHelper> symt;
    public HashMap<ContextObject, ArrayList<TypeHelper>> sigt;
    public ArrayList<ContextObject> classes;

    public SymbolHelper(HashMap<Symbol, TypeHelper> symt,
                        HashMap<ContextObject, ArrayList<TypeHelper>> sigt,
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
        HashMap<ContextObject, ArrayList<TypeHelper>> funcs = searchSigt(c.classObject);
        HashMap<Symbol, TypeHelper> syms = searchSymt(c.classObject);
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

    public HashMap<ContextObject, ArrayList<TypeHelper>> searchSigt(ClassObject c) {
        HashMap<ContextObject, ArrayList<TypeHelper>> ret = new HashMap<>();
        for (Map.Entry<ContextObject, ArrayList<TypeHelper>> curr : sigt.entrySet())
            if (curr.getKey().classObject.equals(c))
                ret.put(curr.getKey(), curr.getValue());
        return ret;
    }

    public HashMap<Symbol, TypeHelper> searchSymt(ClassObject c) {
        HashMap<Symbol, TypeHelper> ret = new HashMap<>();
        for (Map.Entry<Symbol, TypeHelper> curr : symt.entrySet())
            // only interested in class level
            if (curr.getKey().context.methodName == null && curr.getKey().context.classObject.equals(c))
                ret.put(curr.getKey(), curr.getValue());
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
        for (Map.Entry<Symbol, TypeHelper> entry : this.symt.entrySet()) {
            Symbol key = entry.getKey();
            TypeHelper val = entry.getValue();
            ret += key + " : " + val + "\n";
        }
        ret += "Method signature table ------------\n";
        for (Map.Entry<ContextObject, ArrayList<TypeHelper>> entry : this.sigt.entrySet()) {
            ContextObject key = entry.getKey();
            ArrayList<TypeHelper> val = entry.getValue();
            ret += key + " : ";
            for (TypeHelper t : val)
                ret += t + " ";
            ret += "\n";
        }
        ret += "Classes table ---------------------\n";
        for (ContextObject i : this.classes) {
            ret += i + "\n";
        }
        return ret;
    }
}
