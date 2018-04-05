package V2VM;

import V2VM.CFG.CFG;
import V2VM.CFG.Node;
import cs132.vapor.ast.*;

public class CFGVisitor extends VInstr.Visitor {
    CFG cfg;
    public CFGVisitor(CFG cfg) {
        this.cfg = cfg;
    }

    @Override
    public void visit(VAssign curr) throws Throwable {
        cfg.addLast(new Node(curr, curr.sourcePos.line));
    }

    @Override
    public void visit(VCall curr) throws Throwable {
        cfg.addLast(new Node(curr, curr.sourcePos.line));
    }

    @Override
    public void visit(VBuiltIn curr) throws Throwable {
        cfg.addLast(new Node(curr, curr.sourcePos.line));
    }

    @Override
    public void visit(VMemWrite curr) throws Throwable {
        cfg.addLast(new Node(curr, curr.sourcePos.line));
    }

    @Override
    public void visit(VMemRead curr) throws Throwable {
        cfg.addLast(new Node(curr, curr.sourcePos.line));
    }

    @Override
    public void visit(VBranch curr) throws Throwable {
        cfg.addLast(new Node(curr, curr.sourcePos.line));
    }

    @Override
    public void visit(VGoto curr) throws Throwable {
        cfg.addLast(new Node(curr, curr.sourcePos.line));
    }

    @Override
    public void visit(VReturn curr) throws Throwable {
        cfg.addLast(new Node(curr, curr.sourcePos.line));
    }
}
