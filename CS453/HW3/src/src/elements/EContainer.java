package elements;

import context.ContextObject;

import java.util.ArrayList;

public class EContainer<T extends Element> implements Element {
    public ContextObject c;
    public ArrayList<T> elements = new ArrayList<>();

    public EContainer(ContextObject c){
        this.c = c;
    }

    public void add(T e) {
        elements.add(e);
    }

    public String toVapor(String tab, int depth) {
        String ret = "";
        for (T e : elements)
            ret += e.toVapor(tab, depth);
        return ret;
    }
}
