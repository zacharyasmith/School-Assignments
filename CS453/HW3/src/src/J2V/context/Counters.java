package J2V.context;

public class Counters {
    public static int SYMBOL = 0;
    public static int ERROR = 0;
    public static int IF = 0;
    public static int WHILE = 0;

    public static int nextSym() {
        return SYMBOL++;
    }

    public static int nextError() {
        return ERROR++;
    }

    public static int nextIf() {
        return IF++;
    }

    public static int nextWhile() {
        return WHILE++;
    }
}
