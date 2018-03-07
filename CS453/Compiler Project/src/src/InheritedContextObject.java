public class InheritedContextObject extends ContextObject {
    public ContextObject inheritsFrom;

    public InheritedContextObject(ContextObject c, ContextObject inheritsFrom) {
        this.methodName = c.methodName;
        this.className = c.className;
        this.inheritsFrom = inheritsFrom;
    }

    @Override
    public String toString() {
        return super.toString() + " -> [" + inheritsFrom + "]";
    }
}
