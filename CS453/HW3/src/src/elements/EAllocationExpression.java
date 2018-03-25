package elements;
import context.*;

public class EAllocationExpression extends EExpression {
    public ClassObject c;
    private String ft_prefix;

    public EAllocationExpression(ClassObject c, String ft_prefix) {
        super(new ETemporarySymbol());
        this.c = c;
        this.ft_prefix = ft_prefix;
    }

    @Override
    public String toVapor(String tab, int depth) {
        String tab_ = Element.repeatTab(tab, depth);
        String ret = getAccessor().toVapor(tab, depth);
        ret += tab_ + getAccessor() +
                " = HeapAllocZ(" + 4 * c.numWords() + ")\n";
        // assign the function tables
        // [t.42] = :functable_A
        ret += tab_ + "[" + getAccessor() + "] = :" + ft_prefix + c.className + "\n";
        ClassObject parent = c.extends_;
        int offset_counter = c.numWordsSelf();
        while (parent != null) {
            ret += tab_ + "[" + getAccessor() + " + " + (4 * offset_counter) +
                    "] = :" + ft_prefix + parent.className + "\n";
            offset_counter += parent.numWordsSelf();
            parent = parent.extends_;
        }
        return ret;
    }
}
