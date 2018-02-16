import syntaxtree.Type;

public class IdentifierType {
    /**
     * Grammar production:
     * f0 -> ArrayType()
     *       | BooleanType()
     *       | IntegerType()
     *       | Identifier()
     */

    private enum IdType {
        ArrayType,
        BooleanType,
        IntegerType,
        Identifier,
        Error
    }

    public IdType type;

    public IdentifierType(Type a) {
        switch (a.f0.which) {
            case 0: // ArrayType
                type = IdType.ArrayType;
                break;
            case 1: // BooleanType
                type = IdType.BooleanType;
                break;
            case 2: // IntegerType
                type = IdType.IntegerType;
                break;
            case 3: // Identifier
                type = IdType.Identifier;
                break;
            default:
                type = IdType.Error;
        }
    }
}
