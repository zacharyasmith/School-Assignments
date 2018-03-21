package elements;

import java.util.ArrayList;

public class ContainerElement implements Element {
    private ArrayList<Element> elements = new ArrayList<>();

    public void add(Element e) {
        elements.add(e);
    }

    public String toVapor(String tab, int depth) {
        String ret = "";
        String tabStr = "";
        for (int i = 0; i < depth; i++)
            tabStr += tab;
        for (Element e : elements)
            ret += tabStr + e.toVapor(tab, depth + 1) + "\n";
        return ret;
    }
}
