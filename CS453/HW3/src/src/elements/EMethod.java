package elements;

import context.ContextObject;
import context.Symbol;
import java.util.ArrayList;

public class EMethod extends EFunction {
    private ArrayList<Symbol> list;
    public EExpression return_expr;

    public EMethod(ContextObject c, ArrayList<Symbol> list) {
        super(c);
        this.list = list;
    }

    @Override
    public String toVapor(String tab, int depth) {
//        assert return_expr != null;
        // declare
        String params = "";
        for (Symbol t : list)
            if (t.symbol != null)
                params += " " + t.tmp;
        String ret = "func " + c.getVaporName() + "(this" + params + ")\n";
        // convert all elements
        ret += super.toVapor(tab, 1);
        ret += return_expr.toVapor(tab, 1);
        ret += return_expr.getAccessor().toVapor(tab, 1);
        ret += tab + "ret " + return_expr.getAccessor() + "\n";
        return ret + "\n";
    }
}

