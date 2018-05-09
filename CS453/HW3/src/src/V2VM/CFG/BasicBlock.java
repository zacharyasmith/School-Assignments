package V2VM.CFG;

import V2VM.Variable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;

public class BasicBlock {
    public ArrayList<Node> nodes = new ArrayList<>();
    public ArrayList<BasicBlock> bb_in = new ArrayList<>();
    public ArrayList<BasicBlock> bb_out = new ArrayList<>();
    public HashSet<Variable> variables = new HashSet<>();
    public HashSet<Variable.Interval> live_in = new HashSet<>();
    public HashSet<Variable.Interval> live_out = new HashSet<>();
    public ArrayList<Variable.Interval> intervals = new ArrayList<>();
    public boolean liveness_computed = false;
    public int identifier = -1;

    public void add(Node n) {
        for (Variable v : n.accessor_vars) {
            variables.add(v);
        }
        if (n.assignment != null)
            variables.add(n.assignment);
        nodes.add(n);
    }

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

    public void computeLiveness() {
        liveness_computed = true;
        int[] range = getRange();
        int start = range[0];
        int end = range[1];
        HashSet<Variable.Interval> pass_through = new HashSet<>();
        for (Variable v : variables) {
            Variable.Interval out = null;
            for(Variable.Interval i : live_out) {
                if (i.variable.equals(v)) {
                    out = i;
                    continue;
                }
                if (!pass_through.contains(i))
                    pass_through.add(i);
            }
            ArrayList<Variable.Interval> intervals = v.computeLiveness(start, end, out);
            if (out != null && out.ackFlag())
                    out.live_count--;
            this.intervals.addAll(intervals);
        }
        // push live-in to each BB
        for (Variable.Interval i : intervals) {
            if (i.live_in) {
                live_in.add(i);
                for (BasicBlock b : bb_in) {
                    b.live_out.add(i);
                    i.updateRange(b.getRange());
                    i.live_count++;
                }
            }
        }
        for (Variable.Interval i : pass_through) {
            if (!i.live_in)
                continue;
            live_in.add(i);
            for (BasicBlock b : bb_in) {
                b.live_out.add(i);
                i.updateRange(b.getRange());
            }
        }
    }

    public static boolean within(int start, int end, int loc) {
        return loc >= start && loc <= end;
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

    public static class SortBBAsc implements Comparator<BasicBlock> {
        @Override
        public int compare(BasicBlock o1, BasicBlock o2) {
            return o1.getRange()[0] - o2.getRange()[0];
        }
    }
}
