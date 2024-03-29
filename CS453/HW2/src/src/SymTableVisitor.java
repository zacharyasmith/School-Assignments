import syntaxtree.*;
import visitor.*;

import java.util.ArrayList;
import java.util.HashMap;

public class SymTableVisitor<R> extends GJDepthFirst<R,String> {

    public HashMap<String, TypeHelper> symt = new HashMap<>();

    public HashMap<String, ArrayList<TypeHelper>> sigt = new HashMap<>();

    public ArrayList<String> objs = new ArrayList<>();

    public HashMap<String, String> inherits = new HashMap<>();

    public void objsEnsure(String clss) {
        if (objs.contains(clss))
            throw new TypeCheckException("Class `" + clss + "` has already been defined.");
        objs.add(clss);
    }

    public void symtEnsure(String var, TypeHelper t) {
        if (symt.containsKey(var))
            throw new TypeCheckException("Variable `" + var + "` has already been defined.");
        symt.put(var, t);
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
    public R visit(MainClass n, String argu) {
        String id = n.f1.f0.tokenImage + "::main";
        return super.visit(n, id);
    }

    /**
     * f0 -> "class"
     * f1 -> Identifier()
     * f2 -> "{"
     * f3 -> ( VarDeclaration() )*
     * f4 -> ( MethodDeclaration() )*
     * f5 -> "}"
     */
    public R visit(ClassDeclaration n, String argu) {
        String id = n.f1.f0.tokenImage;
        objsEnsure(n.f1.f0.tokenImage);
        return super.visit(n, id);
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
    public R visit(ClassExtendsDeclaration n, String argu) {
        String id = n.f1.f0.tokenImage;
        objsEnsure(n.f1.f0.tokenImage);
        inherits.put(n.f1.f0.tokenImage, n.f3.f0.tokenImage);
        return super.visit(n, id);
    }

    /**
     * f0 -> Type()
     * f1 -> Identifier()
     * f2 -> ";"
     */
    public R visit(VarDeclaration n, String argu) {
        symtEnsure(argu + "::" + n.f1.f0.tokenImage, new TypeHelper(n.f0));
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
    public R visit(MethodDeclaration n, String argu) {
        // argu = <classname>
        String id = argu + "::" + n.f2.f0.tokenImage;
        sigt.put(id, new ArrayList<>());
        sigt.get(id).add(new TypeHelper(n.f1));
        n.f4.accept(this, id);
        n.f7.accept(this, id);
        return null;
    }

    /**
     * f0 -> Type()
     * f1 -> Identifier()
     */
    public R visit(FormalParameter n, String argu) {
        sigt.get(argu).add(new TypeHelper(n.f0));
        symtEnsure(argu + "::" + n.f1.f0.tokenImage, new TypeHelper(n.f0));
        return super.visit(n, argu);
    }
}
