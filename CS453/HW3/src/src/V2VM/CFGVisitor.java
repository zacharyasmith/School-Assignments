package V2VM;

import V2VM.CFG.CFG;
import V2VM.CFG.Node;
import V2VM.elements.*;
import cs132.vapor.ast.*;

public class CFGVisitor extends VInstr.Visitor {
    CFG cfg;
    public CFGVisitor(CFG cfg) {
        this.cfg = cfg;
    }

    @Override
    public void visit(VAssign curr) throws Throwable {
        Node n = new Node(curr.sourcePos.line, new EAssign(curr));
        n.setAssignment(cfg.searchVar(curr.dest.toString()));
        if (curr.source instanceof VVarRef.Local)
            n.addAccessor(cfg.searchVar(((VVarRef.Local) curr.source).ident));
        cfg.addLast(n);
    }

    @Override
    public void visit(VCall curr) throws Throwable {
        Node n = new Node(curr.sourcePos.line, new ECall(curr));
        if (curr.dest != null)
            n.setAssignment(cfg.searchVar(curr.dest.ident));
        if (curr.addr instanceof VAddr.Var)
            n.addAccessor(cfg.searchVar(((VAddr.Var) curr.addr).var.toString()));
        for (VOperand o : curr.args)
            if (o instanceof VVarRef.Local)
                n.addAccessor(cfg.searchVar(((VVarRef.Local) o).ident));
        cfg.addLast(n);
    }

    @Override
    public void visit(VBuiltIn curr) throws Throwable {
        Node n = new Node(curr.sourcePos.line, new EBuiltIn(curr));
        if (curr.dest != null)
            n.setAssignment(cfg.searchVar(curr.dest.toString()));
        for (VOperand o : curr.args)
            if (o instanceof VVarRef.Local)
                n.addAccessor(cfg.searchVar(((VVarRef.Local) o).ident));
        cfg.addLast(n);
    }

    @Override
    public void visit(VMemWrite curr) throws Throwable {
        Node n = new Node(curr.sourcePos.line, new EMemWrite(curr));
        if (curr.dest instanceof VMemRef.Global && ((VMemRef.Global) curr.dest).base instanceof VAddr.Var)
            n.addAccessor(cfg.searchVar(((VAddr.Var) ((VMemRef.Global) curr.dest).base).var.toString()));
        if (curr.source instanceof VVarRef.Local)
            n.addAccessor(cfg.searchVar(((VVarRef.Local) curr.source).ident));
        cfg.addLast(n);
    }

    @Override
    public void visit(VMemRead curr) throws Throwable {
        Node n = new Node(curr.sourcePos.line, new EMemRead(curr));
        if (curr.dest != null)
            n.setAssignment(cfg.searchVar(curr.dest.toString()));
        if (curr.source instanceof VMemRef.Global && ((VMemRef.Global) curr.source).base instanceof VAddr.Var)
            n.addAccessor(cfg.searchVar(((VAddr.Var) ((VMemRef.Global) curr.source).base).var.toString()));
        cfg.addLast(n);
    }

    @Override
    public void visit(VBranch curr) throws Throwable {
        Node n = new Node(curr.sourcePos.line, new EBranch(curr));
        if (curr.value instanceof VVarRef.Local)
            n.addAccessor(cfg.searchVar(((VVarRef.Local) curr.value).ident));
        cfg.addLast(n);
    }

    @Override
    public void visit(VGoto curr) throws Throwable {
        Node n = new Node(curr.sourcePos.line, new EGoto(curr));
        cfg.addLast(n);
    }

    @Override
    public void visit(VReturn curr) throws Throwable {
        Node n = new Node(curr.sourcePos.line, new EReturn(curr));
        if (curr.value != null && curr.value instanceof VVarRef.Local)
            n.addAccessor(cfg.searchVar(((VVarRef.Local) curr.value).ident));
        cfg.addLast(n);
    }
}
