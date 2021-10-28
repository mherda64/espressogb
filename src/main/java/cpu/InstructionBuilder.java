package cpu;

import memory.AddressSpace;

import java.util.List;

public class InstructionBuilder {

    private List<Operation> operations;

    public Instruction build () {
        return new Instruction(operations);
    }

    public InstructionBuilder loadByte(RegEnum reg) {
        operations.add(new Operation() {
            @Override
            public void execute(Registers registers, AddressSpace addressSpace) {
                var val = addressSpace.get(registers.getPC());
                registers.incPC();
                registers.set(reg, val);
            }

            @Override
            public int getCycleLength() {
                return 2;
            }
        });
        return this;
    }

}
