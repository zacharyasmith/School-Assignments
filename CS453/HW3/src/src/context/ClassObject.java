package context;

public class ClassObject {
    public String className;
    private int numVars;
    private boolean hasFunctions;
    private boolean inherits;
    private boolean init = false;


    public ClassObject(String name) {
        this.className = name;
    }

    public void init(int numVars, boolean hasFunctions, boolean inherits) {
        init = true;
        this.numVars = numVars;
        this.hasFunctions = hasFunctions;
    }

    public boolean isInit() {
        return init;
    }

    public int numWords() {
        return numVars + (hasFunctions ? 1 : 0) + (inherits ? 1 : 0);
    }

    @Override
    public String toString() {
        return className + (init ? "(" + numWords() * 4 + " B)" : "");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ClassObject that = (ClassObject) o;

        return className.equals(that.className);
    }

    @Override
    public int hashCode() {
        return className.hashCode();
    }
}
