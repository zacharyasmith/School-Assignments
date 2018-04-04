package J2V.elements;

import J2V.context.ContextObject;

public class EMain extends EFunction {
    public EMain(ContextObject c) {
        super(c);
    }

    @Override
    public String toVapor(String tab, int depth) {
        // declare
        String ret = "func Main()\n";
        // convert all elements
        ret += super.toVapor(tab, 1);
        ret += tab + "ret\n";
        return ret + "\n";
    }
}
