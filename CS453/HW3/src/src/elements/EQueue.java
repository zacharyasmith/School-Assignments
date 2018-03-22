package elements;

import java.util.ArrayList;

public class EQueue<T> {
    private ArrayList<T> queue;
    private int pointer;
    private boolean debug;

    public EQueue(boolean debug) {
        this.debug = debug;
        queue = new ArrayList<>();
        pointer = 0;
    }

    public ArrayList<T> getQueue() {
        return queue;
    }

    private void print(String f) {
        System.out.print("\t\t\t\t\t\t\t" + f + ": ");
        for (T s : queue) {
            System.out.print(s.getClass().getSimpleName() + ", ");
        }
        System.out.println();
    }

    public int getPointer() {
        return pointer;
    }

    public T peek(int back) {
        if (pointer == queue.size())
            return null;
        return queue.get(pointer);
    }

    public void enqueue(T s) {
        queue.add(s);
        if (debug) print("enqueue");
    }

    public T dequeue() {
        if (pointer == queue.size())
            throw new IndexOutOfBoundsException();
        T ret = queue.get(pointer);
        if (debug) print("dequeue");
        pointer++;
        return ret;
    }
}
