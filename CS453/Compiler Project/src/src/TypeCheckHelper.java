import syntaxtree.Identifier;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class TypeCheckHelper {

    public HashMap<String, TypeHelper> symt;
    public HashMap<String, ArrayList<TypeHelper>> sigt;
    public ArrayList<Identifier> objs;
    public boolean passing = true;

    public TypeCheckHelper(HashMap<String, TypeHelper> symt,
                           HashMap<String, ArrayList<TypeHelper>> sigt,
                           ArrayList<Identifier> objs) {
        this.symt = symt;
        this.sigt = sigt;
        this.objs = objs;
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
        for (Identifier i : objs)
            if(i.f0.tokenImage.equals(c))
                return new TypeHelper(i);
        throw new TypeCheckException("Class `" + c + "` not declared.");
    }

    @Override
    public String toString() {
        String ret = "Symbol table ----------------------\n";
        for (Map.Entry<String, TypeHelper> entry : this.symt.entrySet()) {
            String key = entry.getKey();
            TypeHelper val = entry.getValue();
            ret += key + " : " + val.type + "\n";
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
        for (Identifier i : this.objs) {
            ret += i.f0 + "\n";
        }
        return ret;
    }
}
