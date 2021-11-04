package cpu;

import memory.AddressSpace;

import java.util.ArrayList;
import java.util.List;

public class InstructionBuilder {

    private List<Operation> operations;

    public InstructionBuilder() {
        operations = new ArrayList<>();
    }

    public Instruction build (int cycles) {
        return new Instruction(operations, cycles);
    }

    public InstructionBuilder loadByte() {
        operations.add(new Operation() {
            @Override
            public int execute(Registers registers, AddressSpace addressSpace, int accumulator) {
                return addressSpace.get(registers.getPC()) & 0xFF;
            }
        });
        return this;
    }

    public InstructionBuilder loadReg(RegEnum reg) {
        operations.add(new Operation() {
            @Override
            public int execute(Registers registers, AddressSpace addressSpace, int accumulator) {
                return registers.get(reg);
            }
        });
        return this;
    }

    public InstructionBuilder storeByte(RegEnum reg) {
        operations.add(new Operation() {
            @Override
            public int execute(Registers registers, AddressSpace addressSpace, int accumulator) {
                registers.set(reg, accumulator);
                return accumulator;
            }
        });
        return this;
    }

    public InstructionBuilder loadAddressFromReg(RegEnum reg) {
        operations.add(new Operation() {
            @Override
            public int execute(Registers registers, AddressSpace addressSpace, int accumulator) {
                return addressSpace.get(registers.get(reg)) & 0xFF;
            }
        });
        return this;
    }


}
