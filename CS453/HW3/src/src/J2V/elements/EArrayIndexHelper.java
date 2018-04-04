package J2V.elements;

public class EArrayIndexHelper implements Element {
    @Override
    public String toVapor(String tab, int depth) {
        String ret = "func ArrayIndexHelper(index)\n";
        ret += tab + "loc = Add(index 1)\n";
        ret += tab + "addr = MulS(loc 4)\n";
        ret += tab + "ret addr\n\n";
        return ret;
    }
}
