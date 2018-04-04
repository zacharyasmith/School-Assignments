package J2V.context;

public class ClassObject {
    public String className;
    private int numVars;
    private boolean hasFunctions;
    public ClassObject extends_ = null;
    private boolean init = false;

    public ClassObject(String name) {
        this.className = name;
    }

    public void init(int numVars, boolean hasFunctions, ClassObject extends_) {
        init = true;
        this.numVars = numVars;
        this.hasFunctions = hasFunctions;
        this.extends_ = extends_;
    }

    public boolean hasFunctions() {
        return hasFunctions;
    }

    public int funcOffset() {
        return hasFunctions ? 1 : 0;
    }

    public boolean isInit() {
        return init;
    }

    public int numWords() {
        int from_parents = 0;
        if (extends_ != null) {
            from_parents += extends_.numWords();
        }
        return from_parents + numWordsSelf();
    }

    public int numWordsSelf() {
        return numVars + (hasFunctions ? 1 : 0);
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
