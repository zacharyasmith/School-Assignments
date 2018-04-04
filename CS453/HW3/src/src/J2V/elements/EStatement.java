package J2V.elements;

import J2V.context.ContextObject;

public class EStatement implements Element {
    ContextObject c;
    public EStatement(ContextObject c) {
        this.c = c;
    }

    @Override
    public String toVapor(String tab, int depth) {
        return null;
    }
}
