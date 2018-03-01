import syntaxtree.Identifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class TypeCheckHelper {

    protected HashMap<String, TypeHelper> symt;
    protected HashMap<String, ArrayList<TypeHelper>> sigt;
    protected ArrayList<String> objs;
    protected HashMap<String, String> inherits;
    public boolean passing = true;

    public TypeCheckHelper(HashMap<String, TypeHelper> symt,
                           HashMap<String, ArrayList<TypeHelper>> sigt,
                           ArrayList<String> objs,
                           HashMap<String, String> inherits) {
        this.symt = symt;
        this.sigt = sigt;
        this.objs = objs;
        this.inherits = inherits;
    }

    public void normalize() {
        boolean changed = true;
        while(changed)
            for (String clss : objs)
                changed = normalizeRec(clss) || changed;
    }

    public boolean normalizeRec(String curr) {
        // TODO finish
        // trivial case
        if (!inherits.containsKey(curr))
            return false;

        // track changes
        boolean changed = false;
        boolean computed = false;

        // for substring search
        String from = inherits.get(curr);
        int len = from.length() + 2;
        String search = from + "::";
        for (Map.Entry<String, TypeHelper> e : symt.entrySet()) {
            String sym = e.getKey();
            // Ensure length for substring
            if (sym.length() <= len)
                continue;
            String newSym = search + sym.substring(len, sym.length());
            // Ensure
            if (sym.substring(0, len).equals(search)) {
                if (symt.containsKey(newSym)) {
                    computed = true;
                    break;
                }
                symt.put(newSym, new TypeHelper(symt.get(newSym)));
            }
        }

        return changed;
    }

    public TypeHelper searchSymt(ContextObject argu, Identifier n) throws TypeCheckException {
        String clss = argu.className + "::" + n.f0.tokenImage;
        String method = argu.className + "::" + argu.methodName + "::" +
                n.f0.tokenImage;
        if (symt.containsKey(method))
            return symt.get(method);
        else if (symt.containsKey(clss))
            return symt.get(clss);
        else {
            throw new TypeCheckException("Neither identifiers found in symbol sigt:" +
                    "\n\t" + clss +
                    "\n\t" + method);
        }
    }

    public ArrayList<TypeHelper> searchSigt(ContextObject c) throws TypeCheckException {
        return searchSigt(c.className + "::" + c.methodName);
    }

    public ArrayList<TypeHelper> searchSigt(String className, String methodName) throws TypeCheckException {
        return searchSigt(className + "::" + methodName);
    }

    private ArrayList<TypeHelper> searchSigt(String method) throws TypeCheckException {
        if (sigt.containsKey(method))
            return sigt.get(method);
        else
            throw new TypeCheckException("Method signature not found:" +
                    "\n\t" + method);
    }

    public TypeHelper searchObjs(Identifier c) throws TypeCheckException {
        return searchObjs(c.f0.tokenImage);
    }

    public TypeHelper searchObjs(String c) throws TypeCheckException {
        for (String i : objs)
            if(i.equals(c))
                return new TypeHelper(i);
        throw new TypeCheckException("Class `" + c + "` not declared.");
    }

    @Override
    public String toString() {
        String ret = "Symbol table ----------------------\n";
        for (Map.Entry<String, TypeHelper> entry : this.symt.entrySet()) {
            String key = entry.getKey();
            TypeHelper val = entry.getValue();
            ret += key + " : " + val + "\n";
        }
        ret += "Method signature table ------------\n";
        for (Map.Entry<String, ArrayList<TypeHelper>> entry : this.sigt.entrySet()) {
            String key = entry.getKey();
            ArrayList<TypeHelper> val = entry.getValue();
            ret += key + " : ";
            for (TypeHelper t : val)
                ret += t + " ";
            ret += "\n";
        }
        ret += "Classes table ---------------------\n";
        for (String i : this.objs) {
            ret += i + "\n";
        }
        ret += "Inheritance table -----------------\n";
        for (Map.Entry<String, String> entry : this.inherits.entrySet()) {
            ret += entry.getKey() + " extends " + entry.getValue() + "\n";
        }
        return ret;
    }
}
