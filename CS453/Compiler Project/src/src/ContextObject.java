public class ContextObject {
    public String className;
    public String methodName;
    public IdentifierType requiredType = null;
    public IdentifierType requiredReturnType = null;

    public ContextObject(String className, String methodName) {
        this.className = className;
        this.methodName = methodName;
    }
}
