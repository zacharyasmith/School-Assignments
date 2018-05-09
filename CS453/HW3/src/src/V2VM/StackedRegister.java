package V2VM;

public class StackedRegister extends Register {
    private int array_index;
    public StackedRegister(int array_index) {
        super("");
        this.array_index = array_index;
    }

    @Override
    public String toString() {
        return "[" + array_index + "]";
    }
}
