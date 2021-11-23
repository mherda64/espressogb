package cpu.instructions;

import cpu.BitUtils;
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

    public Instruction build(int cycles) {
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
                switch (reg.size) {
                    case RegEnum.SINGLE:
                        addressSpace.set(accumulator, val);
                        break;
                    case RegEnum.DOUBLE:
                        addressSpace.set(accumulator, BitUtils.getLowByte(val));
                        addressSpace.set(accumulator + 1, BitUtils.getHighByte(val));
                        break;
                    default:
                        throw new IllegalStateException(String.format("Illegal register size %d", reg.size));
                }

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

    public InstructionBuilder incrementReg(RegEnum reg) {
        operations.add(new Operation() {
            @Override
            public int execute(Registers registers, AddressSpace addressSpace, int accumulator) {
                //TODO: support for reg size
                registers.set(reg, registers.get(reg) + 1);
                return accumulator;
            }
        });
        return this;
    }

    public InstructionBuilder decrementReg(RegEnum reg) {
        operations.add(new Operation() {
            @Override
            public int execute(Registers registers, AddressSpace addressSpace, int accumulator) {
                registers.set(reg, registers.get(reg) - 1);
                return accumulator;
            }
        });
        return this;
    }

    public InstructionBuilder toSigned() {
        operations.add(new Operation() {
            @Override
            public int execute(Registers registers, AddressSpace addressSpace, int accumulator) {
                return (byte) accumulator;
            }
        });
        return this;
    }

    /*
    This method was writted purely for LDHL SP, n 0xF8 16 bit load and will most probably change in the future
    as I have to work out how to handle flags
     */
    public InstructionBuilder addRegSetFlags(RegEnum reg) {
        operations.add(new Operation() {
            @Override
            public int execute(Registers registers, AddressSpace addressSpace, int accumulator) {
                var newSP = registers.getSP() + accumulator;

                registers.getFlags().setZFlag(false);
                registers.getFlags().setNFlag(false);
                if (accumulator >= 0) {
                    registers.getFlags().setHFlag(((registers.getSP() & 0xF) + (accumulator & 0xF)) > 0xF);
                    registers.getFlags().setCFlag(((registers.getSP() & 0xFF) + (accumulator)) > 0xFF);
                } else {
                    registers.getFlags().setHFlag((newSP & 0xF) <= (registers.getSP() & 0xF));
                    registers.getFlags().setCFlag((newSP & 0xFF) <= (registers.getSP() & 0xFF));
                }

                return newSP;
            }
        });
        return this;
    }

    public InstructionBuilder push(RegEnum reg) {
        operations.add(new Operation() {
            @Override
            public int execute(Registers registers, AddressSpace addressSpace, int accumulator) {
                var value = registers.get(reg);

                addressSpace.set(registers.decSP(), BitUtils.getHighByte(value));
                addressSpace.set(registers.decSP(), BitUtils.getLowByte(value));
                return value;
            }
        });
        return this;
    }

    public InstructionBuilder pop(RegEnum reg) {
        operations.add(new Operation() {
            @Override
            public int execute(Registers registers, AddressSpace addressSpace, int accumulator) {
                var lo = addressSpace.get(registers.getSP());
                var hi = addressSpace.get(registers.incSP());
                registers.incSP();

                var value = (hi << 8 | lo) & 0xFFFF;
                registers.set(reg, value);

                return value;
            }
        });
        return this;
    }

    public InstructionBuilder add(RegEnum reg, boolean addCarry) {
        operations.add(new Operation() {
            @Override
            public int execute(Registers registers, AddressSpace addressSpace, int accumulator) {
                int carry = 0;
                if (addCarry) {
                    carry = registers.getFlags().isCFlag() ? 1 : 0;
                }
                switch (reg.size) {
                    case RegEnum.SINGLE:
                        var newRegVal = registers.get(reg) + accumulator + carry;

                        registers.getFlags().setZFlag((newRegVal & 0xFF) == 0);
                        registers.getFlags().setNFlag(false);
                        registers.getFlags().setHFlag(((registers.get(reg) & 0xF) + (accumulator & 0xF) + carry) > 0xF);
                        registers.getFlags().setCFlag(((registers.get(reg) & 0xFF) + accumulator + carry) > 0xFF);

                        return newRegVal & 0xFF;
                    case RegEnum.DOUBLE:
                        //TODO support 16-bit alu
                        throw new RuntimeException("Not implemented!");
                    default:
                        throw new IllegalStateException(String.format("Illegal register size %d", reg.size));
                }
            }
        });
        return this;
    }

    public InstructionBuilder sub(RegEnum reg, boolean subCarry) {
        operations.add(new Operation() {
            @Override
            public int execute(Registers registers, AddressSpace addressSpace, int accumulator) {
                var carry = subCarry && registers.getFlags().isCFlag() ? 1 : 0;
                return sub(accumulator, registers.get(reg), carry, registers);
            }
        });
        return this;
    }

    public InstructionBuilder subFrom(RegEnum reg, boolean subCarry) {
        operations.add(new Operation() {
            @Override
            public int execute(Registers registers, AddressSpace addressSpace, int accumulator) {
                var carry = subCarry && registers.getFlags().isCFlag() ? 1 : 0;
                return sub(registers.get(reg), accumulator, carry, registers);
            }
        });
        return this;
    }

    public InstructionBuilder cp(RegEnum reg) {
        operations.add(new Operation() {
            @Override
            public int execute(Registers registers, AddressSpace addressSpace, int accumulator) {
                return sub(registers.get(reg), accumulator, 0, registers);
            }
        });
        return this;
    }

    private static int sub(int byte1, int byte2, int carry, Registers registers) {
        var result = byte1 - byte2 - carry;

        registers.getFlags().setZFlag((result & 0xFF) == 0);
        registers.getFlags().setNFlag(true);
        registers.getFlags().setHFlag(((byte1 ^ byte2 ^ (result & 0xFF)) & 0x10) != 0);
        registers.getFlags().setCFlag(result < 0);

        return result & 0xFF;

    }

    public InstructionBuilder and(RegEnum reg) {
        operations.add(new Operation() {
            @Override
            public int execute(Registers registers, AddressSpace addressSpace, int accumulator) {
                var output = (registers.get(reg) & accumulator) & 0xFF;
                registers.getFlags().setZFlag(output == 0);
                registers.getFlags().setNFlag(false);
                registers.getFlags().setHFlag(true);
                registers.getFlags().setCFlag(false);
                return output;
            }
        });
        return this;
    }

    public InstructionBuilder or(RegEnum reg) {
        operations.add(new Operation() {
            @Override
            public int execute(Registers registers, AddressSpace addressSpace, int accumulator) {
                var output = (registers.get(reg) | accumulator) & 0xFF;
                registers.getFlags().setZFlag(output == 0);
                registers.getFlags().setNFlag(false);
                registers.getFlags().setHFlag(false);
                registers.getFlags().setCFlag(false);
                return output;
            }
        });
        return this;
    }

    public InstructionBuilder xor(RegEnum reg) {
        operations.add(new Operation() {
            @Override
            public int execute(Registers registers, AddressSpace addressSpace, int accumulator) {
                var output = (registers.get(reg) ^ accumulator) & 0xFF;
                registers.getFlags().setZFlag(output == 0);
                registers.getFlags().setNFlag(false);
                registers.getFlags().setHFlag(false);
                registers.getFlags().setCFlag(false);
                return output;
            }
        });
        return this;
    }

    public InstructionBuilder inc(int size) {
        operations.add(new Operation() {
            @Override
            public int execute(Registers registers, AddressSpace addressSpace, int accumulator) {
                switch (size) {
                    case RegEnum.SINGLE:
                        var output = (accumulator + 1) & 0xFF;

                        registers.getFlags().setZFlag(output == 0);
                        registers.getFlags().setNFlag(false);
                        registers.getFlags().setHFlag(((accumulator & 0xF) + 1) > 0xF);

                        return output;
                    case RegEnum.DOUBLE:
                        //TODO support 16-bit alu
                        throw new RuntimeException("Not implemented!");
                    default:
                        throw new IllegalStateException(String.format("Illegal register size %d", size));

                }
            }
        });
        return this;
    }

    public InstructionBuilder dec(int size) {
        operations.add(new Operation() {
            @Override
            public int execute(Registers registers, AddressSpace addressSpace, int accumulator) {
                switch (size) {
                    case RegEnum.SINGLE:
                        var output = (accumulator - 1) & 0xFF;

                        registers.getFlags().setZFlag(output == 0);
                        registers.getFlags().setNFlag(true);
                        registers.getFlags().setHFlag(((accumulator ^ 1 ^ (output & 0xFF)) & 0x10) != 0);

                        return output;
                    case RegEnum.DOUBLE:
                        //TODO support 16-bit alu
                        throw new RuntimeException("Not implemented!");
                    default:
                        throw new IllegalStateException(String.format("Illegal register size %d", size));

                }
            }
        });
        return this;
    }
}


