import syntaxtree.*;
import visitor.GJDepthFirst;

import java.util.HashMap;

public class TypeCheckVisitor<R> extends GJDepthFirst<R, ContextObject> {

    boolean debug;
    public TypeCheckHelper tch;
    TypeCheckVisitor(boolean debug, TypeCheckHelper tch) {
        this.debug = debug;
        this.tch = tch;
    }

    /**
     * f0 -> MainClass()
     * f1 -> ( TypeDeclaration() )*
     * f2 -> <EOF>
     */
    public R visit(Goal n, ContextObject argu) {
        return super.visit(n, argu);
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
    public R visit(MainClass n, ContextObject argu) {
        ContextObject co = new ContextObject(n.f1.f0.tokenImage, "main");
        return super.visit(n, co);
    }

    /**
     * f0 -> ClassDeclaration()
     *       | ClassExtendsDeclaration()
     */
    public R visit(TypeDeclaration n, ContextObject argu) {
        return super.visit(n, argu);
    }

    /**
     * f0 -> "class"
     * f1 -> Identifier()
     * f2 -> "{"
     * f3 -> ( VarDeclaration() )*
     * f4 -> ( MethodDeclaration() )*
     * f5 -> "}"
     */
    public R visit(ClassDeclaration n, ContextObject argu) {
        ContextObject co = new ContextObject(n.f1.f0.tokenImage, null);
        return super.visit(n, co);
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
    public R visit(ClassExtendsDeclaration n, ContextObject argu) {
        ContextObject co = new ContextObject(n.f1.f0.tokenImage, null);
        return super.visit(n, co);
    }

    /**
     * f0 -> Type()
     * f1 -> Identifier()
     * f2 -> ";"
     */
    public R visit(VarDeclaration n, ContextObject argu) {
        return super.visit(n, argu);
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
    public R visit(MethodDeclaration n, ContextObject argu) {
        argu.methodName = n.f2.f0.tokenImage;
        argu.requiredReturnType = new IdentifierType(n.f1);
        return super.visit(n, argu);
    }

    /**
     * f0 -> FormalParameter()
     * f1 -> ( FormalParameterRest() )*
     */
    public R visit(FormalParameterList n, ContextObject argu) {
        return super.visit(n, argu);
    }

    /**
     * f0 -> Type()
     * f1 -> Identifier()
     */
    public R visit(FormalParameter n, ContextObject argu) {
        return super.visit(n, argu);
    }

    /**
     * f0 -> ","
     * f1 -> FormalParameter()
     */
    public R visit(FormalParameterRest n, ContextObject argu) {
        return super.visit(n, argu);
    }

    /**
     * f0 -> ArrayType()
     *       | BooleanType()
     *       | IntegerType()
     *       | Identifier()
     */
    public R visit(Type n, ContextObject argu) {
        return super.visit(n, argu);
    }

    /**
     * f0 -> "int"
     * f1 -> "["
     * f2 -> "]"
     */
    public R visit(ArrayType n, ContextObject argu) {
        return super.visit(n, argu);
    }

    /**
     * f0 -> "boolean"
     */
    public R visit(BooleanType n, ContextObject argu) {
        return super.visit(n, argu);
    }

    /**
     * f0 -> "int"
     */
    public R visit(IntegerType n, ContextObject argu) {
        return super.visit(n, argu);
    }

    /**
     * f0 -> Block()
     *       | AssignmentStatement()
     *       | ArrayAssignmentStatement()
     *       | IfStatement()
     *       | WhileStatement()
     *       | PrintStatement()
     */
    public R visit(Statement n, ContextObject argu) {
        return super.visit(n, argu);
    }

    /**
     * f0 -> "{"
     * f1 -> ( Statement() )*
     * f2 -> "}"
     */
    public R visit(Block n, ContextObject argu) {
        return super.visit(n, argu);
    }

    /**
     * f0 -> Identifier()
     * f1 -> "="
     * f2 -> Expression()
     * f3 -> ";"
     */
    public R visit(AssignmentStatement n, ContextObject argu) {
        try {
            argu.requiredType = tch.searchSymt(argu, n.f0);
        } catch (TypeCheckException e) {
            System.out.println(e.getMessage());
            tch.passing = false;
        }
        return super.visit(n, argu);
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
    public R visit(ArrayAssignmentStatement n, ContextObject argu) {
        return super.visit(n, argu);
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
    public R visit(IfStatement n, ContextObject argu) {
        return super.visit(n, argu);
    }

    /**
     * f0 -> "while"
     * f1 -> "("
     * f2 -> Expression()
     * f3 -> ")"
     * f4 -> Statement()
     */
    public R visit(WhileStatement n, ContextObject argu) {
        return super.visit(n, argu);
    }

    /**
     * f0 -> "System.out.println"
     * f1 -> "("
     * f2 -> Expression()
     * f3 -> ")"
     * f4 -> ";"
     */
    public R visit(PrintStatement n, ContextObject argu) {
        return super.visit(n, argu);
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
    public R visit(Expression n, ContextObject argu) {
        return super.visit(n, argu);
    }

    /**
     * f0 -> PrimaryExpression()
     * f1 -> "&&"
     * f2 -> PrimaryExpression()
     */
    public R visit(AndExpression n, ContextObject argu) {
        return super.visit(n, argu);
    }

    /**
     * f0 -> PrimaryExpression()
     * f1 -> "<"
     * f2 -> PrimaryExpression()
     */
    public R visit(CompareExpression n, ContextObject argu) {
        return super.visit(n, argu);
    }

    /**
     * f0 -> PrimaryExpression()
     * f1 -> "+"
     * f2 -> PrimaryExpression()
     */
    public R visit(PlusExpression n, ContextObject argu) {
        return super.visit(n, argu);
    }

    /**
     * f0 -> PrimaryExpression()
     * f1 -> "-"
     * f2 -> PrimaryExpression()
     */
    public R visit(MinusExpression n, ContextObject argu) {
        return super.visit(n, argu);
    }

    /**
     * f0 -> PrimaryExpression()
     * f1 -> "*"
     * f2 -> PrimaryExpression()
     */
    public R visit(TimesExpression n, ContextObject argu) {
        return super.visit(n, argu);
    }

    /**
     * f0 -> PrimaryExpression()
     * f1 -> "["
     * f2 -> PrimaryExpression()
     * f3 -> "]"
     */
    public R visit(ArrayLookup n, ContextObject argu) {
        return super.visit(n, argu);
    }

    /**
     * f0 -> PrimaryExpression()
     * f1 -> "."
     * f2 -> "length"
     */
    public R visit(ArrayLength n, ContextObject argu) {
        return super.visit(n, argu);
    }

    /**
     * f0 -> PrimaryExpression()
     * f1 -> "."
     * f2 -> Identifier()
     * f3 -> "("
     * f4 -> ( ExpressionList() )?
     * f5 -> ")"
     */
    public R visit(MessageSend n, ContextObject argu) {
        return super.visit(n, argu);
    }

    /**
     * f0 -> Expression()
     * f1 -> ( ExpressionRest() )*
     */
    public R visit(ExpressionList n, ContextObject argu) {
        return super.visit(n, argu);
    }

    /**
     * f0 -> ","
     * f1 -> Expression()
     */
    public R visit(ExpressionRest n, ContextObject argu) {
        return super.visit(n, argu);
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
    public R visit(PrimaryExpression n, ContextObject argu) {
        return super.visit(n, argu);
    }

    /**
     * f0 -> <INTEGER_LITERAL>
     */
    public R visit(IntegerLiteral n, ContextObject argu) {
        return super.visit(n, argu);
    }

    /**
     * f0 -> "true"
     */
    public R visit(TrueLiteral n, ContextObject argu) {
        return super.visit(n, argu);
    }

    /**
     * f0 -> "false"
     */
    public R visit(FalseLiteral n, ContextObject argu) {
        return super.visit(n, argu);
    }

    /**
     * f0 -> <IDENTIFIER>
     */
    public R visit(Identifier n, ContextObject argu) {
        return super.visit(n, argu);
    }

    /**
     * f0 -> "this"
     */
    public R visit(ThisExpression n, ContextObject argu) {
        return super.visit(n, argu);
    }

    /**
     * f0 -> "new"
     * f1 -> "int"
     * f2 -> "["
     * f3 -> Expression()
     * f4 -> "]"
     */
    public R visit(ArrayAllocationExpression n, ContextObject argu) {
        return super.visit(n, argu);
    }

    /**
     * f0 -> "new"
     * f1 -> Identifier()
     * f2 -> "("
     * f3 -> ")"
     */
    public R visit(AllocationExpression n, ContextObject argu) {
        return super.visit(n, argu);
    }

    /**
     * f0 -> "!"
     * f1 -> Expression()
     */
    public R visit(NotExpression n, ContextObject argu) {
        return super.visit(n, argu);
    }

    /**
     * f0 -> "("
     * f1 -> Expression()
     * f2 -> ")"
     */
    public R visit(BracketExpression n, ContextObject argu) {
        return super.visit(n, argu);
    }
}
