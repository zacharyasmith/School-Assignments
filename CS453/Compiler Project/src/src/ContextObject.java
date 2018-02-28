public class ContextObject {
    public String className;
    public String methodName;
    public TypeHelper expressionType = null;
    public boolean expressionList = false;
    public boolean searchSigt = false;
    public String listClass = null;
    public String listMeth = null;
    public int expressionCount = 0;
    public int sigExprTotal = -1;

    public ContextObject(ContextObject c, boolean expressionList) {
        this.expressionList = expressionList;
        if (expressionList)
            this.expressionCount = c.expressionCount;
        this.listClass = c.listClass;
        this.listMeth = c.listMeth;
        this.expressionType = c.expressionType;
        this.methodName = c.methodName;
        this.className = c.className;
        this.searchSigt = c.searchSigt;
    }

    public ContextObject(String className, String methodName) {
        this.className = className;
        this.methodName = methodName;
    }

    public ContextObject(ContextObject c, String className,
                         String methodName, boolean expressionList) {
        this.expressionList = expressionList;
        this.listClass = className;
        this.listMeth = methodName;
        this.expressionCount = c.expressionCount;
        this.expressionType = c.expressionType;
        this.methodName = c.methodName;
        this.className = c.className;
        this.searchSigt = c.searchSigt;
    }

    public TypeHelper checkExpressionList(TypeCheckHelper tch) throws TypeCheckException {
        if (!expressionList)
            throw new TypeCheckException("Internal: checkExpressionList Called erroneously.");
        if (sigExprTotal == -1)
            sigExprTotal = tch.searchSigt(this.listClass, this.listMeth).size();
        ++expressionCount;
        if (sigExprTotal >= expressionCount + 1)
            return tch.searchSigt(this.listClass, this.listMeth).get(expressionCount);
        throw new TypeCheckException("Method signature not found matching parameters. Too many parameters.");
    }

    @Override
    public String toString() {
        return "Context: Class::" + className + " Method::" + methodName + " EType::"
                + expressionType + " List::" + expressionList + ":" + expressionCount;
    }
}
