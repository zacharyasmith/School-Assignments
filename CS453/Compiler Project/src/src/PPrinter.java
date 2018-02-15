import syntaxtree.*;
import visitor.*;

public class PPrinter<R> extends GJDepthFirst<R, String> {
    private static final String sep = "| ";

    /**
     * f0 -> MainClass()
     * f1 -> ( TypeDeclaration() )*
     * f2 -> <EOF>
     */
    public R visit(Goal n, String argu) {
        System.out.println(argu + n.getClass().getSimpleName());
        return super.visit(n, sep + argu);
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
    @Override
    public R visit(MainClass n, String argu) {
        System.out.println(argu + n.getClass().getSimpleName());
        return super.visit(n, sep + argu);
    }

    /**
     * f0 -> ClassDeclaration()
     *       | ClassExtendsDeclaration()
     */
    @Override
    public R visit(TypeDeclaration n, String argu) {
        System.out.println(argu + n.getClass().getSimpleName());
        return super.visit(n, sep + argu);
    }

    /**
     * f0 -> "class"
     * f1 -> Identifier()
     * f2 -> "{"
     * f3 -> ( VarDeclaration() )*
     * f4 -> ( MethodDeclaration() )*
     * f5 -> "}"
     */
    @Override
    public R visit(ClassDeclaration n, String argu) {
        System.out.println(argu + n.getClass().getSimpleName());
        return super.visit(n, sep + argu);
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
    @Override
    public R visit(ClassExtendsDeclaration n, String argu) {
        System.out.println(argu + n.getClass().getSimpleName());
        return super.visit(n, sep + argu);
    }

    /**
     * f0 -> Type()
     * f1 -> Identifier()
     * f2 -> ";"
     */
    @Override
    public R visit(VarDeclaration n, String argu) {
        System.out.println(argu + n.getClass().getSimpleName());
        return super.visit(n, sep + argu);
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
    @Override
    public R visit(MethodDeclaration n, String argu) {
        System.out.println(argu + n.getClass().getSimpleName());
        return super.visit(n, sep + argu);
    }

    /**
     * f0 -> FormalParameter()
     * f1 -> ( FormalParameterRest() )*
     */
    @Override
    public R visit(FormalParameterList n, String argu) {
        System.out.println(argu + n.getClass().getSimpleName());
        return super.visit(n, sep + argu);
    }

    /**
     * f0 -> Type()
     * f1 -> Identifier()
     */
    @Override
    public R visit(FormalParameter n, String argu) {
        System.out.println(argu + n.getClass().getSimpleName());
        return super.visit(n, sep + argu);
    }

    /**
     * f0 -> ","
     * f1 -> FormalParameter()
     */
    @Override
    public R visit(FormalParameterRest n, String argu) {
        System.out.println(argu + n.getClass().getSimpleName());
        return super.visit(n, sep + argu);
    }

    /**
     * f0 -> ArrayType()
     *       | BooleanType()
     *       | IntegerType()
     *       | Identifier()
     */
    @Override
    public R visit(Type n, String argu) {
        System.out.println(argu + n.getClass().getSimpleName());
        return super.visit(n, sep + argu);
    }

    /**
     * f0 -> "int"
     * f1 -> "["
     * f2 -> "]"
     */
    @Override
    public R visit(ArrayType n, String argu) {
        System.out.println(argu + n.getClass().getSimpleName());
        return super.visit(n, sep + argu);
    }

    /**
     * f0 -> "boolean"
     */
    @Override
    public R visit(BooleanType n, String argu) {
        System.out.println(argu + n.getClass().getSimpleName());
        return super.visit(n, sep + argu);
    }

    /**
     * f0 -> "int"
     */
    @Override
    public R visit(IntegerType n, String argu) {
        System.out.println(argu + n.getClass().getSimpleName());
        return super.visit(n, sep + argu);
    }

    /**
     * f0 -> Block()
     *       | AssignmentStatement()
     *       | ArrayAssignmentStatement()
     *       | IfStatement()
     *       | WhileStatement()
     *       | PrintStatement()
     */
    @Override
    public R visit(Statement n, String argu) {
        System.out.println(argu + n.getClass().getSimpleName());
        return super.visit(n, sep + argu);
    }

    /**
     * f0 -> "{"
     * f1 -> ( Statement() )*
     * f2 -> "}"
     */
    @Override
    public R visit(Block n, String argu) {
        System.out.println(argu + n.getClass().getSimpleName());
        return super.visit(n, sep + argu);
    }

    /**
     * f0 -> Identifier()
     * f1 -> "="
     * f2 -> Expression()
     * f3 -> ";"
     */
    @Override
    public R visit(AssignmentStatement n, String argu) {
        System.out.println(argu + n.getClass().getSimpleName());
        return super.visit(n, sep + argu);
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
    @Override
    public R visit(ArrayAssignmentStatement n, String argu) {
        System.out.println(argu + n.getClass().getSimpleName());
        return super.visit(n, sep + argu);
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
    @Override
    public R visit(IfStatement n, String argu) {
        System.out.println(argu + n.getClass().getSimpleName());
        return super.visit(n, sep + argu);
    }

    /**
     * f0 -> "while"
     * f1 -> "("
     * f2 -> Expression()
     * f3 -> ")"
     * f4 -> Statement()
     */
    @Override
    public R visit(WhileStatement n, String argu) {
        System.out.println(argu + n.getClass().getSimpleName());
        return super.visit(n, sep + argu);
    }

    /**
     * f0 -> "System.out.println"
     * f1 -> "("
     * f2 -> Expression()
     * f3 -> ")"
     * f4 -> ";"
     */
    @Override
    public R visit(PrintStatement n, String argu) {
        System.out.println(argu + n.getClass().getSimpleName());
        return super.visit(n, sep + argu);
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
    @Override
    public R visit(Expression n, String argu) {
        System.out.println(argu + n.getClass().getSimpleName());
        return super.visit(n, sep + argu);
    }

    /**
     * f0 -> PrimaryExpression()
     * f1 -> "&&"
     * f2 -> PrimaryExpression()
     */
    @Override
    public R visit(AndExpression n, String argu) {
        System.out.println(argu + n.getClass().getSimpleName());
        return super.visit(n, sep + argu);
    }

    /**
     * f0 -> PrimaryExpression()
     * f1 -> "<"
     * f2 -> PrimaryExpression()
     */
    @Override
    public R visit(CompareExpression n, String argu) {
        System.out.println(argu + n.getClass().getSimpleName());
        return super.visit(n, sep + argu);
    }

    /**
     * f0 -> PrimaryExpression()
     * f1 -> "+"
     * f2 -> PrimaryExpression()
     */
    @Override
    public R visit(PlusExpression n, String argu) {
        System.out.println(argu + n.getClass().getSimpleName());
        return super.visit(n, sep + argu);
    }

    /**
     * f0 -> PrimaryExpression()
     * f1 -> "-"
     * f2 -> PrimaryExpression()
     */
    @Override
    public R visit(MinusExpression n, String argu) {
        System.out.println(argu + n.getClass().getSimpleName());
        return super.visit(n, sep + argu);
    }

    /**
     * f0 -> PrimaryExpression()
     * f1 -> "*"
     * f2 -> PrimaryExpression()
     */
    @Override
    public R visit(TimesExpression n, String argu) {
        System.out.println(argu + n.getClass().getSimpleName());
        return super.visit(n, sep + argu);
    }

    /**
     * f0 -> PrimaryExpression()
     * f1 -> "["
     * f2 -> PrimaryExpression()
     * f3 -> "]"
     */
    @Override
    public R visit(ArrayLookup n, String argu) {
        System.out.println(argu + n.getClass().getSimpleName());
        return super.visit(n, sep + argu);
    }

    /**
     * f0 -> PrimaryExpression()
     * f1 -> "."
     * f2 -> "length"
     */
    @Override
    public R visit(ArrayLength n, String argu) {
        System.out.println(argu + n.getClass().getSimpleName());
        return super.visit(n, sep + argu);
    }

    /**
     * f0 -> PrimaryExpression()
     * f1 -> "."
     * f2 -> Identifier()
     * f3 -> "("
     * f4 -> ( ExpressionList() )?
     * f5 -> ")"
     */
    @Override
    public R visit(MessageSend n, String argu) {
        System.out.println(argu + n.getClass().getSimpleName());
        return super.visit(n, sep + argu);
    }

    /**
     * f0 -> Expression()
     * f1 -> ( ExpressionRest() )*
     */
    @Override
    public R visit(ExpressionList n, String argu) {
        System.out.println(argu + n.getClass().getSimpleName());
        return super.visit(n, sep + argu);
    }

    /**
     * f0 -> ","
     * f1 -> Expression()
     */
    @Override
    public R visit(ExpressionRest n, String argu) {
        System.out.println(argu + n.getClass().getSimpleName());
        return super.visit(n, sep + argu);
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
    @Override
    public R visit(PrimaryExpression n, String argu) {
        System.out.println(argu + n.getClass().getSimpleName());
        return super.visit(n, sep + argu);
    }

    /**
     * f0 -> <INTEGER_LITERAL>
     */
    @Override
    public R visit(IntegerLiteral n, String argu) {
        System.out.println(argu + n.getClass().getSimpleName());
        return super.visit(n, sep + argu);
    }

    /**
     * f0 -> "true"
     */
    @Override
    public R visit(TrueLiteral n, String argu) {
        System.out.println(argu + n.getClass().getSimpleName());
        return super.visit(n, sep + argu);
    }

    /**
     * f0 -> "false"
     */
    @Override
    public R visit(FalseLiteral n, String argu) {
        System.out.println(argu + n.getClass().getSimpleName());
        return super.visit(n, sep + argu);
    }

    /**
     * f0 -> <IDENTIFIER>
     */
    @Override
    public R visit(Identifier n, String argu) {
        System.out.println(argu + n.getClass().getSimpleName());
        return super.visit(n, sep + argu);
    }

    /**
     * f0 -> "this"
     */
    @Override
    public R visit(ThisExpression n, String argu) {
        System.out.println(argu + n.getClass().getSimpleName());
        return super.visit(n, sep + argu);
    }

    /**
     * f0 -> "new"
     * f1 -> "int"
     * f2 -> "["
     * f3 -> Expression()
     * f4 -> "]"
     */
    @Override
    public R visit(ArrayAllocationExpression n, String argu) {
        System.out.println(argu + n.getClass().getSimpleName());
        return super.visit(n, sep + argu);
    }

    /**
     * f0 -> "new"
     * f1 -> Identifier()
     * f2 -> "("
     * f3 -> ")"
     */
    @Override
    public R visit(AllocationExpression n, String argu) {
        System.out.println(argu + n.getClass().getSimpleName());
        return super.visit(n, sep + argu);
    }

    /**
     * f0 -> "!"
     * f1 -> Expression()
     */
    @Override
    public R visit(NotExpression n, String argu) {
        System.out.println(argu + n.getClass().getSimpleName());
        return super.visit(n, sep + argu);
    }

    /**
     * f0 -> "("
     * f1 -> Expression()
     * f2 -> ")"
     */
    @Override
    public R visit(BracketExpression n, String argu) {
        System.out.println(argu + n.getClass().getSimpleName());
        return super.visit(n, sep + argu);
    }

}
