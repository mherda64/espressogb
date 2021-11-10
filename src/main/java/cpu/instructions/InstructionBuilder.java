package cpu.instructions;

import cpu.RegEnum;
import cpu.Registers;
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

    public InstructionBuilder loadBytes(int bytes) {
        operations.add(new Operation() {
            @Override
            public int execute(Registers registers, AddressSpace addressSpace, int accumulator) {
                switch (bytes) {
                    case 1:
                        return addressSpace.get(registers.incPC());

                    case 2:
                        var lo = addressSpace.get(registers.incPC());
                        var hi = addressSpace.get(registers.incPC());
                        return (hi << 8 | lo) & 0xFFFF;
                    default:
                        throw new IllegalStateException(String.format("Trying to load %s bytes of immediate data!", bytes));
                }
            }
        });
        return this;
    }

    public InstructionBuilder store(RegEnum reg) {
        operations.add(new Operation() {
            @Override
            public int execute(Registers registers, AddressSpace addressSpace, int accumulator) {
                registers.set(reg, accumulator);
                return accumulator;
            }
        });
        return this;
    }

    public InstructionBuilder storeRegAddressAccumulator(RegEnum reg) {
        operations.add(new Operation() {
            @Override
            public int execute(Registers registers, AddressSpace addressSpace, int accumulator) {
                addressSpace.set(registers.get(reg), accumulator);
                //TODO what to return??
                return accumulator;
            }
        });
        return this;
    }

    public InstructionBuilder storeAccumulatorAddressReg(RegEnum reg) {
        operations.add(new Operation() {
            @Override
            public int execute(Registers registers, AddressSpace addressSpace, int accumulator) {
                var val = registers.get(reg);
                addressSpace.set(accumulator, val);
                //TODO what to return??
                return val;
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

    public InstructionBuilder loadAddress() {
        operations.add(new Operation() {
            @Override
            public int execute(Registers registers, AddressSpace addressSpace, int accumulator) {
                return addressSpace.get(accumulator) & 0xFF;
            }
        });
        return this;
    }

    public InstructionBuilder add(int value) {
        operations.add(new Operation() {
            @Override
            public int execute(Registers registers, AddressSpace addressSpace, int accumulator) {
                return (accumulator + value) & 0xFFFF;
            }
        });
        return this;
    }

    public InstructionBuilder inc(RegEnum reg) {
        operations.add(new Operation() {
            @Override
            public int execute(Registers registers, AddressSpace addressSpace, int accumulator) {
                registers.set(reg, registers.get(reg) + 1);
                return accumulator;
            }
        });
        return this;
    }

    public InstructionBuilder dec(RegEnum reg) {
        operations.add(new Operation() {
            @Override
            public int execute(Registers registers, AddressSpace addressSpace, int accumulator) {
                registers.set(reg, registers.get(reg) - 1);
                return accumulator;
            }
        });
        return this;
    }


}
