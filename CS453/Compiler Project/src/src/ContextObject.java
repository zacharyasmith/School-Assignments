public class ContextObject {
    public String className;
    public String methodName;

    public ContextObject() {
        this.className = "";
        this.methodName = null;
    }

    public ContextObject(ContextObject c) {
        this.methodName = c.methodName;
        this.className = c.className;
    }

    public ContextObject(String className, String methodName) {
        this.className = className;
        this.methodName = methodName;
    }

    public ContextObject(String className) {
        this.className = className;
        this.methodName = null;
    }

    @Override
    public String toString() {
        return "Class::" + className + (methodName != null ? " Method::" + methodName : "");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ContextObject that = (ContextObject) o;

        if (!className.equals(that.className)) return false;
        return methodName != null ? methodName.equals(that.methodName) : that.methodName == null;
    }

    @Override
    public int hashCode() {
        int result = className.hashCode();
        result = 31 * result + (methodName != null ? methodName.hashCode() : 0);
        return result;
    }
}
