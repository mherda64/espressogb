package cpu;

import java.util.List;

public class Instruction {

    List<Operation> operations;

    public void execute() {};

    public Instruction(List<Operation> operations) {
        this.operations = operations;
    }

    public static InstructionBuilder builder() {
        return new InstructionBuilder();
    }
}
