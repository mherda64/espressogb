package cpu.instruction.appender;

import cpu.RegEnum;
import cpu.instruction.Instruction;
import cpu.instruction.Opcode;

import java.util.Map;
import java.util.stream.Stream;

public class ByteLoadInstructions implements InstructionAppender {

    public void add(Map<Integer, Instruction> instructions) {
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
                                .label(String.format("LD %s, n", opcode.getTarget().get()))
                                .loadBytes(RegEnum.SINGLE)
                                .store(opcode.getTarget().get())
                                .build(opcode.getCyclesFun()))
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
                                .label(String.format("LD A, %s", opcode.getTarget().get()))
                                .loadReg(opcode.getTarget().get())
                                .store(RegEnum.A)
                                .build(opcode.getCyclesFun()))
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
                                .label(String.format("LD B, %s", opcode.getTarget().get()))
                                .loadReg(opcode.getTarget().get())
                                .store(RegEnum.B)
                                .build(opcode.getCyclesFun()))
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
                                .label(String.format("LD C, %s", opcode.getTarget().get()))
                                .loadReg(opcode.getTarget().get())
                                .store(RegEnum.C)
                                .build(opcode.getCyclesFun()))
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
                                .label(String.format("LD D, %s", opcode.getTarget().get()))
                                .loadReg(opcode.getTarget().get())
                                .store(RegEnum.D)
                                .build(opcode.getCyclesFun()))
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
                                .label(String.format("LD E, %s", opcode.getTarget().get()))
                                .loadReg(opcode.getTarget().get())
                                .store(RegEnum.E)
                                .build(opcode.getCyclesFun()))
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
                                .label(String.format("LD H, %s", opcode.getTarget().get()))
                                .loadReg(opcode.getTarget().get())
                                .store(RegEnum.H)
                                .build(opcode.getCyclesFun()))
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
                                .label(String.format("LD L, %s", opcode.getTarget().get()))
                                .loadReg(opcode.getTarget().get())
                                .store(RegEnum.L)
                                .build(opcode.getCyclesFun()))
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
                                .label(String.format("LD %s, (HL)", opcode.getTarget().get()))
                                .loadReg(RegEnum.HL)
                                .loadAddress()
                                .store(opcode.getTarget().get())
                                .build(opcode.getCyclesFun()))
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
                                .label(String.format("LD (HL), %s", opcode.getTarget().get()))
                                .loadReg(opcode.getTarget().get())
                                .storeRegAddressAccumulator(RegEnum.HL)
                                .build(opcode.getCyclesFun()))
        );

        /*
        LD (HL),n
         */
        put(0x36,
                instructions,
                Instruction.builder()
                        .label("LD (HL), n")
                        .loadBytes(RegEnum.SINGLE)
                        .storeRegAddressAccumulator(RegEnum.HL)
                        .build(context -> 3)
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
                                .label(String.format("LD A, (%s)", opcode.getTarget().get()))
                                .loadReg(opcode.getTarget().get())
                                .loadAddress()
                                .store(RegEnum.A)
                                .build(opcode.getCyclesFun()))
        );

        /*
        LD A, (nn)
         */
        put(0xFA,
                instructions,
                Instruction.builder()
                        .label("LD A, (nn)")
                        .loadBytes(RegEnum.DOUBLE)
                        .loadAddress()
                        .store(RegEnum.A)
                        .build(context -> 4)
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
                                .label(String.format("LD %s, A", opcode.getTarget().get()))
                                .loadReg(RegEnum.A)
                                .store(opcode.getTarget().get())
                                .build(opcode.getCyclesFun()))
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
                                .label(String.format("LD (%s), A", opcode.getTarget().get()))
                                .loadReg(RegEnum.A)
                                .storeRegAddressAccumulator(opcode.getTarget().get())
                                .build(opcode.getCyclesFun()))
        );

        /*
        LD (nn), A
         */
        put(0xEA,
                instructions,
                Instruction.builder()
                        .label("LD (nn), A")
                        .loadBytes(RegEnum.DOUBLE)
                        .storeAccumulatorAddressReg(RegEnum.A)
                        .build(context -> 4)
        );

        /*
        LD A, (C)
         */
        put(0xF2,
                instructions,
                Instruction.builder()
                        .label("LD A, (0xFF00 + C)")
                        .loadReg(RegEnum.C)
                        .add(0xFF00)
                        .loadAddress()
                        .store(RegEnum.A)
                        .build(context -> 2)
        );

        /*
         * LD (C), A
         * */
        put(0xE2,
                instructions,
                Instruction.builder()
                        .label("LD (0xFF00 + C), A")
                        .loadReg(RegEnum.C)
                        .add(0xFF00)
                        .storeAccumulatorAddressReg(RegEnum.A)
                        .build(context -> 2)
        );

        /*
         * LD A, (HL-)
         * LDD A, (HL)
         * */
        put(0x3A,
                instructions,
                Instruction.builder()
                        .label("LD A, (HL-)")
                        .loadReg(RegEnum.HL)
                        .loadAddress()
                        .store(RegEnum.A)
                        .decrementReg(RegEnum.HL)
                        .build(context -> 2)
        );

        /*
         * LD (HL-), A
         * LDD (HL), A
         * */
        put(0x32,
                instructions,
                Instruction.builder()
                        .label("LD (HL-), A")
                        .loadReg(RegEnum.HL)
                        .storeAccumulatorAddressReg(RegEnum.A)
                        .decrementReg(RegEnum.HL)
                        .build(context -> 2)
        );

        /*
         * LD A, (HL+)
         * LDI A, (HL)
         * */
        put(0x2A,
                instructions,
                Instruction.builder()
                        .label("LD A, (HL+)")
                        .loadReg(RegEnum.HL)
                        .loadAddress()
                        .store(RegEnum.A)
                        .incrementReg(RegEnum.HL)
                        .build(context -> 2)
        );

        /*
         * LD (HL+), A
         * LDI (HL), A
         * */
        put(0x22,
                instructions,
                Instruction.builder()
                        .label("LD (HL+), A")
                        .loadReg(RegEnum.HL)
                        .storeAccumulatorAddressReg(RegEnum.A)
                        .incrementReg(RegEnum.HL)
                        .build(context -> 2)
        );

        /*
         * LDH (n), A
         * */
        put(0xE0,
                instructions,
                Instruction.builder()
                        .label("LDH (n), A")
                        .loadBytes(RegEnum.SINGLE)
                        .add(0xFF00)
                        .storeAccumulatorAddressReg(RegEnum.A)
                        .build(context -> 3)
        );

        /*
         * LDH A, (n)
         * */
        put(0xF0,
                instructions,
                Instruction.builder()
                        .label("LDH A, (n)")
                        .loadBytes(RegEnum.SINGLE)
                        .add(0xFF00)
                        .loadAddress()
                        .store(RegEnum.A)
                        .build(context -> 3)
        );
    }
}
