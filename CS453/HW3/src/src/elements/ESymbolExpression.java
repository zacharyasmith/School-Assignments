package elements;

import context.ClassObject;

public class ESymbolExpression extends EExpression {
    public ClassObject c;
    public ESymbolExpression(ESymbol s, ClassObject c) {
        super(s);
        this.c = c;
    }
}
