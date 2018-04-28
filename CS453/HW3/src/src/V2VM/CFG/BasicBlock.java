package V2VM.CFG;

import java.util.ArrayList;
import java.util.Arrays;

public class BasicBlock {
    public ArrayList<Node> nodes = new ArrayList<>();
    public ArrayList<BasicBlock> bb_in = new ArrayList<>();
    public ArrayList<BasicBlock> bb_out = new ArrayList<>();
    public int identifier = -1;

    public int[] getRange() {
        int start = 0x7FFFFFFF;
        int stop = 0;
        for (Node n : nodes) {
            start = Math.min(start, n.line_number);
            stop = Math.max(stop, n.line_number);
        }
        return new int[] {start, stop};
    }

    public Node getLast() {
        if (nodes.size() == 0)
            return null;
        return nodes.get(nodes.size() - 1);
    }

    @Override
    public String toString() {
        String ret = identifier + ": {";
        for (int i = 0; i < bb_in.size(); i++)
            ret += (i == 0 ? "" : ",") + bb_in.get(i).identifier;
        ret += "} -> " + Arrays.toString(getRange()) + " -> {";
        for (int i = 0; i < bb_out.size(); i++)
            ret += (i == 0 ? "" : ",") + bb_out.get(i).identifier;
        ret += "}";
        return ret;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BasicBlock that = (BasicBlock) o;

        return nodes.equals(that.nodes);
    }

    @Override
    public int hashCode() {
        return nodes.hashCode();
    }
}
