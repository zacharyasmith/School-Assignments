package elements;

import java.util.ArrayList;

public class EList<T extends Element> implements Element {
    private ArrayList<T> list;

    public EList() {
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
            ret += e.toVapor(tab, depth);
        return ret;
    }
}
