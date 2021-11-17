package cpu.instructions;

import cpu.RegEnum;

import java.util.Map;
import java.util.stream.Stream;

public class ByteLoadInstructions {

    private ByteLoadInstructions() {}

    private static void put(int opcode, Map<Integer, Instruction> instructions, Instruction instruction) {
        if (instructions.containsKey(opcode))
            throw new IllegalStateException(String.format("Opcode %02X already exists!", opcode));
        instructions.put(opcode, instruction);
    }

    public static void add(Map<Integer, Instruction> instructions) {
        /*
         * 8-Bit Load Instructions
         * LD nn, n
         */
        Stream.of(
                new Opcode<>(0x3E, RegEnum.A, 2),
                new Opcode<>(0x06, RegEnum.B, 2),
                new Opcode<>(0x0E, RegEnum.C, 2),
                new Opcode<>(0x16, RegEnum.D, 2),
                new Opcode<>(0x1E, RegEnum.E, 2),
                new Opcode<>(0x26, RegEnum.H, 2),
                new Opcode<>(0x2E, RegEnum.L, 2)
        ).forEach(opcode ->
                put(opcode.getOpcode(),
                        instructions,
                        Instruction.builder()
                                .loadBytes(RegEnum.SINGLE)
                                .store(opcode.getTarget().get())
                                .build(opcode.getCycles()))
        );

        /*
         * 8-Bit Load Instructions
         * LD r1, r2
         */

        /*
        LD A, r2
         */
        Stream.of(
                new Opcode<>(0x7F, RegEnum.A, 1),
                new Opcode<>(0x78, RegEnum.B, 1),
                new Opcode<>(0x79, RegEnum.C, 1),
                new Opcode<>(0x7A, RegEnum.D, 1),
                new Opcode<>(0x7B, RegEnum.E, 1),
                new Opcode<>(0x7C, RegEnum.H, 1),
                new Opcode<>(0x7D, RegEnum.L, 1)
        ).forEach(opcode ->
                put(opcode.getOpcode(),
                        instructions,
                        Instruction.builder()
                                .loadReg(opcode.getTarget().get())
                                .store(RegEnum.A)
                                .build(opcode.getCycles()))
        );

        /*
        LD B, r2
         */
        Stream.of(
                new Opcode<>(0x40, RegEnum.B, 1),
                new Opcode<>(0x41, RegEnum.C, 1),
                new Opcode<>(0x42, RegEnum.D, 1),
                new Opcode<>(0x43, RegEnum.E, 1),
                new Opcode<>(0x44, RegEnum.H, 1),
                new Opcode<>(0x45, RegEnum.L, 1)
        ).forEach(opcode ->
                put(opcode.getOpcode(),
                        instructions,
                        Instruction.builder()
                                .loadReg(opcode.getTarget().get())
                                .store(RegEnum.B)
                                .build(opcode.getCycles()))
        );

        /*
        LD C, r2
         */
        Stream.of(
                new Opcode<>(0x48, RegEnum.B, 1),
                new Opcode<>(0x49, RegEnum.C, 1),
                new Opcode<>(0x4A, RegEnum.D, 1),
                new Opcode<>(0x4B, RegEnum.E, 1),
                new Opcode<>(0x4C, RegEnum.H, 1),
                new Opcode<>(0x4D, RegEnum.L, 1)
        ).forEach(opcode ->
                put(opcode.getOpcode(),
                        instructions,
                        Instruction.builder()
                                .loadReg(opcode.getTarget().get())
                                .store(RegEnum.C)
                                .build(opcode.getCycles()))
        );

        /*
        LD D, r2
         */
        Stream.of(
                new Opcode<>(0x50, RegEnum.B, 1),
                new Opcode<>(0x51, RegEnum.C, 1),
                new Opcode<>(0x52, RegEnum.D, 1),
                new Opcode<>(0x53, RegEnum.E, 1),
                new Opcode<>(0x54, RegEnum.H, 1),
                new Opcode<>(0x55, RegEnum.L, 1)
        ).forEach(opcode ->
                put(opcode.getOpcode(),
                        instructions,
                        Instruction.builder()
                                .loadReg(opcode.getTarget().get())
                                .store(RegEnum.D)
                                .build(opcode.getCycles()))
        );

        /*
        LD E, r2
         */
        Stream.of(
                new Opcode<>(0x58, RegEnum.B, 1),
                new Opcode<>(0x59, RegEnum.C, 1),
                new Opcode<>(0x5A, RegEnum.D, 1),
                new Opcode<>(0x5B, RegEnum.E, 1),
                new Opcode<>(0x5C, RegEnum.H, 1),
                new Opcode<>(0x5D, RegEnum.L, 1)
        ).forEach(opcode ->
                put(opcode.getOpcode(),
                        instructions,
                        Instruction.builder()
                                .loadReg(opcode.getTarget().get())
                                .store(RegEnum.E)
                                .build(opcode.getCycles()))
        );

        /*
        LD H, r2
         */
        Stream.of(
                new Opcode<>(0x60, RegEnum.B, 1),
                new Opcode<>(0x61, RegEnum.C, 1),
                new Opcode<>(0x62, RegEnum.D, 1),
                new Opcode<>(0x63, RegEnum.E, 1),
                new Opcode<>(0x64, RegEnum.H, 1),
                new Opcode<>(0x65, RegEnum.L, 1)
        ).forEach(opcode ->
                put(opcode.getOpcode(),
                        instructions,
                        Instruction.builder()
                                .loadReg(opcode.getTarget().get())
                                .store(RegEnum.H)
                                .build(opcode.getCycles()))
        );

        /*
        LD L, r2
         */
        Stream.of(
                new Opcode<>(0x68, RegEnum.B, 1),
                new Opcode<>(0x69, RegEnum.C, 1),
                new Opcode<>(0x6A, RegEnum.D, 1),
                new Opcode<>(0x6B, RegEnum.E, 1),
                new Opcode<>(0x6C, RegEnum.H, 1),
                new Opcode<>(0x6D, RegEnum.L, 1)
        ).forEach(opcode ->
                put(opcode.getOpcode(),
                        instructions,
                        Instruction.builder()
                                .loadReg(opcode.getTarget().get())
                                .store(RegEnum.L)
                                .build(opcode.getCycles()))
        );

        /*
        LD reg, (HL)
         */
        Stream.of(
                new Opcode<>(0x7E, RegEnum.A, 2),
                new Opcode<>(0x46, RegEnum.B, 2),
                new Opcode<>(0x4E, RegEnum.C, 2),
                new Opcode<>(0x56, RegEnum.D, 2),
                new Opcode<>(0x5E, RegEnum.E, 2),
                new Opcode<>(0x66, RegEnum.H, 2),
                new Opcode<>(0x6E, RegEnum.L, 2)
        ).forEach(opcode ->
                put(opcode.getOpcode(),
                        instructions,
                        Instruction.builder()
                                .loadReg(RegEnum.HL)
                                .loadAddress()
                                .store(opcode.getTarget().get())
                                .build(opcode.getCycles()))
        );

        /*
        LD (HL), reg
         */
        Stream.of(
                new Opcode<>(0x70, RegEnum.B, 2),
                new Opcode<>(0x71, RegEnum.C, 2),
                new Opcode<>(0x72, RegEnum.D, 2),
                new Opcode<>(0x73, RegEnum.E, 2),
                new Opcode<>(0x74, RegEnum.H, 2),
                new Opcode<>(0x75, RegEnum.L, 2)
        ).forEach(opcode ->
                put(opcode.getOpcode(),
                        instructions,
                        Instruction.builder()
                                .loadReg(opcode.getTarget().get())
                                .storeRegAddressAccumulator(RegEnum.HL)
                                .build(opcode.getCycles()))
        );

        /*
        LD (HL),n
         */
        put(0x36,
                instructions,
                Instruction.builder()
                        .loadBytes(RegEnum.SINGLE)
                        .storeRegAddressAccumulator(RegEnum.HL)
                        .build(3)
        );


        /*
        LD A, (BC, DE)
         */
        Stream.of(
                new Opcode<>(0x0A, RegEnum.BC, 2),
                new Opcode<>(0x1A, RegEnum.DE, 2)
        ).forEach(opcode ->
                put(opcode.getOpcode(),
                        instructions,
                        Instruction.builder()
                                .loadReg(opcode.getTarget().get())
                                .loadAddress()
                                .store(RegEnum.A)
                                .build(opcode.getCycles()))
        );

        /*
        LD A, (nn)
         */
        put(0xFA,
                instructions,
                Instruction.builder()
                        .loadBytes(RegEnum.DOUBLE)
                        .loadAddress()
                        .store(RegEnum.A)
                        .build(4)
        );

        /*
        LD reg, A
         */
        Stream.of(
                new Opcode<>(0x47, RegEnum.B, 1),
                new Opcode<>(0x4F, RegEnum.C, 1),
                new Opcode<>(0x57, RegEnum.D, 1),
                new Opcode<>(0x5F, RegEnum.E, 1),
                new Opcode<>(0x67, RegEnum.H, 1),
                new Opcode<>(0x6F, RegEnum.L, 1)
        ).forEach(opcode ->
                put(opcode.getOpcode(),
                        instructions,
                        Instruction.builder()
                                .loadReg(RegEnum.A)
                                .store(opcode.getTarget().get())
                                .build(opcode.getCycles()))
        );

        /*
        LD (reg), A
         */
        Stream.of(
                new Opcode<>(0x02, RegEnum.BC, 2),
                new Opcode<>(0x12, RegEnum.DE, 2),
                new Opcode<>(0x77, RegEnum.HL, 2)
        ).forEach(opcode ->
                put(opcode.getOpcode(),
                        instructions,
                        Instruction.builder()
                                .loadReg(RegEnum.A)
                                .storeRegAddressAccumulator(opcode.getTarget().get())
                                .build(opcode.getCycles()))
        );

        /*
        LD (nn), A
         */
        put(0xEA,
                instructions,
                Instruction.builder()
                        .loadBytes(RegEnum.DOUBLE)
                        .storeAccumulatorAddressReg(RegEnum.A)
                        .build(4)
        );

        /*
        LD A, (C)
         */
        put(0xF2,
                instructions,
                Instruction.builder()
                        .loadReg(RegEnum.C)
                        .add(0xFF00)
                        .loadAddress()
                        .store(RegEnum.A)
                        .build(2)
        );

        /*
         * LD (C), A
         * */
        put(0xE2,
                instructions,
                Instruction.builder()
                        .loadReg(RegEnum.C)
                        .add(0xFF00)
                        .storeAccumulatorAddressReg(RegEnum.A)
                        .build(2)
        );

        /*
         * LD A, (HL-)
         * LDD A, (HL)
         * */
        put(0x3A,
                instructions,
                Instruction.builder()
                        .loadReg(RegEnum.HL)
                        .loadAddress()
                        .store(RegEnum.A)
                        .dec(RegEnum.HL)
                        .build(2)
        );

        /*
         * LD (HL-), A
         * LDD (HL), A
         * */
        put(0x32,
                instructions,
                Instruction.builder()
                        .loadReg(RegEnum.HL)
                        .storeAccumulatorAddressReg(RegEnum.A)
                        .dec(RegEnum.HL)
                        .build(2)
        );

        /*
         * LD A, (HL+)
         * LDI A, (HL)
         * */
        put(0x2A,
                instructions,
                Instruction.builder()
                        .loadReg(RegEnum.HL)
                        .loadAddress()
                        .store(RegEnum.A)
                        .inc(RegEnum.HL)
                        .build(2)
        );

        /*
         * LD (HL+), A
         * LDI (HL), A
         * */
        put(0x22,
                instructions,
                Instruction.builder()
                        .loadReg(RegEnum.HL)
                        .storeAccumulatorAddressReg(RegEnum.A)
                        .inc(RegEnum.HL)
                        .build(2)
        );

        /*
         * LDH (n), A
         * */
        put(0xE0,
                instructions,
                Instruction.builder()
                        .loadBytes(RegEnum.SINGLE)
                        .add(0xFF00)
                        .storeAccumulatorAddressReg(RegEnum.A)
                        .build(3)
        );

        /*
         * LDH A, (n)
         * */
        put(0xF0,
                instructions,
                Instruction.builder()
                        .loadBytes(RegEnum.SINGLE)
                        .add(0xFF00)
                        .loadAddress()
                        .store(RegEnum.A)
                        .build(3)
        );
    }
}
