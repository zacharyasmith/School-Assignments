package V2VM.CFG;

import java.util.ArrayList;

public class BasicBlockContainer {
    private ArrayList<BasicBlock> bbs = new ArrayList<>();
    private int counter = 1;

    public void add(BasicBlock b) {
        bbs.add(b);
        if (b.identifier == -1)
            b.identifier = counter++;
    }

    public BasicBlock getById(int id) {
        for(BasicBlock b : bbs)
            if (b.identifier == id)
                return b;
        return null;
    }

    public boolean contains(BasicBlock b) {
        return bbs.contains(b);
    }

    public BasicBlock getAtLine(int line_number) {
        for (BasicBlock b : bbs) {
            if (line_number >= b.getRange()[0] &&
                    line_number <= b.getRange()[1])
                return b;
        }
        return null;
    }

    @Override
    public String toString() {
        String ret = "";
        for (BasicBlock b : bbs)
            ret += b + "\n";
        return ret;
    }
}
