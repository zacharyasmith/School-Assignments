import java.util.ArrayList;

public class Stack {
    private ArrayList<String> stack;
    private int pointer;
    private boolean debug;

    public Stack(boolean debug) {
        this.debug = debug;
        stack = new ArrayList<>();
        pointer = 0;
    }

    private void print(String f) {
        System.out.print("\t\t\t\t\t\t\t" + f + ": ");
        for (String s : stack) {
            System.out.print(s + ", ");
        }
        System.out.println();
    }

    public int getPointer() {
        return pointer;
    }

    public String peek(int back) {
        if (pointer - back == 0)
            return "";
        return stack.get(pointer - back - 1);
    }

    public void push(String s) {
        stack.add(s);
        pointer++;
        if (debug) print("push");
    }

    public String pop() {
        if (pointer == 0)
            throw new IndexOutOfBoundsException();
        String ret = stack.get(--pointer);
        stack.remove(pointer);
        if (debug) print("pop");
        return ret;
    }
}
