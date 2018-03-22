package context;

import syntaxtree.Identifier;

public class TypeHelper {

    public enum Type {
        ArrayType,
        BooleanType,
        IntegerType,
        Identifier,
        Void,
        Error
    }

    public Type type;
    public String objName;

    public static TypeHelper NewArray() {return new TypeHelper(Type.ArrayType);}
    public static TypeHelper NewBool() {return new TypeHelper(Type.BooleanType);}
    public static TypeHelper NewInt() {return new TypeHelper(Type.IntegerType);}
    //    public static TypeHelper NewIdentifier() {return new TypeHelper(Type.IntegerType);}
    public static TypeHelper NewVoid() {return new TypeHelper(Type.Void);}
    public static TypeHelper NewErr() {return new TypeHelper(Type.Error);}

    public TypeHelper(TypeHelper t) {
        this.type = t.type;
        this.objName = t.objName;
    }

    public TypeHelper(Type a) {
        this.type = a;
    }

    public TypeHelper(String a) {
        this.type = Type.Identifier;
        this.objName = a;
    }

    public TypeHelper(syntaxtree.Type a) {
        switch (a.f0.which) {
            case 0: // ArrayType
                type = Type.ArrayType;
                break;
            case 1: // BooleanType
                type = Type.BooleanType;
                break;
            case 2: // IntegerType
                type = Type.IntegerType;
                break;
            case 3: // Identifier
                type = Type.Identifier;
                objName = ((Identifier) a.f0.choice).f0.tokenImage;
                break;
            default:
                type = Type.Error;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TypeHelper that = (TypeHelper) o;

        if (type != that.type) return false;
        return objName != null ? objName.equals(that.objName) : that.objName == null;
    }

    @Override
    public int hashCode() {
        int result = type.hashCode();
        result = 31 * result + (objName != null ? objName.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        if (type == Type.Identifier && objName != null)
            return Type.Identifier + "::" + objName;
        return type.toString();
    }
}
