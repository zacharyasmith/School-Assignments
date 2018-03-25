package elements;

public class EMessageSend extends EExpression {
    public EContainer<EExpression> args;
    private EExpression obj;
    private EExpression method;
    private EExpression primary;
    public EMessageSend(EExpression primary,
                        EContainer<EExpression> args,
                        EExpression method,
                        EExpression obj) {
        super(new ETemporarySymbol());
        this.args = args;
        this.obj = obj;
        this.primary = primary;
        this.method = method;
    }

    @Override
    public String toVapor(String tab, int depth) {
        String ret = primary.toVapor(tab, depth);
        ret += obj.toVapor(tab, depth);
        ret += method.toVapor(tab, depth);
        String args_list = primary.getAccessor().toString();
        for (EExpression exp : args.elements) {
            ret += exp.getAccessor().toVapor(tab, depth);
            args_list += " " + exp.getAccessor();
        }
        ret += args.toVapor(tab, depth);
        ret += Element.repeatTab(tab, depth) + getAccessor() + " = call " + method.getAccessor() +
                "(" + args_list + ")\n";
        return ret;
    }
}
