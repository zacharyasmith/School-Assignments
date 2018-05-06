package V2VM.CFG;

import java.util.ArrayList;

public class BasicBlockContainer {
    private ArrayList<BasicBlock> bbs = new ArrayList<>();
    private int counter = 1;

    public BasicBlock get(int i) {
        return bbs.get(i);
    }

    public void add(BasicBlock b) {
        bbs.add(b);
        if (b.identifier == -1)
            b.identifier = counter++;
    }

    public void sortAsc() {
        bbs.sort(new BasicBlock.SortBBAsc());
    }

    public int size() {
        return bbs.size();
    }

    public BasicBlock getById(int id) {
        for(BasicBlock b : bbs)
            if (b.identifier == id)
                return b;
        return null;
    }

    public BasicBlock getLast() {
        if (!bbs.isEmpty())
            return bbs.get(bbs.size() - 1);
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
