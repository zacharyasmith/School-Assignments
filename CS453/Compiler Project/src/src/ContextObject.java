public class ContextObject {
    public String className;
    public String methodName;
    public TypeHelper expressionType = null;

    public ContextObject(String className, String methodName) {
        this.className = className;
        this.methodName = methodName;
    }

    @Override
    public String toString() {
        return "Context: Class::" + className + " Method::" + methodName;
    }
}
