package cpu.instruction;

import cpu.*;
import cpu.instruction.appender.JumpCondition;
import memory.AddressSpace;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

import static cpu.BitUtils.*;

public class InstructionBuilder {

    private List<Operation> operations;

    private String label;

    public InstructionBuilder() {
        operations = new ArrayList<>();
    }

    public Instruction build(Function<Context, Integer> cycleFun) {
        return new Instruction(operations, cycleFun, label);
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public InstructionBuilder label(String label) {
        setLabel(label);
        return this;
    }

    public InstructionBuilder load(int value) {
        operations.add((registers, addressSpace, accumulator, context, interruptManager) -> value);
        return this;
    }

    public InstructionBuilder loadBytes(int bytes) {
        operations.add(new Operation() {
            @Override
            public int execute(Registers registers, AddressSpace addressSpace, int accumulator, Context context, InterruptManager interruptManager) {
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
            public int execute(Registers registers, AddressSpace addressSpace, int accumulator, Context context, InterruptManager interruptManager) {
                registers.set(reg, accumulator);
                return accumulator;
            }
        });
        return this;
    }

    public InstructionBuilder storeRegAddressAccumulator(RegEnum reg) {
        operations.add(new Operation() {
            @Override
            public int execute(Registers registers, AddressSpace addressSpace, int accumulator, Context context, InterruptManager interruptManager) {
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
            public int execute(Registers registers, AddressSpace addressSpace, int accumulator, Context context, InterruptManager interruptManager) {
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
            public int execute(Registers registers, AddressSpace addressSpace, int accumulator, Context context, InterruptManager interruptManager) {
                return registers.get(reg);
            }
        });
        return this;
    }

    public InstructionBuilder loadAddress() {
        operations.add(new Operation() {
            @Override
            public int execute(Registers registers, AddressSpace addressSpace, int accumulator, Context context, InterruptManager interruptManager) {
                return addressSpace.get(accumulator) & 0xFF;
            }
        });
        return this;
    }

    public InstructionBuilder add(Integer value) {
        operations.add(new Operation() {
            @Override
            public int execute(Registers registers, AddressSpace addressSpace, int accumulator, Context context, InterruptManager interruptManager) {
                return (accumulator + value) & 0xFFFF;
            }
        });
        return this;
    }

    public InstructionBuilder incrementReg(RegEnum reg) {
        operations.add(new Operation() {
            @Override
            public int execute(Registers registers, AddressSpace addressSpace, int accumulator, Context context, InterruptManager interruptManager) {
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
            public int execute(Registers registers, AddressSpace addressSpace, int accumulator, Context context, InterruptManager interruptManager) {
                registers.set(reg, registers.get(reg) - 1);
                return accumulator;
            }
        });
        return this;
    }

    public InstructionBuilder toSigned() {
        operations.add(new Operation() {
            @Override
            public int execute(Registers registers, AddressSpace addressSpace, int accumulator, Context context, InterruptManager interruptManager) {
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
            public int execute(Registers registers, AddressSpace addressSpace, int accumulator, Context context, InterruptManager interruptManager) {
                var newSP = registers.getSP() + accumulator;

                var flags = registers.getFlags();
                flags.setZFlag(false);
                flags.setNFlag(false);
                if (accumulator >= 0) {
                    flags.setHFlag(((registers.getSP() & 0xF) + (accumulator & 0xF)) > 0xF);
                    flags.setCFlag(((registers.getSP() & 0xFF) + (accumulator)) > 0xFF);
                } else {
                    flags.setHFlag((newSP & 0xF) <= (registers.getSP() & 0xF));
                    flags.setCFlag((newSP & 0xFF) <= (registers.getSP() & 0xFF));
                }

                return newSP;
            }
        });
        return this;
    }

    private void push(int value, Registers registers, AddressSpace addressSpace) {
        addressSpace.set(registers.decSP(), BitUtils.getHighByte(value));
        addressSpace.set(registers.decSP(), BitUtils.getLowByte(value));
    }

    private int pop(Registers registers, AddressSpace addressSpace) {
        var lo = addressSpace.get(registers.incSP());
        var hi = addressSpace.get(registers.incSP());

        return (hi << 8 | lo) & 0xFFFF;
    }

    public InstructionBuilder push(RegEnum reg) {
        operations.add(new Operation() {
            @Override
            public int execute(Registers registers, AddressSpace addressSpace, int accumulator, Context context, InterruptManager interruptManager) {
                var value = registers.get(reg);
                push(value, registers, addressSpace);
                return value;
            }
        });
        return this;
    }

    public InstructionBuilder pop(RegEnum reg) {
        operations.add(new Operation() {
            @Override
            public int execute(Registers registers, AddressSpace addressSpace, int accumulator, Context context, InterruptManager interruptManager) {
                var value = pop(registers, addressSpace);
                registers.set(reg, value);
                return value;
            }
        });
        return this;
    }

    public InstructionBuilder add(RegEnum reg, boolean addCarry, boolean isSigned) {
        operations.add(new Operation() {
            @Override
            public int execute(Registers registers, AddressSpace addressSpace, int accumulator, Context context, InterruptManager interruptManager) {
                int carry = 0;
                var flags = registers.getFlags();
                final var regVal = registers.get(reg);

                if (addCarry) {
                    carry = flags.isCFlag() ? 1 : 0;
                }
                var newRegVal = regVal + accumulator;

                switch (reg.size) {
                    case RegEnum.SINGLE:
                        newRegVal += carry;

                        flags.setZFlag((newRegVal & 0xFF) == 0);
                        flags.setNFlag(false);
                        flags.setHFlag(((regVal & 0xF) + (accumulator & 0xF) + carry) > 0xF);
                        flags.setCFlag(((regVal & 0xFF) + accumulator + carry) > 0xFF);

                        return newRegVal & 0xFF;
                    case RegEnum.DOUBLE:
                        flags.setNFlag(false);

                        if (isSigned) {
                            flags.setHFlag(((regVal & 0xF) + (accumulator & 0xF)) > 0xF);
                            flags.setCFlag(((regVal & 0xFF) + accumulator) > 0xFF);
                        } else {
                            flags.setHFlag(((regVal & 0xFFF) + (accumulator & 0xFFF)) > 0xFFF);
                            flags.setCFlag(((regVal & 0xFFFF) + accumulator) > 0xFFFF);
                        }

                        return newRegVal & 0xFFFF;
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
            public int execute(Registers registers, AddressSpace addressSpace, int accumulator, Context context, InterruptManager interruptManager) {
                var carry = subCarry && registers.getFlags().isCFlag() ? 1 : 0;
                return sub(accumulator, registers.get(reg), carry, registers);
            }
        });
        return this;
    }

    public InstructionBuilder subFrom(RegEnum reg, boolean subCarry) {
        operations.add(new Operation() {
            @Override
            public int execute(Registers registers, AddressSpace addressSpace, int accumulator, Context context, InterruptManager interruptManager) {
                var carry = subCarry && registers.getFlags().isCFlag() ? 1 : 0;
                return sub(registers.get(reg), accumulator, carry, registers);
            }
        });
        return this;
    }

    public InstructionBuilder cp(RegEnum reg) {
        operations.add(new Operation() {
            @Override
            public int execute(Registers registers, AddressSpace addressSpace, int accumulator, Context context, InterruptManager interruptManager) {
                return sub(registers.get(reg), accumulator, 0, registers);
            }
        });
        return this;
    }

    private static int sub(int byte1, int byte2, int carry, Registers registers) {
        var result = byte1 - byte2 - carry;

        var flags = registers.getFlags();
        flags.setZFlag((result & 0xFF) == 0);
        flags.setNFlag(true);
        flags.setHFlag(((byte1 ^ byte2 ^ (result & 0xFF)) & 0x10) != 0);
        flags.setCFlag(result < 0);

        return result & 0xFF;

    }

    public InstructionBuilder and(RegEnum reg) {
        operations.add(new Operation() {
            @Override
            public int execute(Registers registers, AddressSpace addressSpace, int accumulator, Context context, InterruptManager interruptManager) {
                var output = (registers.get(reg) & accumulator) & 0xFF;

                var flags = registers.getFlags();
                flags.setZFlag(output == 0);
                flags.setNFlag(false);
                flags.setHFlag(true);
                flags.setCFlag(false);

                return output;
            }
        });
        return this;
    }

    public InstructionBuilder or(RegEnum reg) {
        operations.add(new Operation() {
            @Override
            public int execute(Registers registers, AddressSpace addressSpace, int accumulator, Context context, InterruptManager interruptManager) {
                var output = (registers.get(reg) | accumulator) & 0xFF;

                var flags = registers.getFlags();
                flags.setZFlag(output == 0);
                flags.setNFlag(false);
                flags.setHFlag(false);
                flags.setCFlag(false);

                return output;
            }
        });
        return this;
    }

    public InstructionBuilder xor(RegEnum reg) {
        operations.add(new Operation() {
            @Override
            public int execute(Registers registers, AddressSpace addressSpace, int accumulator, Context context, InterruptManager interruptManager) {
                var output = (registers.get(reg) ^ accumulator) & 0xFF;

                var flags = registers.getFlags();
                flags.setZFlag(output == 0);
                flags.setNFlag(false);
                flags.setHFlag(false);
                flags.setCFlag(false);

                return output;
            }
        });
        return this;
    }

    public InstructionBuilder inc(int size) {
        operations.add(new Operation() {
            @Override
            public int execute(Registers registers, AddressSpace addressSpace, int accumulator, Context context, InterruptManager interruptManager) {
                switch (size) {
                    case RegEnum.SINGLE:
                        var output = (accumulator + 1) & 0xFF;

                        var flags = registers.getFlags();
                        flags.setZFlag(output == 0);
                        flags.setNFlag(false);
                        flags.setHFlag(((accumulator & 0xF) + 1) > 0xF);

                        return output;
                    case RegEnum.DOUBLE:
                        return (accumulator + 1) & 0xFFFF;
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
            public int execute(Registers registers, AddressSpace addressSpace, int accumulator, Context context, InterruptManager interruptManager) {
                switch (size) {
                    case RegEnum.SINGLE:
                        var output = (accumulator - 1) & 0xFF;

                        var flags = registers.getFlags();
                        flags.setZFlag(output == 0);
                        flags.setNFlag(true);
                        flags.setHFlag(((accumulator ^ 1 ^ (output & 0xFF)) & 0x10) != 0);

                        return output;
                    case RegEnum.DOUBLE:
                        return (accumulator - 1) & 0xFFFF;
                    default:
                        throw new IllegalStateException(String.format("Illegal register size %d", size));

                }
            }
        });
        return this;
    }

    public InstructionBuilder swap() {
        operations.add(new Operation() {
            @Override
            public int execute(Registers registers, AddressSpace addressSpace, int accumulator, Context context, InterruptManager interruptManager) {
                var hi = (accumulator & 0xF) << 4;
                var output = ((accumulator & 0xF0) >> 4) | hi;

                var flags = registers.getFlags();
                flags.setZFlag(output == 0);
                flags.setNFlag(false);
                flags.setHFlag(false);
                flags.setCFlag(false);

                return output;
            }
        });
        return this;
    }

    public InstructionBuilder rlc() {
        operations.add(new Operation() {
            @Override
            public int execute(Registers registers, AddressSpace addressSpace, int accumulator, Context context, InterruptManager interruptManager) {
                isByte(accumulator);
                var carry = ((accumulator >>> 7) & 0x1);
                var output = ((accumulator << 1) & 0xFF) | carry;

                var flags = registers.getFlags();
                flags.setZFlag(output == 0);
                flags.setNFlag(false);
                flags.setHFlag(false);
                flags.setCFlag(carry == 1);

                return output;
            }
        });
        return this;
    }

    public InstructionBuilder rl() {
        operations.add(new Operation() {
            @Override
            public int execute(Registers registers, AddressSpace addressSpace, int accumulator, Context context, InterruptManager interruptManager) {
                var flags = registers.getFlags();

                isByte(accumulator);
                var carry = ((accumulator >>> 7) & 0x1);
                var output = ((accumulator << 1) & 0xFF) | (flags.isCFlag() ? 1 : 0);

                flags.setZFlag(output == 0);
                flags.setNFlag(false);
                flags.setHFlag(false);
                flags.setCFlag(carry == 1);

                return output;
            }
        });
        return this;
    }

    public InstructionBuilder rrc() {
        operations.add(new Operation() {
            @Override
            public int execute(Registers registers, AddressSpace addressSpace, int accumulator, Context context, InterruptManager interruptManager) {
                isByte(accumulator);
                var carry = accumulator & 0x1;
                var output = ((accumulator >> 1) & 0xFF) | (carry << 7);

                var flags = registers.getFlags();
                flags.setZFlag(output == 0);
                flags.setNFlag(false);
                flags.setHFlag(false);
                flags.setCFlag(carry == 1);

                return output;
            }
        });
        return this;
    }

    public InstructionBuilder rr() {
        operations.add(new Operation() {
            @Override
            public int execute(Registers registers, AddressSpace addressSpace, int accumulator, Context context, InterruptManager interruptManager) {
                var flags = registers.getFlags();

                isByte(accumulator);
                var carry = accumulator & 0x1;
                var output = ((accumulator >> 1) & 0xFF) | ((flags.isCFlag() ? 1 : 0) << 7);

                flags.setZFlag(output == 0);
                flags.setNFlag(false);
                flags.setHFlag(false);
                flags.setCFlag(carry == 1);

                return output;
            }
        });
        return this;
    }

    public InstructionBuilder sla() {
        operations.add(new Operation() {
            @Override
            public int execute(Registers registers, AddressSpace addressSpace, int accumulator, Context context, InterruptManager interruptManager) {
                var flags = registers.getFlags();

                isByte(accumulator);
                var carry = ((accumulator >>> 7) & 0x1);
                var output = (accumulator << 1) & 0xFE;

                flags.setZFlag(output == 0);
                flags.setNFlag(false);
                flags.setHFlag(false);
                flags.setCFlag(carry == 1);

                return output;
            }
        });
        return this;
    }

    public InstructionBuilder sra() {
        operations.add(new Operation() {
            @Override
            public int execute(Registers registers, AddressSpace addressSpace, int accumulator, Context context, InterruptManager interruptManager) {
                var flags = registers.getFlags();

                isByte(accumulator);
                var carry = accumulator & 0x1;
                var msb = (accumulator & 0x80) >> 7;
                var output = ((accumulator >> 1) & 0x7F) | (msb << 7);

                flags.setZFlag(output == 0);
                flags.setNFlag(false);
                flags.setHFlag(false);
                flags.setCFlag(carry == 1);

                return output;
            }
        });
        return this;
    }

    public InstructionBuilder srl() {
        operations.add(new Operation() {
            @Override
            public int execute(Registers registers, AddressSpace addressSpace, int accumulator, Context context, InterruptManager interruptManager) {
                var flags = registers.getFlags();

                isByte(accumulator);
                var carry = accumulator & 0x1;
                var output = (accumulator >> 1) & 0x7F;

                flags.setZFlag(output == 0);
                flags.setNFlag(false);
                flags.setHFlag(false);
                flags.setCFlag(carry == 1);

                return output;
            }
        });
        return this;
    }

    public InstructionBuilder bitTest(int bit) {
        operations.add(new Operation() {
            @Override
            public int execute(Registers registers, AddressSpace addressSpace, int accumulator, Context context, InterruptManager interruptManager) {
                var flags = registers.getFlags();
                if (bit < 0 || bit > 7) {
                    throw new IllegalArgumentException(String.format("Bit %d out of range 0-7!", accumulator));
                }

                flags.setZFlag(!getByteBit(accumulator, bit));
                flags.setNFlag(false);
                flags.setHFlag(true);

                return accumulator;
            }
        });
        return this;
    }

    public InstructionBuilder setBit(int bit, boolean value) {
        operations.add(new Operation() {
            @Override
            public int execute(Registers registers, AddressSpace addressSpace, int accumulator, Context context, InterruptManager interruptManager) {
                if (bit < 0 || bit > 7) {
                    throw new IllegalArgumentException(String.format("Bit %d out of range 0-7!", accumulator));
                }

                accumulator = setByteBit(accumulator, bit, value);

                return accumulator;
            }
        });
        return this;
    }

    public InstructionBuilder setContext() {
        operations.add(new Operation() {
            @Override
            public int execute(Registers registers, AddressSpace addressSpace, int accumulator, Context context, InterruptManager interruptManager) {
                context.set(accumulator);
                return accumulator;
            }
        });
        return this;
    }

    private boolean checkCondition(JumpCondition condition, Registers registers, Context context) {
        boolean shouldJump = true;
        switch (condition) {
            case NZ:
                shouldJump = !registers.getFlags().isZFlag();
                break;
            case Z:
                shouldJump = registers.getFlags().isZFlag();
                break;
            case NC:
                shouldJump = !registers.getFlags().isCFlag();
                break;
            case C:
                shouldJump = registers.getFlags().isCFlag();
                break;
            default:
                throw new IllegalStateException(String.format("Illegal Jump Condition %s", condition));
        }
        context.setConditionTrue(shouldJump);

        return shouldJump;
    }

    public InstructionBuilder jp(JumpCondition conditionOpt, boolean jr) {
        operations.add(new Operation() {
            @Override
            public int execute(Registers registers, AddressSpace addressSpace, int accumulator, Context context, InterruptManager interruptManager) {
                boolean shouldJump = true;

                if (conditionOpt != null) {
                    shouldJump = checkCondition(conditionOpt, registers, context);
                }

                if (shouldJump) {
                    if (jr) {
                        registers.setPC(registers.getPC() + accumulator);
                    } else {
                        registers.setPC(accumulator);
                    }
                }

                return accumulator;
            }
        });
        return this;
    }

    public InstructionBuilder call(JumpCondition conditionOpt) {
        operations.add(new Operation() {
            @Override
            public int execute(Registers registers, AddressSpace addressSpace, int accumulator, Context context, InterruptManager interruptManager) {
                boolean shouldCall = true;

                if (conditionOpt != null) {
                    shouldCall = checkCondition(conditionOpt, registers, context);
                }

                if (shouldCall) {
                    push(registers.get(RegEnum.PC), registers, addressSpace);
                    registers.setPC(accumulator);
                }

                return accumulator;
            }
        });
        return this;
    }

    public InstructionBuilder ret(JumpCondition conditionOpt) {
        operations.add(new Operation() {
            @Override
            public int execute(Registers registers, AddressSpace addressSpace, int accumulator, Context context, InterruptManager interruptManager) {
                boolean shouldCall = true;

                if (conditionOpt != null) {
                    shouldCall = checkCondition(conditionOpt, registers, context);
                }

                if (shouldCall) {
                    registers.setPC(pop(registers, addressSpace));
                }

                return accumulator;
            }
        });
        return this;
    }

    public InstructionBuilder daa() {
        operations.add(new Operation() {
            @Override
            public int execute(Registers registers, AddressSpace addressSpace, int accumulator, Context context, InterruptManager interruptManager) {
                var flags = registers.getFlags();
                var regA = registers.getA();

                int correction = 0;

                if (!flags.isNFlag()) {
                    if (flags.isCFlag() || regA > 0x99) {
                        correction += 0x60;
                        flags.setCFlag(true);
                    }
                    if (flags.isHFlag() || (regA & 0x0F) > 0x09) {
                        correction += 0x06;
                    }
                } else {
                    if (flags.isCFlag()) {
                        correction -= 0x60;
                        flags.setCFlag(true);
                    }
                    if (flags.isHFlag()) {
                        correction -= 0x06;
                    }
                }
                // If it fails, it probably needs 0xFF
                registers.setA(regA + correction);

                flags.setZFlag(registers.getA() == 0);
                flags.setHFlag(false);

                return registers.getA();
            }
        });
        return this;
    }

    public InstructionBuilder cpl() {
        operations.add(new Operation() {
            @Override
            public int execute(Registers registers, AddressSpace addressSpace, int accumulator, Context context, InterruptManager interruptManager) {
                var flags = registers.getFlags();
                var regA = registers.getA();
                registers.setA(~regA);

                flags.setNFlag(true);
                flags.setHFlag(true);

                return registers.getA();
            }
        });
        return this;
    }

    public InstructionBuilder ccf() {
        operations.add(new Operation() {
            @Override
            public int execute(Registers registers, AddressSpace addressSpace, int accumulator, Context context, InterruptManager interruptManager) {
                var flags = registers.getFlags();

                flags.setCFlag(!flags.isCFlag());
                flags.setNFlag(false);
                flags.setHFlag(false);

                return flags.isCFlag() ? 1 : 0;
            }
        });
        return this;
    }

    public InstructionBuilder setCarry() {
        operations.add(new Operation() {
            @Override
            public int execute(Registers registers, AddressSpace addressSpace, int accumulator, Context context, InterruptManager interruptManager) {
                var flags = registers.getFlags();

                flags.setCFlag(true);
                flags.setNFlag(false);
                flags.setHFlag(false);

                return 1;
            }
        });
        return this;
    }

    public InstructionBuilder halt() {
        operations.add(new Operation() {
            @Override
            public int execute(Registers registers, AddressSpace addressSpace, int accumulator, Context context, InterruptManager interruptManager) {
                interruptManager.setHalted(true);

                return 1;
            }
        });
        return this;
    }

    public InstructionBuilder stop() {
        operations.add(new Operation() {
            @Override
            public int execute(Registers registers, AddressSpace addressSpace, int accumulator, Context context, InterruptManager interruptManager) {
                interruptManager.setStopped(true);

                return 1;
            }
        });
        return this;
    }

    public InstructionBuilder disableInterrupts() {
        setShouldDisableInterrupts(true);
        return this;
    }

    public InstructionBuilder enableInterrupts() {
        setShouldDisableInterrupts(false);
        return this;
    }

    private void setShouldDisableInterrupts(boolean value) {
        operations.add(new Operation() {
            @Override
            public int execute(Registers registers, AddressSpace addressSpace, int accumulator, Context context, InterruptManager interruptManager) {
                interruptManager.setShouldDisableInterrupts(value);

                return value ? 1 : 0;
            }
        });
    }


}


