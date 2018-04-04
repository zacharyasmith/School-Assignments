package J2V.elements;

public class EArrayAllocHelper implements Element {
    @Override
    public String toVapor(String tab, int depth) {
        String ret = "func AllocArray(size)\n";
        ret += tab + "bytes = MulS(size 4)\n";
        ret += tab + "bytes = Add(bytes 4)\n";
        ret += tab + "v = HeapAllocZ(bytes)\n";
        ret += tab + "[v] = size\n";
        ret += tab + "ret v\n\n";
        return ret;
    }
}
