package context;

public class Symbol {
    public ContextObject context;
    public String symbol;

    public Symbol(String clss, String method, String symbol) {
        this.context = new ContextObject(clss, method);
        this.symbol = symbol;
    }

    public Symbol(ContextObject c, String symbol) {
        this.context = new ContextObject(c);
        this.symbol = symbol;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Symbol symbol1 = (Symbol) o;

        if (!context.equals(symbol1.context)) return false;
        return symbol.equals(symbol1.symbol);
    }

    @Override
    public int hashCode() {
        int result = context.hashCode();
        result = 31 * result + symbol.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return context + " Symbol::" + symbol;
    }
}
