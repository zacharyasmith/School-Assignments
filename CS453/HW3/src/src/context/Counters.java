package context;

public class Counters {
    public static int SYMBOL = 0;
    public static int ERROR = 0;
    public static int IF = 0;
    public static int WHILE = 0;

    public int nextSym() {
        return SYMBOL++;
    }

    public int nextError() {
        return ERROR++;
    }

    public int nextIf() {
        return IF++;
    }

    public int nextWhile() {
        return WHILE++;
    }
}
