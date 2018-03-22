package context;

import syntaxtree.*;
import visitor.*;

import java.util.ArrayList;
import java.util.HashMap;

public class SymTableVisitor<R> extends GJDepthFirst<R, ContextObject> {

    public ArrayList<Symbol> symt = new ArrayList<>();

    public HashMap<ContextObject, ArrayList<Symbol>> sigt = new HashMap<>();

    public ArrayList<ContextObject> objs = new ArrayList<>();

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
     * f15 -> ( StatementElement() )*
     * f16 -> "}"
     * f17 -> "}"
     */
    public R visit(MainClass n, ContextObject argu) {
        ContextObject c = new ContextObject(n.f1.f0.tokenImage);
        return super.visit(n, c);
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
        ContextObject c = new ContextObject(n.f1.f0.tokenImage);
        objs.add(new ContextObject(c));
        return super.visit(n, c);
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
        ContextObject c = new ContextObject(n.f1.f0.tokenImage);
        objs.add(new InheritedContextObject(c, new ContextObject(n.f3.f0.tokenImage)));
        return super.visit(n, c);
    }

    /**
     * f0 -> Type()
     * f1 -> Identifier()
     * f2 -> ";"
     */
    public R visit(VarDeclaration n, ContextObject argu) {
        symt.add(new Symbol(argu, n.f1.f0.tokenImage, new TypeHelper(n.f0)));
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
     * f8 -> ( StatementElement() )*
     * f9 -> "return"
     * f10 -> Expression()
     * f11 -> ";"
     * f12 -> "}"
     */
    public R visit(MethodDeclaration n, ContextObject argu) {
        // argu contains <classname>
        argu.methodName = n.f2.f0.tokenImage;
        ContextObject c = new ContextObject(argu);
        sigt.put(c, new ArrayList<>());
        sigt.get(c).add(new Symbol(c, null, new TypeHelper(n.f1)));
        n.f4.accept(this, c);
        n.f7.accept(this, c);
        return null;
    }

    /**
     * f0 -> Type()
     * f1 -> Identifier()
     */
    public R visit(FormalParameter n, ContextObject argu) {
        Symbol s = new Symbol(argu, n.f1.f0.tokenImage, new TypeHelper(n.f0));
        sigt.get(argu).add(s);
        symt.add(s);
        return super.visit(n, argu);
    }
}
