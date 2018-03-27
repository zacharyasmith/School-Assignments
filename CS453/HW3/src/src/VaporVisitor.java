import context.*;
import elements.*;
import syntaxtree.*;
import visitor.GJDepthFirst;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class VaporVisitor extends GJDepthFirst<EExpression, EContainer> {
    SymbolHelper sh;
    boolean debug;
    private EContainer<EFunctionTable> const_list = new EContainer<>(null);
    private EMain main_method;
    private EContainer<EMethod> methods = new EContainer<>(null);

    // configurables
    public static final String TAB = "  ";
    public static final String CONST_PREFIX = "ft_";

    public VaporVisitor(SymbolHelper sh, boolean debug) {
        this.sh = sh;
        this.debug = debug;
        for (ContextObject c : sh.classes) {
            HashMap<ContextObject, ArrayList<Symbol>> list = sh.searchSigt(c.classObject);
            if (!list.isEmpty()) {
                EFunctionTable ft = new EFunctionTable(c, list, CONST_PREFIX);
                const_list.add(ft);
            }
        }
    }

    public String toVapor() {
        String ret = "";
        // Const table
        ret += const_list.toVapor(TAB, 0);
        // Funcs
        ret += methods.toVapor(TAB, 0);
        // helpers
        ret += new EArrayAllocHelper().toVapor(TAB, 0);
        ret += new EArrayIndexHelper().toVapor(TAB, 0);
        // Main method
        ret += main_method.toVapor(TAB, 0);
        return ret;
    }

    /**
     * f0 -> MainClass()
     * f1 -> ( TypeDeclaration() )*
     * f2 -> <EOF>
     */
    public EExpression visit(Goal n, EContainer argu) {
        n.f0.accept(this, argu);
        n.f1.accept(this, argu);
        return null;
    }

    /**
     * f0 -> "class"
     * f1 -> Identifier()
     * f2 -> "{"
     * f3 -> "public"
     * f4 -> "static"
     * f5 -> "void"
     * f6 -> "main"
     * f7 -> "("
     * f8 -> "String"
     * f9 -> "["
     * f10 -> "]"
     * f11 -> Identifier()
     * f12 -> ")"
     * f13 -> "{"
     * f14 -> ( VarDeclaration() )*
     * f15 -> ( Statement() )*
     * f16 -> "}"
     * f17 -> "}"
     */
    public EExpression visit(MainClass n, EContainer argu) {
        ClassObject c = sh.searchObjs(n.f1.f0.tokenImage).classObject;
        Map.Entry<ContextObject, ArrayList<Symbol>> search =
                sh.searchSigt(c, "main");
        main_method = new EMain(search.getKey());
        n.f15.accept(this, main_method);
        return null;
    }

    /**
     * f0 -> "class"
     * f1 -> Identifier()
     * f2 -> "{"
     * f3 -> ( VarDeclaration() )*
     * f4 -> ( MethodDeclaration() )*
     * f5 -> "}"
     */
    public EExpression visit(ClassDeclaration n, EContainer argu) {
        // pass down the class
        n.f4.accept(this, new EClass(sh.searchObjs(n.f1.f0.tokenImage)));
        return null;
    }

    /**
     * f0 -> "class"
     * f1 -> Identifier()
     * f2 -> "extends"
     * f3 -> Identifier()
     * f4 -> "{"
     * f5 -> ( VarDeclaration() )*
     * f6 -> ( MethodDeclaration() )*
     * f7 -> "}"
     */
    public EExpression visit(ClassExtendsDeclaration n, EContainer argu) {
        n.f6.accept(this, new EClass(sh.searchObjs(n.f1.f0.tokenImage)));
        return null;
    }

    /**
     * f0 -> "public"
     * f1 -> Type()
     * f2 -> Identifier()
     * f3 -> "("
     * f4 -> ( FormalParameterList() )?
     * f5 -> ")"
     * f6 -> "{"
     * f7 -> ( VarDeclaration() )*
     * f8 -> ( Statement() )*
     * f9 -> "return"
     * f10 -> Expression()
     * f11 -> ";"
     * f12 -> "}"
     */
    public EExpression visit(MethodDeclaration n, EContainer argu) {
        assert argu instanceof EClass;
        EClass class_container = (EClass) argu;
        Map.Entry<ContextObject, ArrayList<Symbol>> sig = sh.searchSigt(
                class_container.c.classObject, n.f2.f0.tokenImage);
        EMethod meth = new EMethod(sig.getKey(), sig.getValue());
        n.f8.accept(this, meth);
        meth.return_expr = n.f10.accept(this, meth);
        methods.add(meth);
        return null;
    }

    /**
     * f0 -> Block()
     * | AssignmentStatement()
     * | ArrayAssignmentStatement()
     * | IfStatement()
     * | WhileStatement()
     * | PrintStatement()
     */
    public EExpression visit(Statement n, EContainer argu) {
        n.f0.accept(this, argu);
        return null;
    }

    /**
     * f0 -> Identifier()
     * f1 -> "="
     * f2 -> Expression()
     * f3 -> ";"
     */
    public EExpression visit(AssignmentStatement n, EContainer argu) {
        EExpression assigned = n.f2.accept(this, argu);
        // tell SymbolHelper what I assigned this as
        EExpression symbol = n.f0.accept(this, argu);
        if (assigned instanceof EAllocationExpression)
            if (symbol instanceof ESymbolExpression)
                ((ESymbolExpression) symbol).c = ((EAllocationExpression) assigned).c;
            else // symbol is an EAccessorExpression AND is member variable assignment
                sh.searchSymt(argu.c, n.f0).class_type = ((EAllocationExpression) assigned).c;
        if (symbol instanceof EAccessorExpression) {
            // recompute symbol to be a [this + offset] = ...
//            argu.add(new EAssignmentStatement(argu.c, assigned));
        }
        argu.add(symbol);
        argu.add(new EAssignmentStatement(argu.c, assigned, symbol.getAccessor()));
        return null;
    }

    /**
     * f0 -> Identifier()
     * f1 -> "["
     * f2 -> Expression()
     * f3 -> "]"
     * f4 -> "="
     * f5 -> Expression()
     * f6 -> ";"
     */
    public EExpression visit(ArrayAssignmentStatement n, EContainer argu) {
        EExpression sym = n.f0.accept(this, argu);
        argu.add(sym);
        argu.add(new EArrayAssignmentStatement(n.f2.accept(this, argu),
                n.f5.accept(this, argu),
                sym.getAccessor()));
        return null;
    }

    /**
     * f0 -> "if"
     * f1 -> "("
     * f2 -> Expression()
     * f3 -> ")"
     * f4 -> Statement()
     * f5 -> "else"
     * f6 -> Statement()
     */
    public EExpression visit(IfStatement n, EContainer argu) {
        EExpression conditional = n.f2.accept(this, argu);
        EIf if_container = new EIf(conditional, argu.c);
        n.f4.accept(this, if_container.true_statements);
        n.f6.accept(this, if_container.false_statements);
        argu.add(if_container);
        return null;
    }

    /**
     * f0 -> "while"
     * f1 -> "("
     * f2 -> Expression()
     * f3 -> ")"
     * f4 -> Statement()
     */
    public EExpression visit(WhileStatement n, EContainer argu) {
        EExpression conditional = n.f2.accept(this, argu);
        EWhile while_container = new EWhile(argu.c, conditional);
        n.f4.accept(this, while_container.statements);
        argu.add(while_container);
        return null;
    }

    /**
     * f0 -> "System.out.println"
     * f1 -> "("
     * f2 -> Expression()
     * f3 -> ")"
     * f4 -> ";"
     */
    public EExpression visit(PrintStatement n, EContainer argu) {
        argu.add(new EPrintStatement(n.f2.accept(this, argu)));
        return null;
    }

    /**
     * f0 -> AndExpression()
     * | CompareExpression()
     * | PlusExpression()
     * | MinusExpression()
     * | TimesExpression()
     * | ArrayLookup()
     * | ArrayLength()
     * | MessageSend()
     * | PrimaryExpression()
     */
    public EExpression visit(Expression n, EContainer argu) {
        return n.f0.accept(this, argu);
    }

    /**
     * f0 -> PrimaryExpression()
     * f1 -> "&&"
     * f2 -> PrimaryExpression()
     */
    public EExpression visit(AndExpression n, EContainer argu) {
        return new EAndExpression(n.f0.accept(this, argu), n.f2.accept(this, argu));
    }

    /**
     * f0 -> PrimaryExpression()
     * f1 -> "<"
     * f2 -> PrimaryExpression()
     */
    public EExpression visit(CompareExpression n, EContainer argu) {
        return new ECompareExpression(n.f0.accept(this, argu), n.f2.accept(this, argu));
    }

    /**
     * f0 -> PrimaryExpression()
     * f1 -> "+"
     * f2 -> PrimaryExpression()
     */
    public EExpression visit(PlusExpression n, EContainer argu) {
        return new EPlusExpression(n.f0.accept(this, argu), n.f2.accept(this, argu));
    }

    /**
     * f0 -> PrimaryExpression()
     * f1 -> "-"
     * f2 -> PrimaryExpression()
     */
    public EExpression visit(MinusExpression n, EContainer argu) {
        return new EMinusExpression(n.f0.accept(this, argu), n.f2.accept(this, argu));
    }

    /**
     * f0 -> PrimaryExpression()
     * f1 -> "*"
     * f2 -> PrimaryExpression()
     */
    public EExpression visit(TimesExpression n, EContainer argu) {
        return new ETimesExpression(n.f0.accept(this, argu), n.f2.accept(this, argu));
    }

    /**
     * f0 -> PrimaryExpression()
     * f1 -> "["
     * f2 -> PrimaryExpression()
     * f3 -> "]"
     */
    public EExpression visit(ArrayLookup n, EContainer argu) {
        EExpression obj_expr = n.f0.accept(this, argu);
        EExpression offset_expr = n.f2.accept(this, argu);
        return new EArrayLookupExpression(obj_expr, offset_expr);
    }

    /**
     * f0 -> PrimaryExpression()
     * f1 -> "."
     * f2 -> "length"
     */
    public EExpression visit(ArrayLength n, EContainer argu) {
        EExpression obj_expr = n.f0.accept(this, argu);
        return new EArrayLengthExpression(obj_expr);
    }

    /**
     * f0 -> PrimaryExpression()
     * f1 -> "."
     * f2 -> Identifier()
     * f3 -> "("
     * f4 -> ( ExpressionList() )?
     * f5 -> ")"
     */
    public EExpression visit(MessageSend n, EContainer argu) {
        // Allocation | this | symbol | EMessageSend
        EExpression obj_expr = n.f0.accept(this, argu);
        EExpression obj = null;
        EExpression method = null;
        if (obj_expr instanceof EAllocationExpression) {
            /**
             * EALLOCATION
             * t.42 = heapAllocZ(4)
             * if0 t.42 goto :error
             * [t.42] = :functable_A
             * OBJ ACCESSOR
             * t.43 = [t.42]
             * METHOD ACCESSOR
             * t.44 = [t.43]
             * call t.44(t.42)
             */
            ClassObject obj_class = ((EAllocationExpression) obj_expr).c;
            // [class, method]
            int[] offsets = sh.methodToOffset(obj_class, n.f2.f0.tokenImage);
            // OBJ ACCESSOR
            obj = new EAccessorExpression(obj_expr.getAccessor(), offsets[0]);
            // METHOD ACCESSOR
            method = new EAccessorExpression(obj.getAccessor(), offsets[1]);
        } else if (obj_expr instanceof EThisExpression) {
            /**
             * ETHISEXPRESSION
             * OBJ ACCESSOR (determining func table)
             * t.43 = [this + {offset}]
             * METHOD ACCESSOR (determining func in table)
             * t.44 = [t.43]
             * call t.44(t.42)
             */
            // use context to determine where I am
            // call respective function table
            // [class, method]
            int[] offsets = sh.methodToOffset(argu.c.classObject, n.f2.f0.tokenImage);
            // OBJ ACCESSOR
            obj = new EAccessorExpression(obj_expr.getAccessor(), offsets[0]);
            // METHOD ACCESSOR
            method = new EAccessorExpression(obj.getAccessor(), offsets[1]);
        } else if (obj_expr instanceof ESymbolExpression) { // symbol expression
            /**
             * ESYMBOL EXPR
             * t.1 = [{symbol} + {offset}]
             * OBJ ACCESSOR
             * t.43 = [t.1 + {offset}]
             * METHOD ACCESSOR
             * t.44 = [t.43]
             * call t.44(t.42)
             */
            // symbol needs to be accessed
            ClassObject obj_class = ((ESymbolExpression) obj_expr).c;
            // [class, method]
            int[] offsets = sh.methodToOffset(obj_class, n.f2.f0.tokenImage);
            // OBJ ACCESSOR
            obj = new EAccessorExpression(obj_expr.getAccessor(), offsets[0]);
            // METHOD ACCESSOR
            method = new EAccessorExpression(obj.getAccessor(), offsets[1]);
        } else if (obj_expr instanceof EMessageSend) {
            // symbol needs to be accessed
            ClassObject obj_class = ((EMessageSend) obj_expr).args.c.classObject;
            // [class, method]
            int[] offsets = sh.methodToOffset(obj_class, n.f2.f0.tokenImage);
            // OBJ ACCESSOR
            obj = new EAccessorExpression(obj_expr.getAccessor(), offsets[0]);
            // METHOD ACCESSOR
            method = new EAccessorExpression(obj.getAccessor(), offsets[1]);
        } else if (obj_expr instanceof EAccessorExpression) {
            // symbol needs to be accessed
            ClassObject obj_class = ((EAccessorExpression) obj_expr).c;
            // [class, method]
            int[] offsets = sh.methodToOffset(obj_class, n.f2.f0.tokenImage);
            // OBJ ACCESSOR
            obj = new EAccessorExpression(obj_expr.getAccessor(), offsets[0]);
            // METHOD ACCESSOR
            method = new EAccessorExpression(obj.getAccessor(), offsets[1]);
        } else {
            throw new RuntimeException("Unexpected behavior");
        }
        // pass an expression container
        EContainer<EExpression> args = new EContainer<>(argu.c);
        n.f4.accept(this, args);
        // get method and object
        return new EMessageSend(obj_expr, args, method, obj);
    }

    /**
     * f0 -> Expression()
     * f1 -> ( ExpressionRest() )*
     */
    public EExpression visit(ExpressionList n, EContainer argu) {
        argu.add(n.f0.accept(this, argu));
        n.f1.accept(this, argu);
        return null;
    }

    /**
     * f0 -> ","
     * f1 -> Expression()
     */
    public EExpression visit(ExpressionRest n, EContainer argu) {
        argu.add(n.f1.accept(this, argu));
        return null;
    }

    /**
     * f0 -> IntegerLiteral()
     * | TrueLiteral()
     * | FalseLiteral()
     * | Identifier()
     * | ThisExpression()
     * | ArrayAllocationExpression()
     * | AllocationExpression()
     * | NotExpression()
     * | BracketExpression()
     */
    public EExpression visit(PrimaryExpression n, EContainer argu) {
        return n.f0.accept(this, argu);
    }

    /**
     * f0 -> <INTEGER_LITERAL>
     */
    public EExpression visit(IntegerLiteral n, EContainer argu) {
        return new EExpression(new EPrimitive(Integer.parseInt(n.f0.tokenImage)));
    }

    /**
     * f0 -> "true"
     */
    public EExpression visit(TrueLiteral n, EContainer argu) {
        return new EExpression(new EPrimitive(1));
    }

    /**
     * f0 -> "false"
     */
    public EExpression visit(FalseLiteral n, EContainer argu) {
        return new EExpression(new EPrimitive(0));
    }

    /**
     * f0 -> <IDENTIFIER>
     */
    public EExpression visit(Identifier n, EContainer argu) {
        Symbol s = sh.searchSymt(argu.c, n);
        assert s.class_type != null;
        if (s.context.methodName == null) {
            EAccessorExpression ret = new EAccessorExpression(new EThisSymbol(),
                    sh.varToOffset(argu.c.classObject, n.f0.tokenImage));
            ret.c = s.class_type;
            return ret;
        }
        return new ESymbolExpression(sh.identifierToSymbol(argu.c, n), s.class_type);
    }

    /**
     * f0 -> "this"
     */
    public EExpression visit(ThisExpression n, EContainer argu) {
        return new EThisExpression();
    }

    /**
     * f0 -> "new"
     * f1 -> "int"
     * f2 -> "["
     * f3 -> Expression()
     * f4 -> "]"
     */
    public EExpression visit(ArrayAllocationExpression n, EContainer argu) {
        return new EArrayAllocationExpression(n.f3.accept(this, argu));
    }

    /**
     * f0 -> "new"
     * f1 -> Identifier()
     * f2 -> "("
     * f3 -> ")"
     */
    public EExpression visit(AllocationExpression n, EContainer argu) {
        ContextObject search = sh.searchObjs(n.f1.f0.tokenImage);
        return new EAllocationExpression(search.classObject, CONST_PREFIX);
    }

    /**
     * f0 -> "!"
     * f1 -> Expression()
     */
    public EExpression visit(NotExpression n, EContainer argu) {
        return new ENotExpression(n.f1.accept(this, argu));
    }

    /**
     * f0 -> "("
     * f1 -> Expression()
     * f2 -> ")"
     */
    public EExpression visit(BracketExpression n, EContainer argu) {
        return n.f1.accept(this, argu);
    }
}
