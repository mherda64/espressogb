package cpu;

import util.Pair;

import java.util.*;

public class Instructions {

    public static final Map<Integer, Instruction> instructions;

    public static Instruction get(int opcode) {
        return instructions.get(opcode);
    }

    private static void put(int opcode, Instruction instruction) {
        if (instructions.containsKey(opcode))
            throw new IllegalStateException(String.format("Opcode %02X already exists!", opcode));
        instructions.put(opcode, instruction);
    }

    static {
        instructions = new HashMap<>();

        /*
         * 8-Bit Load Instructions
         * LD nn, n
         * */
        Arrays.asList(
                new Opcode<>(0x06, RegEnum.B, 2),
                new Opcode<>(0x0E, RegEnum.C, 2),
                new Opcode<>(0x16, RegEnum.D, 2),
                new Opcode<>(0x1E, RegEnum.E, 2),
                new Opcode<>(0x26, RegEnum.H, 2),
                new Opcode<>(0x2E, RegEnum.L, 2)
        ).forEach(opcode -> {
                    put(opcode.getOpcode(),
                            Instruction.builder()
                                    .loadByte()
                                    .storeByte(opcode.getTarget().get())
                                    .build(opcode.getCycles()));
                }
        );

        /*
         * 8-Bit Load Instructions
         * LD r1, r2
         * */

        /*
        LD A, r2
         */
        Arrays.asList(
                new Opcode<>(0x7F, RegEnum.A, 1),
                new Opcode<>(0x78, RegEnum.B, 1),
                new Opcode<>(0x79, RegEnum.C, 1),
                new Opcode<>(0x7A, RegEnum.D, 1),
                new Opcode<>(0x7B, RegEnum.E, 1),
                new Opcode<>(0x7C, RegEnum.H, 1),
                new Opcode<>(0x7D, RegEnum.L, 1)
        ).forEach(opcode -> {
                    put(opcode.getOpcode(),
                            Instruction.builder()
                                    .loadReg(opcode.getTarget().get())
                                    .storeByte(RegEnum.A)
                                    .build(opcode.getCycles()));
                }
        );

        /*
        LD B, r2
         */
        Arrays.asList(
                new Opcode<>(0x40, RegEnum.B, 1),
                new Opcode<>(0x41, RegEnum.C, 1),
                new Opcode<>(0x42, RegEnum.D, 1),
                new Opcode<>(0x43, RegEnum.E, 1),
                new Opcode<>(0x44, RegEnum.H, 1),
                new Opcode<>(0x45, RegEnum.L, 1)
        ).forEach(opcode -> {
                    put(opcode.getOpcode(),
                            Instruction.builder()
                                    .loadReg(opcode.getTarget().get())
                                    .storeByte(RegEnum.B)
                                    .build(opcode.getCycles()));
                }
        );

        /*
        LD C, r2
         */
        Arrays.asList(
                new Opcode<>(0x48, RegEnum.B, 1),
                new Opcode<>(0x49, RegEnum.C, 1),
                new Opcode<>(0x4A, RegEnum.D, 1),
                new Opcode<>(0x4B, RegEnum.E, 1),
                new Opcode<>(0x4C, RegEnum.H, 1),
                new Opcode<>(0x4D, RegEnum.L, 1)
        ).forEach(opcode -> {
                    put(opcode.getOpcode(),
                            Instruction.builder()
                                    .loadReg(opcode.getTarget().get())
                                    .storeByte(RegEnum.C)
                                    .build(opcode.getCycles()));
                }
        );

        /*
        LD D, r2
         */
        Arrays.asList(
                new Opcode<>(0x50, RegEnum.B, 1),
                new Opcode<>(0x51, RegEnum.C, 1),
                new Opcode<>(0x52, RegEnum.D, 1),
                new Opcode<>(0x53, RegEnum.E, 1),
                new Opcode<>(0x54, RegEnum.H, 1),
                new Opcode<>(0x55, RegEnum.L, 1)
        ).forEach(opcode -> {
                    put(opcode.getOpcode(),
                            Instruction.builder()
                                    .loadReg(opcode.getTarget().get())
                                    .storeByte(RegEnum.D)
                                    .build(opcode.getCycles()));
                }
        );

        /*
        LD E, r2
         */
        Arrays.asList(
                new Opcode<>(0x58, RegEnum.B, 1),
                new Opcode<>(0x59, RegEnum.C, 1),
                new Opcode<>(0x5A, RegEnum.D, 1),
                new Opcode<>(0x5B, RegEnum.E, 1),
                new Opcode<>(0x5C, RegEnum.H, 1),
                new Opcode<>(0x5D, RegEnum.L, 1)
        ).forEach(opcode -> {
                    put(opcode.getOpcode(),
                            Instruction.builder()
                                    .loadReg(opcode.getTarget().get())
                                    .storeByte(RegEnum.E)
                                    .build(opcode.getCycles()));
                }
        );

        /*
        LD H, r2
         */
        Arrays.asList(
                new Opcode<>(0x60, RegEnum.B, 1),
                new Opcode<>(0x61, RegEnum.C, 1),
                new Opcode<>(0x62, RegEnum.D, 1),
                new Opcode<>(0x63, RegEnum.E, 1),
                new Opcode<>(0x64, RegEnum.H, 1),
                new Opcode<>(0x65, RegEnum.L, 1)
        ).forEach(opcode -> {
                    put(opcode.getOpcode(),
                            Instruction.builder()
                                    .loadReg(opcode.getTarget().get())
                                    .storeByte(RegEnum.H)
                                    .build(opcode.getCycles()));
                }
        );

        /*
        LD L, r2
         */
        Arrays.asList(
                new Opcode<>(0x68, RegEnum.B, 1),
                new Opcode<>(0x69, RegEnum.C, 1),
                new Opcode<>(0x6A, RegEnum.D, 1),
                new Opcode<>(0x6B, RegEnum.E, 1),
                new Opcode<>(0x6C, RegEnum.H, 1),
                new Opcode<>(0x6D, RegEnum.L, 1)
        ).forEach(opcode -> {
                    put(opcode.getOpcode(),
                            Instruction.builder()
                                    .loadReg(opcode.getTarget().get())
                                    .storeByte(RegEnum.L)
                                    .build(opcode.getCycles()));
                }
        );

        /*
        LD reg, (HL)
         */
        Arrays.asList(
                new Opcode<>(0x7E, RegEnum.A, 2),
                new Opcode<>(0x46, RegEnum.B, 2),
                new Opcode<>(0x4E, RegEnum.C, 2),
                new Opcode<>(0x56, RegEnum.D, 2),
                new Opcode<>(0x5E, RegEnum.E, 2),
                new Opcode<>(0x66, RegEnum.H, 2),
                new Opcode<>(0x6E, RegEnum.L, 2)
        ).forEach(opcode -> {
                    put(opcode.getOpcode(),
                            Instruction.builder()
                                    .loadAddressFromReg(RegEnum.HL)
                                    .storeByte(opcode.getTarget().get())
                                    .build(opcode.getCycles()));
                }
        );

    }

    private Instructions() {
    }
}
