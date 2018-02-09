import java.util.ArrayList;

public class Stack {
    private ArrayList<String> stack;
    private int pointer;

    public Stack() {
        stack = new ArrayList<>();
        pointer = 0;
    }

    public void push(String s) {
        stack.add(s);
        pointer++;
    }

    public String pop() {
        if (pointer == 0)
            throw new IndexOutOfBoundsException();
        return stack.get(--pointer);
    }
}
