package cpu;

import memory.AddressSpace;

import java.util.List;

public class Instruction {

    private List<Operation> operations;
    // TODO ustalić gdzie nam to potrzebne jest
    private int cycles;

    // W sumie to mozna w CPU już brać po operacji i wykonywać, zamiast wykonywać wszystkie na raz
//    public void execute(Registers registers, AddressSpace addressSpace) {
//        operations.forEach(operation -> operation.execute(registers, addressSpace));
//    };

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
