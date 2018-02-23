import syntaxtree.Identifier;

public class TypeHelper {
    /**
     * Grammar production:
     * f0 -> ArrayType()
     *       | BooleanType()
     *       | IntegerType()
     *       | Identifier()
     */

    public enum Type {
        ArrayType,
        BooleanType,
        IntegerType,
        Identifier,
        Error
    }

    public Type type;
    public Identifier identifier;

    public TypeHelper(Type a) {
        this.type = a;
    }

    public TypeHelper(Identifier a) {
        type = Type.Identifier;
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
                identifier = (Identifier) a.f0.choice;
                break;
            default:
                type = Type.Error;
        }
    }

    public static void compare (TypeHelper expected, TypeHelper actual) throws TypeCheckException {
        if (expected.type == actual.type)
            if (expected.type == Type.Identifier)
                if (expected.identifier.f0.tokenImage != actual.identifier.f0.tokenImage)
                    throw new TypeCheckException("Expecting identifier `" +
                            expected.identifier.f0.tokenImage + "` but found `" +
                            actual.identifier.f0.tokenImage + "`.");
        throw new TypeCheckException("Expecting type `" +
                expected.type + "` but found `" +
                actual.type+ "`.");
    }
}
