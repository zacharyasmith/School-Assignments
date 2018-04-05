package V2VM;

import cs132.vapor.ast.*;

public class PrettyPrintVisitor extends VInstr.Visitor {
    VFunction f;
    public PrettyPrintVisitor(VFunction f) {
        super();
        this.f = f;
    }

    @Override
    public void visit(VAssign curr) throws Throwable {
        System.out.println(curr.sourcePos.line + curr.getClass().getSimpleName());
        System.out.println(curr.dest);
        System.out.println(curr.source);
    }

    @Override
    public void visit(VCall curr) throws Throwable {
        System.out.println(curr.sourcePos.line + curr.getClass().getSimpleName());
        if (curr.dest != null)
            System.out.println(curr.dest);
        System.out.println(curr.addr);
    }

    @Override
    public void visit(VBuiltIn curr) throws Throwable {
        System.out.println(curr.sourcePos.line + curr.getClass().getSimpleName());
        if (curr.dest != null)
            System.out.println(curr.dest.toString());
        if (curr.op != null)
            System.out.println(curr.op.name);
        for (VOperand v : curr.args) {
            System.out.println(v);
        }
    }

    @Override
    public void visit(VMemWrite curr) throws Throwable {
        System.out.println(curr.sourcePos.line + curr.getClass().getSimpleName());
    }

    @Override
    public void visit(VMemRead curr) throws Throwable {
        System.out.println(curr.sourcePos.line + curr.getClass().getSimpleName());
    }

    @Override
    public void visit(VBranch curr) throws Throwable {
        System.out.println(curr.sourcePos.line + curr.getClass().getSimpleName());
        System.out.println(curr.value);
        System.out.println(curr.target.ident);
        for (int i = 0; i < f.labels.length; i++) {
            if (f.labels[i].ident.equals(curr.target.ident))
                System.out.println("At" + f.labels[i].instrIndex);
        }
    }

    @Override
    public void visit(VGoto curr) throws Throwable {
        System.out.println(curr.sourcePos.line + curr.getClass().getSimpleName());
        System.out.println(curr.target);
    }

    @Override
    public void visit(VReturn curr) throws Throwable {
        System.out.println(curr.sourcePos.line + curr.getClass().getSimpleName());
    }
}
