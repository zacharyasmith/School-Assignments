import sun.jvm.hotspot.debugger.cdbg.Sym;
import syntaxtree.Identifier;
import syntaxtree.Type;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SymbolHelper {

    public HashMap<Symbol, TypeHelper> symt;
    public HashMap<ContextObject, ArrayList<TypeHelper>> sigt;
    public ArrayList<ContextObject> objs;

    public SymbolHelper(HashMap<Symbol, TypeHelper> symt,
                        HashMap<ContextObject, ArrayList<TypeHelper>> sigt,
                        ArrayList<ContextObject> objs) {
        this.symt = symt;
        this.sigt = sigt;
        this.objs = objs;
    }

    public ContextObject searchObjs(ContextObject o) {
        for (ContextObject s : objs) {
            if (s.className.equals(o.className))
                return s;
        }
        return null;
    }

    public void normalize() {
        for (ContextObject curr : objs) {
            // continue if it doesn't inherit
            if (!(curr instanceof InheritedContextObject))
                continue;
            // can assume non-null value
            ContextObject newFrom = searchObjs(((InheritedContextObject) curr).inheritsFrom);
            ((InheritedContextObject) curr).inheritsFrom = newFrom;
        }
    }

    public void normalizeRec(ContextObject curr) {
        // does curr inherit? base case.
        if (!(curr instanceof InheritedContextObject))
            return;
        // It does inherit
        ContextObject from = ((InheritedContextObject) curr).inheritsFrom;
        // inherit further? recurse as DFS
        normalizeRec(from);
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
        for (ContextObject i : this.objs) {
            ret += i + "\n";
        }
        return ret;
    }
}
