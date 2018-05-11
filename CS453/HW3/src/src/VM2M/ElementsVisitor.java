package VM2M;

import VM2M.elements.*;
import cs132.vapor.ast.*;

public class ElementsVisitor extends VInstr.Visitor {
    public MipsFunction f;
    public ElementsVisitor(MipsFunction f) {
        super();
        this.f = f;
    }

    @Override
    public void visit(VAssign curr) throws Throwable {
        f.addElement(new EAssign(curr));
    }

    @Override
    public void visit(VCall curr) throws Throwable {
        f.addElement(new ECall(curr));
    }

    @Override
    public void visit(VBuiltIn curr) throws Throwable {
        f.addElement(new EBuiltIn(curr));
    }

    @Override
    public void visit(VMemWrite curr) throws Throwable {
        f.addElement(new EMemWrite(curr));
    }

    @Override
    public void visit(VMemRead curr) throws Throwable {
        f.addElement(new EMemRead(curr));
    }

    @Override
    public void visit(VBranch curr) throws Throwable {
        f.addElement(new EBranch(curr));
    }

    @Override
    public void visit(VGoto curr) throws Throwable {
        f.addElement(new EGoto(curr));
    }

    @Override
    public void visit(VReturn curr) throws Throwable {
        f.addElement(new EReturn(curr));
    }
}
