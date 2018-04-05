package V2VM;

import cs132.vapor.ast.*;

public class VaporVisitor extends VInstr.Visitor {
    public VaporVisitor() {
        super();
    }

    @Override
    public void visit(VAssign curr) throws Throwable {
        System.out.println(curr.getClass().getSimpleName());
    }

    @Override
    public void visit(VCall curr) throws Throwable {
        System.out.println(curr.getClass().getSimpleName());
    }

    @Override
    public void visit(VBuiltIn curr) throws Throwable {
        System.out.println(curr.getClass().getSimpleName());
        System.out.println(curr.dest.toString());
        System.out.println(curr.op.name);
        for (VOperand v : curr.args) {
            System.out.println(v);
        }
    }

    @Override
    public void visit(VMemWrite curr) throws Throwable {
        System.out.println(curr.getClass().getSimpleName());
    }

    @Override
    public void visit(VMemRead curr) throws Throwable {
        System.out.println(curr.getClass().getSimpleName());
    }

    @Override
    public void visit(VBranch curr) throws Throwable {
        System.out.println(curr.getClass().getSimpleName());
    }

    @Override
    public void visit(VGoto curr) throws Throwable {
        System.out.println(curr.getClass().getSimpleName());
    }

    @Override
    public void visit(VReturn curr) throws Throwable {
        System.out.println(curr.getClass().getSimpleName());
    }
}
