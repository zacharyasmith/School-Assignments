import syntaxtree.Identifier;

import java.util.HashMap;

public class TypeCheckHelper {

    public HashMap<String, IdentifierType> symt;
    public boolean passing = true;

    public IdentifierType searchSymt(ContextObject argu, Identifier n) throws TypeCheckException {
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

    public TypeCheckHelper(HashMap<String, IdentifierType> symt) {
        this.symt = symt;
    }

}
