package elements;

public class EMessageSend extends EExpression {
    private EContainer<EExpression> args;
    private EExpression obj;
    private EExpression method;
    public EMessageSend(EContainer<EExpression> args, EExpression method, EExpression obj) {
        super(new ETemporarySymbol());
        this.args = args;
        this.obj = obj;
        // TODO
        this.method = method;
    }

    @Override
    public String toVapor(String tab, int depth) {
        String ret = args.toVapor(tab, depth);
        ret += obj.toVapor(tab, depth);
        ret += method.toVapor(tab, depth);
        String args_list = obj.getAccessor().toString();
        for (EExpression exp : args.elements) {
            ret += exp.getAccessor().toVapor(tab, depth);
            args_list += " " + exp.getAccessor();
        }
        ret += Element.repeatTab(tab, depth) + getAccessor() + " = call " + method +
                "(" + args_list + ")";
        return ret;
    }
}
