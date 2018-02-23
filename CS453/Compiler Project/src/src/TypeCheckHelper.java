import syntaxtree.Identifier;

import java.util.HashMap;
import java.util.Map;

public class TypeCheckHelper {

    public HashMap<String, TypeHelper> symt;
    public HashMap<String, TypeHelper> sigt;
    public boolean passing = true;

    public TypeHelper searchSymt(ContextObject argu, Identifier n) throws TypeCheckException {
        String clss = argu.className + "::" + n.f0.tokenImage;
        String method = argu.className + "::" + argu.methodName + "::" +
                n.f0.tokenImage;
        if (symt.containsKey(method))
            return symt.get(method);
        else if (symt.containsKey(clss))
            return symt.get(clss);
        else {
            throw new TypeCheckException("Neither identifiers found in symbol table:" +
                    "\n\t" + clss +
                    "\n\t" + method);
        }
    }

    public TypeHelper searchSigt(ContextObject argu) throws TypeCheckException {
        String method = argu.className + "::" + argu.methodName;
        if (sigt.containsKey(method))
            return sigt.get(method);
        else
            throw new TypeCheckException("Method signature not found:" +
                    "\n\t" + method);
    }

    public TypeCheckHelper(HashMap<String, TypeHelper> symt, HashMap<String, TypeHelper> sigt) {
        this.symt = symt;
        this.sigt = sigt;
    }

    @Override
    public String toString() {
        String ret = "Symbol table ---------------------------\n";
        for (Map.Entry<String, TypeHelper> entry : this.symt.entrySet()) {
            String key = entry.getKey();
            TypeHelper val = entry.getValue();
            ret += key + " : " + val.type + "\n";
        }
        ret += "Method signature table -----------------\n";
        for (Map.Entry<String, TypeHelper> entry : this.sigt.entrySet()) {
            String key = entry.getKey();
            TypeHelper val = entry.getValue();
            ret += key + " : " + val.type + "\n";
        }
        return ret;
    }
}
