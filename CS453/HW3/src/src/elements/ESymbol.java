package elements;

import context.Counters;

public class ESymbol implements Element {
    private int id;

    public ESymbol() {
        id = Counters.nextSym();
    }

    public ESymbol(int id) {
        this.id = id;
    }

    @Override
    public String toVapor(String tab, int depth) {
        return "t." + id;
    }
}
