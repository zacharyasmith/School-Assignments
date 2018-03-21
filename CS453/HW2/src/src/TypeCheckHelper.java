import syntaxtree.Identifier;
import syntaxtree.Type;

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
        HashMap<String, String> cpy = new HashMap<>(inherits);
        for (String clss : objs)
            normalizeRec(clss, new ArrayList<>());
        inherits = cpy;
    }

    /**
     * @param curr
     * @param children
     */
    public void normalizeRec(String curr, ArrayList<String> children) {
        // add self to children
        if (!children.contains(curr)) children.add(curr);
        
        // does curr inherit? base case.
        if (!inherits.containsKey(curr))
            return;

        // It does inherit
        String from = inherits.get(curr);
        // Is there a parent inheriting from a child?
        if (children.contains(from))
            throw new TypeCheckException("Parent class `" + from +
                    "` inheriting from child class `"+curr+"`");
        // does inherited class exist?
        if (!objs.contains(from))
            throw new TypeCheckException("Class `" + curr +
                    "` inherits from non-existent class `" + from + "`.");
        // inherit further? recurse... DFS
        normalizeRec(from, children);
        // remove self from inheritance list
        inherits.remove(curr);

        // for substring search
        String search = from + "::";
        int len = search.length();

        // Method table inheritance
        for (Map.Entry<String, ArrayList<TypeHelper>> e : (new HashMap<>(sigt)).entrySet()) {
            String method = e.getKey();
            // Ensure length for substring
            if (method.length() <= len)
                continue;
            String newMethod = curr + "::" + method.substring(len, method.length());
            // Ensure
            if (method.substring(0, len).equals(search)) {
                // Current method is one of `from`'s
                // Overriden? Already handled?
                if (sigt.containsKey(newMethod)) {
                    // Method signature the same?
                    // If not, illegal override
                    if (!sigt.get(newMethod).equals(sigt.get(method)))
                        throw new TypeCheckException("Method override `" + newMethod + "` with `" +
                                method + "` " + "have different signatures.");
                } else {
                    // Add to `curr`'s method list
                    sigt.put(newMethod, sigt.get(method));
                    // along with method symbols
                    for (Map.Entry<String, TypeHelper> s : searchSymt(method + "::",
                            newMethod + "::", true).entrySet())
                        symt.put(s.getKey(), s.getValue());
                }
            }
        }

        // Class variable inheritance
        for (Map.Entry<String, TypeHelper> e : searchSymt(search, curr + "::",
                false).entrySet()) {
            String sym = e.getKey(); // symbol with replacement
            // Ensure
            if (symt.containsKey(sym)) {
                // already implemented or overridden
                continue;
            }
            symt.put(sym, new TypeHelper(e.getValue()));
        }
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

    /**
     * @param pattern : Include the separator `::` at end of String
     * @param method  : Specify whether the search is at method level.
     *                If false, will search only class level.
     * @return Symbols matching search criteria
     */
    public HashMap<String, TypeHelper> searchSymt(String pattern, String replace, boolean method) {
        HashMap<String, TypeHelper> ret = new HashMap<>();
        int len = pattern.length();
        // along with method symbols
        for (Map.Entry<String, TypeHelper> s : symt.entrySet()) {
            String sym = s.getKey();
            // Trivial case
            if (sym.length() <= len)
                continue;
            if (sym.substring(0, len).equals(pattern)) {
                if (!method) {
                    // ensure no more `::`s
                    if (sym.substring(len, sym.length()).contains("::"))
                        continue;
                }
                // match
                if (replace != null)
                    ret.put(replace + sym.substring(len, sym.length()),
                            s.getValue());
                else
                    ret.put(sym, s.getValue());
            }
        }
        return ret;
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
            if (i.equals(c))
                return new TypeHelper(i);
        throw new TypeCheckException("Class `" + c + "` not declared.");
    }

    public boolean inheritsFrom(String child, String parent) {
        String curr = child;
        do {
            if (curr == parent)
                return true;
            if (inherits.containsKey(curr))
                curr = inherits.get(curr);
            else curr = null;
        } while (curr != null);
        return false;
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
