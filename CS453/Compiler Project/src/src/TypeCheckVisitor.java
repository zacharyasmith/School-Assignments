import syntaxtree.*;
import visitor.GJDepthFirst;

public class TypeCheckVisitor<R> extends GJDepthFirst<R, ContextObject> {

    boolean debug;
    public TypeCheckHelper tch;

    TypeCheckVisitor(boolean debug, TypeCheckHelper tch) {
        this.debug = debug;
        this.tch = tch;
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
        argu.expressionType = null;

        R _ret = null;
        n.f0.accept(this, argu);
        n.f1.accept(this, argu);
        n.f2.accept(this, argu);
        n.f3.accept(this, argu);
        n.f4.accept(this, argu);
        n.f5.accept(this, argu);
        n.f6.accept(this, argu);
        n.f7.accept(this, argu);
        n.f8.accept(this, argu);
        n.f9.accept(this, argu);
        // assign return type
        argu.expressionType = new TypeHelper(n.f1);
        n.f10.accept(this, argu);
        n.f11.accept(this, argu);
        n.f12.accept(this, argu);
        return _ret;
    }

    /**
     * f0 -> Identifier()
     * f1 -> "="
     * f2 -> Expression()
     * f3 -> ";"
     */
    public R visit(AssignmentStatement n, ContextObject argu) {
        try {
            argu.expressionType = tch.searchSymt(argu, n.f0);
        } catch (TypeCheckException e) {
            System.out.println(TypeCheckException.debugString(e, argu, n.f0.f0));
            tch.passing = false;
            return null;
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
        argu.expressionType = new TypeHelper(TypeHelper.Type.IntegerType);
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
        argu.expressionType = new TypeHelper(TypeHelper.Type.BooleanType);
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
        argu.expressionType = new TypeHelper(TypeHelper.Type.BooleanType);
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
        argu.expressionType = new TypeHelper(TypeHelper.Type.IntegerType);
        return super.visit(n, argu);
    }

    /**
     * f0 -> PrimaryExpression()
     * f1 -> "&&"
     * f2 -> PrimaryExpression()
     */
    public R visit(AndExpression n, ContextObject argu) {
        try {
            TypeHelper.compare(argu.expressionType,
                    new TypeHelper(TypeHelper.Type.BooleanType));
        } catch (TypeCheckException e) {
            System.out.println(TypeCheckException.debugString(e, argu, n.f1));
            this.tch.passing = false;
            return null;
        }
        return super.visit(n, argu);
    }

    /**
     * f0 -> PrimaryExpression()
     * f1 -> "<"
     * f2 -> PrimaryExpression()
     */
    public R visit(CompareExpression n, ContextObject argu) {
        try {
            TypeHelper.compare(argu.expressionType,
                    new TypeHelper(TypeHelper.Type.BooleanType));
        } catch (TypeCheckException e) {
            System.out.println(TypeCheckException.debugString(e, argu, n.f1));
            this.tch.passing = false;
            return null;
        }
        return super.visit(n, argu);
    }

    /**
     * f0 -> PrimaryExpression()
     * f1 -> "+"
     * f2 -> PrimaryExpression()
     */
    public R visit(PlusExpression n, ContextObject argu) {
        try {
            TypeHelper.compare(argu.expressionType,
                    new TypeHelper(TypeHelper.Type.IntegerType));
        } catch (TypeCheckException e) {
            System.out.println(TypeCheckException.debugString(e, argu, n.f1));
            this.tch.passing = false;
            return null;
        }
        return super.visit(n, argu);
    }

    /**
     * f0 -> PrimaryExpression()
     * f1 -> "-"
     * f2 -> PrimaryExpression()
     */
    public R visit(MinusExpression n, ContextObject argu) {
        try {
            TypeHelper.compare(argu.expressionType,
                    new TypeHelper(TypeHelper.Type.IntegerType));
        } catch (TypeCheckException e) {
            System.out.println(TypeCheckException.debugString(e, argu, n.f1));
            this.tch.passing = false;
            return null;
        }
        return super.visit(n, argu);
    }

    /**
     * f0 -> PrimaryExpression()
     * f1 -> "*"
     * f2 -> PrimaryExpression()
     */
    public R visit(TimesExpression n, ContextObject argu) {
        try {
            TypeHelper.compare(argu.expressionType,
                    new TypeHelper(TypeHelper.Type.IntegerType));
        } catch (TypeCheckException e) {
            System.out.println(TypeCheckException.debugString(e, argu, n.f1));
            this.tch.passing = false;
            return null;
        }
        return super.visit(n, argu);
    }

    /**
     * f0 -> PrimaryExpression()
     * f1 -> "["
     * f2 -> PrimaryExpression()
     * f3 -> "]"
     */
    public R visit(ArrayLookup n, ContextObject argu) {
        try {
            TypeHelper.compare(argu.expressionType,
                    new TypeHelper(TypeHelper.Type.IntegerType));
        } catch (TypeCheckException e) {
            System.out.println(TypeCheckException.debugString(e, argu, n.f1));
            this.tch.passing = false;
            return null;
        }
        R _ret = null;
        argu.expressionType = new TypeHelper(TypeHelper.Type.ArrayType);
        n.f0.accept(this, argu);
        n.f1.accept(this, argu);
        argu.expressionType = new TypeHelper(TypeHelper.Type.IntegerType);
        n.f2.accept(this, argu);
        n.f3.accept(this, argu);
        return _ret;
    }

    /**
     * f0 -> PrimaryExpression()
     * f1 -> "."
     * f2 -> "length"
     */
    public R visit(ArrayLength n, ContextObject argu) {
        try {
            TypeHelper.compare(argu.expressionType,
                    new TypeHelper(TypeHelper.Type.IntegerType));
        } catch (TypeCheckException e) {
            System.out.println(TypeCheckException.debugString(e, argu, n.f1));
            this.tch.passing = false;
            return null;
        }
        argu.expressionType = new TypeHelper(TypeHelper.Type.ArrayType);
        return super.visit(n, argu);
    }

    /**
     * f0 -> PrimaryExpression()
     * f1 -> "."
     * f2 -> Identifier()
     * f3 -> "("
     * f4 -> ( ExpressionList() )?
     * f5 -> ")"
     * <p>
     * Allowable:
     * f0 -> 3 Identifier()
     * | 4 ThisExpression()
     * | 6 AllocationExpression()
     */
    public R visit(MessageSend n, ContextObject argu) {
        // TODO ensure f0
        // TODO ensure f2 associate with f0
        // TODO ensure f4 as needed for f2
        return super.visit(n, argu);
    }

    /**
     * f0 -> 0 IntegerLiteral()
     * | 1 TrueLiteral()
     * | 2 FalseLiteral()
     * | 3 Identifier()
     * | 4 ThisExpression()
     * | 5 ArrayAllocationExpression()
     * | 6 AllocationExpression()
     * | 7 NotExpression()
     * | 8 BracketExpression()
     */
    public R visit(PrimaryExpression n, ContextObject argu) {
        return super.visit(n, argu);
    }

    /**
     * f0 -> <INTEGER_LITERAL>
     */
    public R visit(IntegerLiteral n, ContextObject argu) {
        try {
            TypeHelper.compare(argu.expressionType,
                    new TypeHelper(TypeHelper.Type.IntegerType));
        } catch (TypeCheckException e) {
            System.out.println(TypeCheckException.debugString(e, argu, n.f0));
            this.tch.passing = false;
        }
        return super.visit(n, argu);
    }

    /**
     * f0 -> "true"
     */
    public R visit(TrueLiteral n, ContextObject argu) {
        try {
            TypeHelper.compare(argu.expressionType,
                    new TypeHelper(TypeHelper.Type.BooleanType));
        } catch (TypeCheckException e) {
            System.out.println(TypeCheckException.debugString(e, argu, n.f0));
            this.tch.passing = false;
        }
        return super.visit(n, argu);
    }

    /**
     * f0 -> "false"
     */
    public R visit(FalseLiteral n, ContextObject argu) {
        try {
            TypeHelper.compare(argu.expressionType,
                    new TypeHelper(TypeHelper.Type.BooleanType));
        } catch (TypeCheckException e) {
            System.out.println(TypeCheckException.debugString(e, argu, n.f0));
            this.tch.passing = false;
        }
        return super.visit(n, argu);
    }

    /**
     * f0 -> <IDENTIFIER>
     */
    public R visit(Identifier n, ContextObject argu) {
        if (argu.expressionType != null)
            try {
                TypeHelper.compare(argu.expressionType,
                        new TypeHelper(n));
            } catch (TypeCheckException e) {
                System.out.println(TypeCheckException.debugString(e, argu, n.f0));
                this.tch.passing = false;
                return null;
            }
        return super.visit(n, argu);
    }

    /**
     * f0 -> "this"
     */
    public R visit(ThisExpression n, ContextObject argu) {
        // TODO associate this with context
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
        try {
            TypeHelper.compare(argu.expressionType,
                    new TypeHelper(TypeHelper.Type.ArrayType));
        } catch (TypeCheckException e) {
            System.out.println(TypeCheckException.debugString(e, argu, n.f0));
            this.tch.passing = false;
            return null;
        }
        argu.expressionType = new TypeHelper(TypeHelper.Type.IntegerType);
        return super.visit(n, argu);
    }

    /**
     * f0 -> "new"
     * f1 -> Identifier()
     * f2 -> "("
     * f3 -> ")"
     */
    public R visit(AllocationExpression n, ContextObject argu) {
        try {
            TypeHelper.compare(argu.expressionType,
                    new TypeHelper(n.f1));
        } catch (TypeCheckException e) {
            System.out.println(TypeCheckException.debugString(e, argu, n.f0));
            this.tch.passing = false;
            return null;
        }
        return super.visit(n, argu);
    }

    /**
     * f0 -> "!"
     * f1 -> Expression()
     */
    public R visit(NotExpression n, ContextObject argu) {
        try {
            TypeHelper.compare(argu.expressionType,
                    new TypeHelper(TypeHelper.Type.BooleanType));
        } catch (TypeCheckException e) {
            System.out.println(TypeCheckException.debugString(e, argu, n.f0));
            this.tch.passing = false;
            return null;
        }
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
