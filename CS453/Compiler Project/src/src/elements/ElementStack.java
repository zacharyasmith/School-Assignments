package elements;
import java.util.ArrayList;

public class ElementStack {
    private ArrayList<Element> stack;
    private int pointer;
    private boolean debug;

    public ElementStack(boolean debug) {
        this.debug = debug;
        stack = new ArrayList<>();
        pointer = 0;
    }

    private void print(String f) {
        System.out.print("\t\t\t\t\t\t\t" + f + ": ");
        for (Element s : stack) {
            System.out.print(s.getClass().getSimpleName() + ", ");
        }
        System.out.println();
    }

    public int getPointer() {
        return pointer;
    }

    public Element peek(int back) {
        if (pointer - back == 0)
            return null;
        return stack.get(pointer - back - 1);
    }

    public void push(Element s) {
        stack.add(s);
        pointer++;
        if (debug) print("push");
    }

    public Element pop() {
        if (pointer == 0)
            throw new IndexOutOfBoundsException();
        Element ret = stack.get(--pointer);
        stack.remove(pointer);
        if (debug) print("pop");
        return ret;
    }
}
