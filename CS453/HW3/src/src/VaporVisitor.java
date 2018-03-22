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
     *       | AssignmentStatement()
     *       | ArrayAssignmentStatement()
     *       | IfStatement()
     *       | WhileStatement()
     *       | PrintStatement()
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
        Symbol sym = sh.searchSymt(argu.c, n.f0);
        ESymbol assignment;
        if (sym.context.methodName == null) {
            assignment = new EAccessorSymbol(sh.getOffset(argu.c, sym));
        } else {
            assignment = sym.tmp;
        }
        argu.add(new EAssignmentStatement(argu.c, n.f2.accept(this, argu), assignment));
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
        // TODO ArrayAssignmentStatement 
        n.f0.accept(this, argu);
        n.f2.accept(this, argu);
        n.f5.accept(this, argu);
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
        EIf if_container = new EIf(argu.c, conditional);
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
     *       | CompareExpression()
     *       | PlusExpression()
     *       | MinusExpression()
     *       | TimesExpression()
     *       | ArrayLookup()
     *       | ArrayLength()
     *       | MessageSend()
     *       | PrimaryExpression()
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
        // TODO And
        n.f0.accept(this, argu);
        n.f2.accept(this, argu);
        return null;
    }

    /**
     * f0 -> PrimaryExpression()
     * f1 -> "<"
     * f2 -> PrimaryExpression()
     */
    public EExpression visit(CompareExpression n, EContainer argu) {
        // TODO compare
        n.f0.accept(this, argu);
        n.f2.accept(this, argu);
        return null;
    }

    /**
     * f0 -> PrimaryExpression()
     * f1 -> "+"
     * f2 -> PrimaryExpression()
     */
    public EExpression visit(PlusExpression n, EContainer argu) {
        // TODO plus
        n.f0.accept(this, argu);
        n.f2.accept(this, argu);
        return null;
    }

    /**
     * f0 -> PrimaryExpression()
     * f1 -> "-"
     * f2 -> PrimaryExpression()
     */
    public EExpression visit(MinusExpression n, EContainer argu) {
        // TODO minus
        n.f0.accept(this, argu);
        n.f2.accept(this, argu);
        return null;
    }

    /**
     * f0 -> PrimaryExpression()
     * f1 -> "*"
     * f2 -> PrimaryExpression()
     */
    public EExpression visit(TimesExpression n, EContainer argu) {
        // TODO multiply
        n.f0.accept(this, argu);
        n.f2.accept(this, argu);
        return null;
    }

    /**
     * f0 -> PrimaryExpression()
     * f1 -> "["
     * f2 -> PrimaryExpression()
     * f3 -> "]"
     */
    public EExpression visit(ArrayLookup n, EContainer argu) {
        // TODO array lookup
        n.f0.accept(this, argu);
        n.f2.accept(this, argu);
        return null;
    }

    /**
     * f0 -> PrimaryExpression()
     * f1 -> "."
     * f2 -> "length"
     */
    public EExpression visit(ArrayLength n, EContainer argu) {
        // TODO array length
        n.f0.accept(this, argu);
        return null;
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
        // TODO message send
        n.f0.accept(this, argu);
        n.f2.accept(this, argu);
        // pass an expression container
        n.f4.accept(this, argu);
        return null;
    }

    /**
     * f0 -> Expression()
     * f1 -> ( ExpressionRest() )*
     */
    public EExpression visit(ExpressionList n, EContainer argu) {
        // TODO expr list
        n.f0.accept(this, argu);
        n.f1.accept(this, argu);
        return null;
    }

    /**
     * f0 -> ","
     * f1 -> Expression()
     */
    public EExpression visit(ExpressionRest n, EContainer argu) {
        // TODO expression rest
        n.f1.accept(this, argu);
        return null;
    }

    /**
     * f0 -> IntegerLiteral()
     *       | TrueLiteral()
     *       | FalseLiteral()
     *       | Identifier()
     *       | ThisExpression()
     *       | ArrayAllocationExpression()
     *       | AllocationExpression()
     *       | NotExpression()
     *       | BracketExpression()
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
        // TODO uncomment following
        assert false;
        n.f0.accept(this, argu);
        return null;
    }

    /**
     * f0 -> "this"
     */
    public EExpression visit(ThisExpression n, EContainer argu) {
        // TODO this
        n.f0.accept(this, argu);
        return null;
    }

    /**
     * f0 -> "new"
     * f1 -> "int"
     * f2 -> "["
     * f3 -> Expression()
     * f4 -> "]"
     */
    public EExpression visit(ArrayAllocationExpression n, EContainer argu) {
        // TODO array alloc
        n.f3.accept(this, argu);
        return null;
    }

    /**
     * f0 -> "new"
     * f1 -> Identifier()
     * f2 -> "("
     * f3 -> ")"
     */
    public EExpression visit(AllocationExpression n, EContainer argu) {
        // TODO alloc
        n.f1.accept(this, argu);
        return null;
    }

    /**
     * f0 -> "!"
     * f1 -> Expression()
     */
    public EExpression visit(NotExpression n, EContainer argu) {
        // TODO not expression
        n.f1.accept(this, argu);
        return null;
    }

    /**
     * f0 -> "("
     * f1 -> Expression()
     * f2 -> ")"
     */
    public EExpression visit(BracketExpression n, EContainer argu) {
        // TODO bracket
        n.f1.accept(this, argu);
        return null;
    }
}
