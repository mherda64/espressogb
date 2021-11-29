package cpu.instruction;

import cpu.Context;

import java.util.List;
import java.util.function.Function;

public class Instruction {

    private List<Operation> operations;
    private int cycles;
    private Function<Context, Integer> cyclesFun;
    private Context context;

    public Instruction(List<Operation> operations, Function<Context, Integer> cyclesFun) {
        this.operations = operations;
        this.cyclesFun = cyclesFun;
    }

    public static InstructionBuilder builder() {
        return new InstructionBuilder();
    }

    public List<Operation> getOperations() {
        return operations;
    }

    public int getCycles(Context context) {
        return cyclesFun.apply(context);
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }
}
