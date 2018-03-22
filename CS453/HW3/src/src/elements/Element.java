package elements;

public interface Element {
    String toVapor(String tab, int depth);

    static String repeatTab(String tab, int depth) {
        return new String(new char[depth]).replace("\0", tab);
    }
}
