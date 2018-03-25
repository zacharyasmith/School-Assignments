package context;

import elements.ETemporarySymbol;

public class Symbol {
    public ContextObject context;
    public String symbol;
    public TypeHelper type;
    public ClassObject class_type = null;
    public ETemporarySymbol tmp = null;

    public Symbol(ContextObject c, String symbol, TypeHelper t) {
        this.context = new ContextObject(c);
        this.symbol = symbol;
        if (c.methodName != null)
            tmp = new ETemporarySymbol();
        this.type = t;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Symbol symbol1 = (Symbol) o;

        if (!context.equals(symbol1.context)) return false;
        if (!symbol.equals(symbol1.symbol)) return false;
        return type.equals(symbol1.type);
    }

    @Override
    public int hashCode() {
        int result = context.hashCode();
        result = 31 * result + symbol.hashCode();
        result = 31 * result + type.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return context + " Symbol::" + symbol +
                (tmp != null ? "[" + tmp + "] " : " ") +
                (class_type != null ? class_type : type);
    }
}
