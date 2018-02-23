import syntaxtree.NodeToken;

public class TypeCheckException extends Exception {
    public TypeCheckException(String message) {
        super("TypeCheckException:: " + message);
    }

    public static String debugString(TypeCheckException e, ContextObject c, NodeToken n) {
        return e.getMessage() + "\n\t" +
                c + "\n\t" +
                "Row: " + n.beginLine + " Col: " + n.beginColumn;
    }
}
