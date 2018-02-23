import syntaxtree.*;
import visitor.GJDepthFirst;

public class TypeCheckVisitor extends GJDepthFirst<TypeHelper, ContextObject> {

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
    public TypeHelper visit(MainClass n, ContextObject argu) {
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
    public TypeHelper visit(ClassDeclaration n, ContextObject argu) {
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
    public TypeHelper visit(ClassExtendsDeclaration n, ContextObject argu) {
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
    public TypeHelper visit(MethodDeclaration n, ContextObject argu) {
        argu.methodName = n.f2.f0.tokenImage;
        argu.expressionType = null;

        TypeHelper _ret = null;
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
        // TODO accept f10 return type
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
    public TypeHelper visit(AssignmentStatement n, ContextObject argu) {
        TypeHelper curr;
        try {
            curr = tch.searchSymt(argu, n.f0);
        } catch (TypeCheckException e) {
            System.out.println(TypeCheckException.debugString(e, argu, n.f0.f0));
            tch.passing = false;
            return null;
        }
        TypeHelper _ret=null;
        argu.expressionType = null;
        n.f0.accept(this, argu);
        n.f1.accept(this, argu);
        argu.expressionType = curr;
        n.f2.accept(this, argu);
        n.f3.accept(this, argu);
        return _ret;
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
    public TypeHelper visit(ArrayAssignmentStatement n, ContextObject argu) {
        TypeHelper _ret = null;
        try {
            // f0 must be array type...
            tch.searchSymt(argu, n.f0);
        } catch (TypeCheckException e) {
            System.out.println(TypeCheckException.debugString(e, argu, n.f0.f0));
        }
        argu.expressionType = null;
        n.f0.accept(this, argu);
        n.f1.accept(this, argu);
        argu.expressionType = new TypeHelper(TypeHelper.Type.IntegerType);
        n.f2.accept(this, argu);
        n.f3.accept(this, argu);
        n.f4.accept(this, argu);
        n.f5.accept(this, argu);
        n.f6.accept(this, argu);
        return _ret;
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
    public TypeHelper visit(IfStatement n, ContextObject argu) {
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
    public TypeHelper visit(WhileStatement n, ContextObject argu) {
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
    public TypeHelper visit(PrintStatement n, ContextObject argu) {
        argu.expressionType = new TypeHelper(TypeHelper.Type.IntegerType);
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
    public TypeHelper visit(Expression n, ContextObject argu) {
        TypeHelper thisType = new TypeHelper(argu.expressionType);
        TypeHelper returned = n.f0.accept(this, argu);
        try {
            TypeHelper.compare(thisType, returned);
        } catch (TypeCheckException e) {
            System.out.println(TypeCheckException.debugString(e, argu, (NodeToken) n.f0.choice));
        }
        return returned;
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
    public TypeHelper visit(PrimaryExpression n, ContextObject argu) {
        TypeHelper _ret=null;
        n.f0.accept(this, argu);
        return _ret;
    }

    /**
     * f0 -> PrimaryExpression()
     * f1 -> "&&"
     * f2 -> PrimaryExpression()
     */
    public TypeHelper visit(AndExpression n, ContextObject argu) {
        argu.expressionType = new TypeHelper(TypeHelper.Type.BooleanType);
        return super.visit(n, argu);
    }

    /**
     * f0 -> PrimaryExpression()
     * f1 -> "<"
     * f2 -> PrimaryExpression()
     */
    public TypeHelper visit(CompareExpression n, ContextObject argu) {
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
    public TypeHelper visit(PlusExpression n, ContextObject argu) {
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
    public TypeHelper visit(MinusExpression n, ContextObject argu) {
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
    public TypeHelper visit(TimesExpression n, ContextObject argu) {
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
    public TypeHelper visit(ArrayLookup n, ContextObject argu) {
        argu.expressionType = new TypeHelper(TypeHelper.Type.ArrayType);
        n.f0.accept(this, argu);
        n.f1.accept(this, argu);
        argu.expressionType = new TypeHelper(TypeHelper.Type.IntegerType);
        n.f2.accept(this, argu);
        n.f3.accept(this, argu);
        return new TypeHelper(TypeHelper.Type.ArrayType);
    }

    /**
     * f0 -> PrimaryExpression()
     * f1 -> "."
     * f2 -> "length"
     */
    public TypeHelper visit(ArrayLength n, ContextObject argu) {
        argu.expressionType = new TypeHelper(TypeHelper.Type.ArrayType);
        super.visit(n, argu);
        return new TypeHelper(TypeHelper.Type.IntegerType);
    }

    /**
     * f0 -> PrimaryExpression()
     * f1 -> "."
     * f2 -> Identifier()
     * f3 -> "("
     * f4 -> ( ExpressionList() )?
     * f5 -> ")"
     */
    public TypeHelper visit(MessageSend n, ContextObject argu) {
        argu.expressionType = new TypeHelper(TypeHelper.Type.Identifier);
        TypeHelper clss = n.f0.accept(this, argu);
        TypeHelper method = n.f2.accept(this, argu);
        // TODO associate method/clss signatures with argument list
        // n.f4.accept(this, argu);
        try {
            return this.tch.searchSigt(clss.objName.f0.tokenImage, method.objName.f0.tokenImage);
        } catch (TypeCheckException e) {
            System.out.println(TypeCheckException.debugString(e, argu, n.f2.f0));
            this.tch.passing = false;
            return new TypeHelper(TypeHelper.Type.Error);
        }
    }

    /**
     * f0 -> <INTEGER_LITERAL>
     */
    public TypeHelper visit(IntegerLiteral n, ContextObject argu) {
        return super.visit(n, argu);
    }

    /**
     * f0 -> "true"
     */
    public TypeHelper visit(TrueLiteral n, ContextObject argu) {
        return super.visit(n, argu);
    }

    /**
     * f0 -> "false"
     */
    public TypeHelper visit(FalseLiteral n, ContextObject argu) {
        return super.visit(n, argu);
    }

    /**
     * f0 -> <IDENTIFIER>
     */
    public TypeHelper visit(Identifier n, ContextObject argu) {
        return super.visit(n, argu);
    }

    /**
     * f0 -> "this"
     */
    public TypeHelper visit(ThisExpression n, ContextObject argu) {
        try {
            return tch.searchObjs(argu.className);
        } catch (TypeCheckException e) {
            System.out.println(TypeCheckException.debugString(e, argu, n.f0));
            this.tch.passing = false;
            return new TypeHelper(TypeHelper.Type.Error);
        }
    }

    /**
     * f0 -> "new"
     * f1 -> "int"
     * f2 -> "["
     * f3 -> Expression()
     * f4 -> "]"
     */
    public TypeHelper visit(ArrayAllocationExpression n, ContextObject argu) {
        argu.expressionType = new TypeHelper(TypeHelper.Type.IntegerType);
        n.f3.accept(this, argu);
        return new TypeHelper(TypeHelper.Type.ArrayType);
    }

    /**
     * f0 -> "new"
     * f1 -> Identifier()
     * f2 -> "("
     * f3 -> ")"
     */
    public TypeHelper visit(AllocationExpression n, ContextObject argu) {
        try {
            return tch.searchObjs(n.f1);
        } catch (TypeCheckException e) {
            System.out.println(TypeCheckException.debugString(e, argu, n.f0));
            this.tch.passing = false;
            return new TypeHelper(TypeHelper.Type.Error);
        }
    }

    /**
     * f0 -> "!"
     * f1 -> Expression()
     */
    public TypeHelper visit(NotExpression n, ContextObject argu) {
        argu.expressionType = new TypeHelper(TypeHelper.Type.BooleanType);
        super.visit(n, argu);
        return new TypeHelper(TypeHelper.Type.BooleanType);
    }

    /**
     * f0 -> "("
     * f1 -> Expression()
     * f2 -> ")"
     */
    public TypeHelper visit(BracketExpression n, ContextObject argu) {
        return n.f1.accept(this, argu);
    }
}
