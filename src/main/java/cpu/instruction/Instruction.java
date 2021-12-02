package cpu.instruction;

import cpu.Context;

import java.util.List;
import java.util.function.Function;

public class Instruction {

    private List<Operation> operations;
    private Function<Context, Integer> cyclesFun;
    private Context context;
    private String label;

    public Instruction(List<Operation> operations, Function<Context, Integer> cyclesFun, String label) {
        this.operations = operations;
        this.cyclesFun = cyclesFun;
        this.label = label;
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

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }
}
