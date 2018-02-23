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
    public Identifier objName;

    public TypeHelper(TypeHelper t) {
        this.type = t.type;
        this.objName = t.objName;
    }

    public TypeHelper(Type a) {
        this.type = a;
    }

    public TypeHelper(Identifier a) {
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
                objName = (Identifier) a.f0.choice;
                break;
            default:
                type = Type.Error;
        }
    }

    public static void compare (TypeHelper expected, TypeHelper actual) throws TypeCheckException {
        if (expected.type == actual.type) {
            if (expected.type == Type.Identifier)
                if (expected.objName.f0.tokenImage != actual.objName.f0.tokenImage)
                    throw new TypeCheckException("Expecting identifier `" +
                            expected.objName.f0.tokenImage + "` but found `" +
                            actual.objName.f0.tokenImage + "`.");
            return;
        }
        throw new TypeCheckException("Expecting type `" +
                expected.type + "` but found `" +
                actual.type + "`.");
    }
}
