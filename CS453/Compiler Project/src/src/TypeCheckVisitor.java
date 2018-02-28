import syntaxtree.*;
import visitor.GJDepthFirst;

public class TypeCheckVisitor extends GJDepthFirst<TypeHelper, ContextObject> {

    private boolean debug;
    TypeCheckHelper tch;

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
        n.f14.accept(this, co);
        n.f15.accept(this, co);
        return TypeHelper.NewVoid();
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
        n.f3.accept(this, co);
        n.f4.accept(this, co);
        return TypeHelper.NewVoid();
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
        n.f5.accept(this, co);
        n.f6.accept(this, co);
        return TypeHelper.NewVoid();
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

        n.f7.accept(this, argu);
        n.f8.accept(this, argu);
        
        // assign return type
        argu.expressionType = new TypeHelper(n.f1);
        TypeHelper returned = n.f10.accept(this, argu);
        try {
            TypeHelper.compare(new TypeHelper(n.f1), returned);
        } catch (TypeCheckException e) {
            TypeCheckException.debugString(e, argu, n.f9);
            this.tch.passing = false;
            return TypeHelper.NewErr();
        }
        return TypeHelper.NewVoid();
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
            TypeCheckException.debugString(e, argu, n.f0.f0);
            tch.passing = false;
            return TypeHelper.NewVoid();
        }
        argu.expressionType = null;
        n.f0.accept(this, argu);
        argu.expressionType = curr;
        n.f2.accept(this, argu);
        return TypeHelper.NewVoid();
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
        try {
            // f0 must be array type...
            TypeHelper.compare(tch.searchSymt(argu, n.f0),
                    TypeHelper.NewArray());
        } catch (TypeCheckException e) {
            TypeCheckException.debugString(e, argu, n.f0.f0);
            tch.passing = false;
            return TypeHelper.NewErr();
        }
        argu.expressionType = TypeHelper.NewInt();
        n.f2.accept(this, argu);
        argu.expressionType = TypeHelper.NewInt();
        n.f5.accept(this, argu);
        return TypeHelper.NewVoid();
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
        argu.expressionType = TypeHelper.NewBool();
        n.f2.accept(this, argu);
        n.f4.accept(this, argu);
        n.f6.accept(this, argu);
        return TypeHelper.NewVoid();
    }

    /**
     * f0 -> "while"
     * f1 -> "("
     * f2 -> Expression()
     * f3 -> ")"
     * f4 -> Statement()
     */
    public TypeHelper visit(WhileStatement n, ContextObject argu) {
        argu.expressionType = TypeHelper.NewBool();
        n.f2.accept(this, argu);
        argu.expressionType = null;
        n.f4.accept(this, argu);
        return TypeHelper.NewVoid();
    }

    /**
     * f0 -> "System.out.println"
     * f1 -> "("
     * f2 -> Expression()
     * f3 -> ")"
     * f4 -> ";"
     */
    public TypeHelper visit(PrintStatement n, ContextObject argu) {
        argu.expressionType = TypeHelper.NewInt();
        return n.f2.accept(this, argu);
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
        try {
            TypeHelper thisType;
            TypeHelper returned;
            // if in expression list, overwrite expected type
            if (argu.expressionList) {
                thisType = argu.checkExpressionList(tch);
                argu.expressionType = thisType;
                returned = n.f0.accept(this, new ContextObject(argu, false));
            } else {
                thisType = new TypeHelper(argu.expressionType);
                returned = n.f0.accept(this, argu);
            }
            // process expr
            TypeHelper.compare(thisType, returned);
            return returned;
        } catch (TypeCheckException e) {
            TypeCheckException.debugString(e, argu, n);
            tch.passing = false;
            return TypeHelper.NewErr();
        }
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
        TypeHelper check = new TypeHelper(argu.expressionType);
        // signal to Identifier()
        if (n.f0.which == 3)
            argu.searchSigt = true;
        TypeHelper actual = n.f0.accept(this, argu);
        // reset signal
        argu.searchSigt = false;
        try {
            TypeHelper.compare(check, actual);
            return actual;
        } catch (TypeCheckException e) {
            TypeCheckException.debugString(e, argu, n);
            this.tch.passing = false;
            return TypeHelper.NewErr();
        }
    }

    /**
     * f0 -> PrimaryExpression()
     * f1 -> "&&"
     * f2 -> PrimaryExpression()
     */
    public TypeHelper visit(AndExpression n, ContextObject argu) {
        argu.expressionType = TypeHelper.NewBool();
        n.f0.accept(this, argu);
        argu.expressionType = TypeHelper.NewBool();
        n.f2.accept(this, argu);
        return TypeHelper.NewBool();
    }

    /**
     * f0 -> PrimaryExpression()
     * f1 -> "<"
     * f2 -> PrimaryExpression()
     */
    public TypeHelper visit(CompareExpression n, ContextObject argu) {
        argu.expressionType = TypeHelper.NewInt();
        n.f0.accept(this, argu);
        argu.expressionType = TypeHelper.NewInt();
        n.f2.accept(this, argu);
        return TypeHelper.NewBool();
    }

    /**
     * f0 -> PrimaryExpression()
     * f1 -> "+"
     * f2 -> PrimaryExpression()
     */
    public TypeHelper visit(PlusExpression n, ContextObject argu) {
        argu.expressionType = TypeHelper.NewInt();
        n.f0.accept(this, argu);
        argu.expressionType = TypeHelper.NewInt();
        n.f2.accept(this, argu);
        return TypeHelper.NewInt();
    }

    /**
     * f0 -> PrimaryExpression()
     * f1 -> "-"
     * f2 -> PrimaryExpression()
     */
    public TypeHelper visit(MinusExpression n, ContextObject argu) {
        argu.expressionType = TypeHelper.NewInt();
        n.f0.accept(this, argu);
        argu.expressionType = TypeHelper.NewInt();
        n.f2.accept(this, argu);
        return TypeHelper.NewInt();
    }

    /**
     * f0 -> PrimaryExpression()
     * f1 -> "*"
     * f2 -> PrimaryExpression()
     */
    public TypeHelper visit(TimesExpression n, ContextObject argu) {
        argu.expressionType = TypeHelper.NewInt();
        n.f0.accept(this, argu);
        argu.expressionType = TypeHelper.NewInt();
        n.f2.accept(this, argu);
        return TypeHelper.NewInt();
    }

    /**
     * f0 -> PrimaryExpression()
     * f1 -> "["
     * f2 -> PrimaryExpression()
     * f3 -> "]"
     */
    public TypeHelper visit(ArrayLookup n, ContextObject argu) {
        argu.expressionType = TypeHelper.NewArray();
        n.f0.accept(this, argu);
        argu.expressionType = TypeHelper.NewInt();
        n.f2.accept(this, argu);
        return TypeHelper.NewInt();
    }

    /**
     * f0 -> PrimaryExpression()
     * f1 -> "."
     * f2 -> "length"
     */
    public TypeHelper visit(ArrayLength n, ContextObject argu) {
        argu.expressionType = TypeHelper.NewArray();
        n.f0.accept(this, argu);
        return TypeHelper.NewInt();
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
        try {
            // run through expression list
            ContextObject expList = new ContextObject(argu,
                    clss.objName.f0.tokenImage,
                    method.objName.f0.tokenImage,
                    true);
            n.f4.accept(this, expList);
            if (expList.sigExprTotal != expList.expressionCount + 1)
                throw new TypeCheckException("Method signature not found matching parameters. Too many.");
            return this.tch.searchSigt(clss.objName.f0.tokenImage,
                    method.objName.f0.tokenImage).get(0);
        } catch (TypeCheckException e) {
            TypeCheckException.debugString(e, argu, n.f2.f0);
            this.tch.passing = false;
            return TypeHelper.NewErr();
        }
    }

    /**
     * f0 -> <INTEGER_LITERAL>
     */
    public TypeHelper visit(IntegerLiteral n, ContextObject argu) {
        return TypeHelper.NewInt();
    }

    /**
     * f0 -> "true"
     */
    public TypeHelper visit(TrueLiteral n, ContextObject argu) {
        return TypeHelper.NewBool();
    }

    /**
     * f0 -> "false"
     */
    public TypeHelper visit(FalseLiteral n, ContextObject argu) {
        return TypeHelper.NewBool();
    }

    /**
     * f0 -> <IDENTIFIER>
     */
    public TypeHelper visit(Identifier n, ContextObject argu) {
        if (argu.expressionType != null && argu.searchSigt)
            try {
                return this.tch.searchSymt(argu, n);
            } catch (TypeCheckException e) {
                TypeCheckException.debugString(e, argu, n.f0);
                this.tch.passing = false;
                return TypeHelper.NewErr();
            }
        return new TypeHelper(n);
    }

    /**
     * f0 -> "this"
     */
    public TypeHelper visit(ThisExpression n, ContextObject argu) {
        try {
            return tch.searchObjs(argu.className);
        } catch (TypeCheckException e) {
            TypeCheckException.debugString(e, argu, n.f0);
            this.tch.passing = false;
            return TypeHelper.NewErr();
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
        argu.expressionType = TypeHelper.NewInt();
        n.f3.accept(this, argu);
        return TypeHelper.NewArray();
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
            TypeCheckException.debugString(e, argu, n.f0);
            this.tch.passing = false;
            return TypeHelper.NewErr();
        }
    }

    /**
     * f0 -> "!"
     * f1 -> Expression()
     */
    public TypeHelper visit(NotExpression n, ContextObject argu) {
        argu.expressionType = TypeHelper.NewBool();
        super.visit(n, argu);
        return TypeHelper.NewBool();
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
