import context.*;
import elements.*;
import syntaxtree.*;
import visitor.GJDepthFirst;

import java.util.ArrayList;
import java.util.HashMap;

public class VaporVisitor<R> extends GJDepthFirst<R, Element> {
    SymbolHelper sh;
    boolean debug;
    private EList<EFunctionTable> const_list = new EList<>();
    private EMain main_method = new EMain();
    private EList<EMethod> methods = new EList<>();
    private Counters GLOBALS = new Counters();

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
        if (debug) System.out.println("Translating to vapor—————————————————");
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
    public R visit(Goal n, Element argu) {
        // TODO new stack for main class
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
    public R visit(MainClass n, Element argu) {
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
    public R visit(ClassDeclaration n, Element argu) {
        n.f4.accept(this, argu);
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
    public R visit(ClassExtendsDeclaration n, Element argu) {
        n.f6.accept(this, argu);
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
    public R visit(MethodDeclaration n, Element argu) {
        n.f8.accept(this, argu);
        // return
        n.f10.accept(this, argu);
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
    public R visit(Statement n, Element argu) {
        assert argu instanceof EFunction;
        n.f0.accept(this, argu);
        return null;
    }

    /**
     * f0 -> "{"
     * f1 -> ( StatementElement() )*
     * f2 -> "}"
     */
    public R visit(Block n, Element argu) {
        n.f0.accept(this, argu);
        n.f1.accept(this, argu);
        n.f2.accept(this, argu);
        return null;
    }

    /**
     * f0 -> Identifier()
     * f1 -> "="
     * f2 -> Expression()
     * f3 -> ";"
     */
    public R visit(AssignmentStatement n, Element argu) {
        n.f0.accept(this, argu);
        n.f1.accept(this, argu);
        n.f2.accept(this, argu);
        n.f3.accept(this, argu);
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
    public R visit(ArrayAssignmentStatement n, Element argu) {
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
     * f4 -> StatementElement()
     * f5 -> "else"
     * f6 -> StatementElement()
     */
    public R visit(IfStatement n, Element argu) {
        n.f2.accept(this, argu);
        n.f4.accept(this, argu);
        n.f6.accept(this, argu);
        return null;
    }

    /**
     * f0 -> "while"
     * f1 -> "("
     * f2 -> Expression()
     * f3 -> ")"
     * f4 -> StatementElement()
     */
    public R visit(WhileStatement n, Element argu) {
        n.f2.accept(this, argu);
        n.f4.accept(this, argu);
        return null;
    }

    /**
     * f0 -> "System.out.println"
     * f1 -> "("
     * f2 -> Expression()
     * f3 -> ")"
     * f4 -> ";"
     */
    public R visit(PrintStatement n, Element argu) {
        n.f2.accept(this, argu);
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
    public R visit(Expression n, Element argu) {
        n.f0.accept(this, argu);
        return null;
    }

    /**
     * f0 -> PrimaryExpression()
     * f1 -> "&&"
     * f2 -> PrimaryExpression()
     */
    public R visit(AndExpression n, Element argu) {
        n.f0.accept(this, argu);
        n.f2.accept(this, argu);
        return null;
    }

    /**
     * f0 -> PrimaryExpression()
     * f1 -> "<"
     * f2 -> PrimaryExpression()
     */
    public R visit(CompareExpression n, Element argu) {
        n.f0.accept(this, argu);
        n.f2.accept(this, argu);
        return null;
    }

    /**
     * f0 -> PrimaryExpression()
     * f1 -> "+"
     * f2 -> PrimaryExpression()
     */
    public R visit(PlusExpression n, Element argu) {
        n.f0.accept(this, argu);
        n.f2.accept(this, argu);
        return null;
    }

    /**
     * f0 -> PrimaryExpression()
     * f1 -> "-"
     * f2 -> PrimaryExpression()
     */
    public R visit(MinusExpression n, Element argu) {
        n.f0.accept(this, argu);
        n.f2.accept(this, argu);
        return null;
    }

    /**
     * f0 -> PrimaryExpression()
     * f1 -> "*"
     * f2 -> PrimaryExpression()
     */
    public R visit(TimesExpression n, Element argu) {
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
    public R visit(ArrayLookup n, Element argu) {
        n.f0.accept(this, argu);
        n.f2.accept(this, argu);
        return null;
    }

    /**
     * f0 -> PrimaryExpression()
     * f1 -> "."
     * f2 -> "length"
     */
    public R visit(ArrayLength n, Element argu) {
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
    public R visit(MessageSend n, Element argu) {
        n.f0.accept(this, argu);
        n.f2.accept(this, argu);
        n.f4.accept(this, argu);
        return null;
    }

    /**
     * f0 -> Expression()
     * f1 -> ( ExpressionRest() )*
     */
    public R visit(ExpressionList n, Element argu) {
        n.f0.accept(this, argu);
        n.f1.accept(this, argu);
        return null;
    }

    /**
     * f0 -> ","
     * f1 -> Expression()
     */
    public R visit(ExpressionRest n, Element argu) {
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
    public R visit(PrimaryExpression n, Element argu) {
        n.f0.accept(this, argu);
        return null;
    }

    /**
     * f0 -> <INTEGER_LITERAL>
     */
    public R visit(IntegerLiteral n, Element argu) {
        n.f0.accept(this, argu);
        return null;
    }

    /**
     * f0 -> "true"
     */
    public R visit(TrueLiteral n, Element argu) {
        n.f0.accept(this, argu);
        return null;
    }

    /**
     * f0 -> "false"
     */
    public R visit(FalseLiteral n, Element argu) {
        n.f0.accept(this, argu);
        return null;
    }

    /**
     * f0 -> <IDENTIFIER>
     */
    public R visit(Identifier n, Element argu) {
        n.f0.accept(this, argu);
        return null;
    }

    /**
     * f0 -> "this"
     */
    public R visit(ThisExpression n, Element argu) {
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
    public R visit(ArrayAllocationExpression n, Element argu) {
        n.f3.accept(this, argu);
        return null;
    }

    /**
     * f0 -> "new"
     * f1 -> Identifier()
     * f2 -> "("
     * f3 -> ")"
     */
    public R visit(AllocationExpression n, Element argu) {
        n.f1.accept(this, argu);
        return null;
    }

    /**
     * f0 -> "!"
     * f1 -> Expression()
     */
    public R visit(NotExpression n, Element argu) {
        n.f1.accept(this, argu);
        return null;
    }

    /**
     * f0 -> "("
     * f1 -> Expression()
     * f2 -> ")"
     */
    public R visit(BracketExpression n, Element argu) {
        n.f1.accept(this, argu);
        return null;
    }
}
