package elements;

import java.util.ArrayList;

public class ContainerElement<T extends Element> implements Element {
    private ArrayList<T> elements = new ArrayList<>();

    public void add(T e) {
        elements.add(e);
    }

    public String toVapor(String tab, int depth) {
        String ret = "";
        for (T e : elements)
            ret += Element.repeatTab(tab, depth) +
                    e.toVapor(tab, depth + 1) +
                    "\n";
        return ret;
    }
}
