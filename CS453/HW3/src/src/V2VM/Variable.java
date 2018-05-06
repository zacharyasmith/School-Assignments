package V2VM;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

public class Variable {
    public ArrayList<Integer> assign = new ArrayList<>();
    public ArrayList<Integer> access = new ArrayList<>();
    public boolean is_paramater = false;
    public String name;
    public Register arg = null;
    public ArrayList<Interval> intervals = new ArrayList<>();
    public Variable (String name) {
        this.name = name;
    }

    public ArrayList<Interval> computeLiveness(int start, int end, Interval live_out) {
        ArrayList<Interval> live = new ArrayList<>();
        Interval curr_interval = live_out;
        if (curr_interval != null)
            curr_interval.setEarliestAssign(earliestAssign(start, end));
        int last_assign = -1;
        for (int i = access.size() - 1; i >= 0; i--) {
            int curr_access = access.get(i);
            // continue if access before start, end
            if (curr_access > end)
                continue;
            else if (curr_access < start)
                break;
            // get nearest assignment
            int soonest_assign = soonestAssign(curr_access, start);
            if (curr_interval == null || last_assign > curr_access) {
                // create new interval therefore
                curr_interval = new Interval(this, soonest_assign,
                        curr_access, soonest_assign == -1);
                live.add(curr_interval);
                intervals.add(curr_interval);
            } else if (curr_interval != null) {
                if (soonest_assign > -1) {
                    curr_interval.start = soonest_assign;
                    curr_interval.live_in = false;
                }
                live.add(curr_interval);
                if (!intervals.contains(curr_interval))
                    intervals.add(curr_interval);
            }
            last_assign = soonest_assign;
        }
        return live;
    }

    private int soonestAssign(int loc, int min) {
        for (int i = assign.size() - 1; i >= 0; i--) {
            int curr = assign.get(i);
            if (curr <= loc && curr >= min)
                return curr;
        }
        return -1;
    }

    private int earliestAssign(int start, int end) {
        for (int i = 0; i < assign.size(); i++) {
            int curr = assign.get(i);
            if (curr >= start && curr <= end)
                return curr;
        }
        return -1;
    }

    public void normalize() {
        for (Interval i : intervals)
            i.normalize();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Variable variable = (Variable) o;

        return name.equals(variable.name);
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }

    @Override
    public String toString() {
        String ret = name + ":" + (is_paramater ? " p" : "") + " {";
        for (int i = 0; i < intervals.size(); i++) {
            ret += (i == 0 ? "" : ",") + intervals.get(i);
        }
        ret += "}" + ": Assign: ";
        for (Integer i : assign)
            ret += i.toString() + " ";
        ret += "Access: ";
        for (Integer i : access)
            ret += i.toString() + " ";
        return ret;
    }

    public static class VariableEndComparator implements Comparator<Variable> {
        @Override
        public int compare(Variable o1, Variable o2) {
            if (o1.intervals.isEmpty())
                return 1;
            if (o2.intervals.isEmpty())
                return -1;
            return o1.intervals.get(0).end - o2.intervals.get(0).end;
        }
    }

    public class Interval {
        public Variable variable;
        public Register register;
        public boolean live_in;
        public ArrayList<Integer> spill = new ArrayList<>();
        public int stack_location = -1;
        public int start = -1;
        public int end = -1;
        private int earliest_assign = -1;
        public boolean flag = false;
        public int live_count = 0;

        public Interval(Variable v, int start, int end, boolean live_in) {
            this.variable = v;
            this.start = start;
            this.end = end;
            this.live_in = live_in;
        }

        public void setEarliestAssign(int loc) {
            if (loc != -1) {
                earliest_assign = earliest_assign == -1 ? loc : Math.min(earliest_assign, loc);
                flag = true;
            }
        }

        public void setPassThroughRange(int[] range) {
            int start = range[0];
            int end = range[1];
            this.end = Math.max(this.end, end);
            this.start = Math.min(this.start, start);
        }

        public boolean ackFlag() {
            boolean ret = flag;
            flag = false;
            return ret;
        }

        public void normalize() {
            if (earliest_assign > -1 && live_count == 0) {
                start = earliest_assign;
                live_in = false;
            }
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Interval interval = (Interval) o;

            if (start != interval.start) return false;
            if (end != interval.end) return false;
            return variable.equals(interval.variable);
        }

        @Override
        public int hashCode() {
            int result = variable.hashCode();
            result = 31 * result + start;
            result = 31 * result + end;
            return result;
        }

        @Override
        public String toString() {
            return (register == null ? variable.name : register)
                    + "[" + (!live_in ? start : "LI") + "," +
                    end + "" + (stack_location > -1 ? ",Spill{" + stack_location + "}"
                    + ":" + Arrays.toString(spill.toArray()) : "" ) + "]";
        }
    }

    public static class IntervalEndComparator implements Comparator<Interval> {
        @Override
        public int compare(Interval o1, Interval o2) {
            return o1.end - o2.end;
        }
    }

    public static class IntervalStartComparator implements Comparator<Interval> {
        @Override
        public int compare(Interval o1, Interval o2) {
            return o1.start - o2.start;
        }
    }
}
