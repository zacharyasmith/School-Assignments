package context;

public class ContextObject {
    public ClassObject classObject;
    public String methodName;

    public ContextObject() {
        this.classObject = new ClassObject("");
        this.methodName = null;
    }

    public ContextObject(ContextObject c) {
        this.methodName = c.methodName;
        this.classObject = c.classObject;
    }

    public ContextObject(String className, String methodName) {
        this.classObject = new ClassObject(className);
        this.methodName = methodName;
    }

    public ContextObject(String className) {
        this.classObject = new ClassObject(className);
        this.methodName = null;
    }

    @Override
    public String toString() {
        return "Class::" + classObject + (methodName != null ? " Method::" + methodName : "");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ContextObject that = (ContextObject) o;

        if (!classObject.equals(that.classObject)) return false;
        return methodName != null ? methodName.equals(that.methodName) : that.methodName == null;
    }

    @Override
    public int hashCode() {
        int result = classObject.hashCode();
        result = 31 * result + (methodName != null ? methodName.hashCode() : 0);
        return result;
    }
}
