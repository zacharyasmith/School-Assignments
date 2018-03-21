package elements;

import java.util.ArrayList;

public class ElementList<T> implements Element {
    private ArrayList<T> list;

    public ElementList() {
        list = new ArrayList<>();
    }

    public ArrayList<T> getList() {
        return list;
    }

    public void add(T s) {
        list.add(s);
    }

    @Override
    public String toVapor(String tab, int depth) {
        String ret = "";
        for (T e : list)
            ret += ((Element) e).toVapor(tab, depth);
        return ret;
    }
}
