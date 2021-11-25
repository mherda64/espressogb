package cpu.instruction;

import java.util.List;

public class Instruction {

    private List<Operation> operations;
    // TODO ustalić gdzie nam to potrzebne jest
    private int cycles;

    public Instruction(List<Operation> operations, int cycles) {
        this.operations = operations;
        this.cycles = cycles;
    }

    public static InstructionBuilder builder() {
        return new InstructionBuilder();
    }

    public List<Operation> getOperations() {
        return operations;
    }

    public int getCycles() {
        return cycles;
    }
}
