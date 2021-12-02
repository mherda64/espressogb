package cpu.instruction.appender;

import cpu.RegEnum;
import cpu.instruction.Instruction;
import cpu.instruction.Opcode;

import java.util.Map;
import java.util.stream.Stream;

public class ByteAluInstructions implements InstructionAppender {

    public void add(Map<Integer, Instruction> instructions) {

        /*
         * ADD A, n
         * */
        Stream.of(
                new Opcode<>(0x87, RegEnum.A, 1),
                new Opcode<>(0x80, RegEnum.B, 1),
                new Opcode<>(0x81, RegEnum.C, 1),
                new Opcode<>(0x82, RegEnum.D, 1),
                new Opcode<>(0x83, RegEnum.E, 1),
                new Opcode<>(0x84, RegEnum.H, 1),
                new Opcode<>(0x85, RegEnum.L, 1)
        ).forEach(opcode ->
                put(opcode.getOpcode(),
                        instructions,
                        Instruction.builder()
                                .loadReg(RegEnum.A)
                                .add(opcode.getTarget().get(), false, false)
                                .store(RegEnum.A)
                                .build(opcode.getCyclesFun()))
        );

        /*
        * ADD A, (HL)
        * */
        put(0x86,
                instructions,
                Instruction.builder()
                        .loadReg(RegEnum.HL)
                        .loadAddress()
                        .add(RegEnum.A, false, false)
                        .store(RegEnum.A)
                        .build(context -> 2)
        );

        /*
         * ADD A, # (immediate byte)
         * */
        put(0xC6,
                instructions,
                Instruction.builder()
                        .loadBytes(RegEnum.SINGLE)
                        .add(RegEnum.A, false, false)
                        .store(RegEnum.A)
                        .build(context -> 2)
        );

        /*
         * ADC A, n
         * */
        Stream.of(
                new Opcode<>(0x8F, RegEnum.A, 1),
                new Opcode<>(0x88, RegEnum.B, 1),
                new Opcode<>(0x89, RegEnum.C, 1),
                new Opcode<>(0x8A, RegEnum.D, 1),
                new Opcode<>(0x8B, RegEnum.E, 1),
                new Opcode<>(0x8C, RegEnum.H, 1),
                new Opcode<>(0x8D, RegEnum.L, 1)
        ).forEach(opcode ->
                put(opcode.getOpcode(),
                        instructions,
                        Instruction.builder()
                                .loadReg(RegEnum.A)
                                .add(opcode.getTarget().get(), true, false)
                                .store(RegEnum.A)
                                .build(opcode.getCyclesFun()))
        );

        /*
         * ADC A, (HL)
         * */
        put(0x8E,
                instructions,
                Instruction.builder()
                        .loadReg(RegEnum.HL)
                        .loadAddress()
                        .add(RegEnum.A, true, false)
                        .store(RegEnum.A)
                        .build(context -> 2)
        );

        /*
         * ADC A, # (immediate byte)
         * */
        put(0xCE,
                instructions,
                Instruction.builder()
                        .loadBytes(RegEnum.SINGLE)
                        .add(RegEnum.A, true, false)
                        .store(RegEnum.A)
                        .build(context -> 2)
        );

        /*
         * SUB A, n
         * */
        Stream.of(
                new Opcode<>(0x97, RegEnum.A, 1),
                new Opcode<>(0x90, RegEnum.B, 1),
                new Opcode<>(0x91, RegEnum.C, 1),
                new Opcode<>(0x92, RegEnum.D, 1),
                new Opcode<>(0x93, RegEnum.E, 1),
                new Opcode<>(0x94, RegEnum.H, 1),
                new Opcode<>(0x95, RegEnum.L, 1)
        ).forEach(opcode ->
                put(opcode.getOpcode(),
                        instructions,
                        Instruction.builder()
                                .loadReg(RegEnum.A)
                                .sub(opcode.getTarget().get(), false)
                                .store(RegEnum.A)
                                .build(opcode.getCyclesFun()))
        );

        /*
         * SUB A, (HL)
         * */
        put(0x96,
                instructions,
                Instruction.builder()
                        .loadReg(RegEnum.HL)
                        .loadAddress()
                        .subFrom(RegEnum.A, false)
                        .store(RegEnum.A)
                        .build(context -> 2)
        );

        /*
         * SUB A, # (immediate byte)
         * */
        put(0xD6,
                instructions,
                Instruction.builder()
                        .loadBytes(RegEnum.SINGLE)
                        .subFrom(RegEnum.A, false)
                        .store(RegEnum.A)
                        .build(context -> 2)
        );

        /*
         * SBC A, n
         * */
        Stream.of(
                new Opcode<>(0x9F, RegEnum.A, 1),
                new Opcode<>(0x98, RegEnum.B, 1),
                new Opcode<>(0x99, RegEnum.C, 1),
                new Opcode<>(0x9A, RegEnum.D, 1),
                new Opcode<>(0x9B, RegEnum.E, 1),
                new Opcode<>(0x9C, RegEnum.H, 1),
                new Opcode<>(0x9D, RegEnum.L, 1)
        ).forEach(opcode ->
                put(opcode.getOpcode(),
                        instructions,
                        Instruction.builder()
                                .loadReg(RegEnum.A)
                                .sub(opcode.getTarget().get(), true)
                                .store(RegEnum.A)
                                .build(opcode.getCyclesFun()))
        );

        /*
         * SBC A, (HL)
         * */
        put(0x9E,
                instructions,
                Instruction.builder()
                        .loadReg(RegEnum.HL)
                        .loadAddress()
                        .subFrom(RegEnum.A, true)
                        .store(RegEnum.A)
                        .build(context -> 2)
        );

        /*
         * SBC A, # (immediate byte)
         * */
        put(0xDE,
                instructions,
                Instruction.builder()
                        .loadBytes(RegEnum.SINGLE)
                        .subFrom(RegEnum.A, true)
                        .store(RegEnum.A)
                        .build(context -> 2)
        );

        /*
         * AND A, n
         * */
        Stream.of(
                new Opcode<>(0xA7, RegEnum.A, 1),
                new Opcode<>(0xA0, RegEnum.B, 1),
                new Opcode<>(0xA1, RegEnum.C, 1),
                new Opcode<>(0xA2, RegEnum.D, 1),
                new Opcode<>(0xA3, RegEnum.E, 1),
                new Opcode<>(0xA4, RegEnum.H, 1),
                new Opcode<>(0xA5, RegEnum.L, 1)
        ).forEach(opcode ->
                put(opcode.getOpcode(),
                        instructions,
                        Instruction.builder()
                                .loadReg(RegEnum.A)
                                .and(opcode.getTarget().get())
                                .store(RegEnum.A)
                                .build(opcode.getCyclesFun()))
        );

        /*
         * AND A, (HL)
         * */
        put(0xA6,
                instructions,
                Instruction.builder()
                        .loadReg(RegEnum.HL)
                        .loadAddress()
                        .and(RegEnum.A)
                        .store(RegEnum.A)
                        .build(context -> 2)
        );

        /*
         * AND A, # (immediate byte)
         * */
        put(0xE6,
                instructions,
                Instruction.builder()
                        .loadBytes(RegEnum.SINGLE)
                        .and(RegEnum.A)
                        .store(RegEnum.A)
                        .build(context -> 2)
        );

        /*
         * OR A, n
         * */
        Stream.of(
                new Opcode<>(0xB7, RegEnum.A, 1),
                new Opcode<>(0xB0, RegEnum.B, 1),
                new Opcode<>(0xB1, RegEnum.C, 1),
                new Opcode<>(0xB2, RegEnum.D, 1),
                new Opcode<>(0xB3, RegEnum.E, 1),
                new Opcode<>(0xB4, RegEnum.H, 1),
                new Opcode<>(0xB5, RegEnum.L, 1)
        ).forEach(opcode ->
                put(opcode.getOpcode(),
                        instructions,
                        Instruction.builder()
                                .loadReg(RegEnum.A)
                                .or(opcode.getTarget().get())
                                .store(RegEnum.A)
                                .build(opcode.getCyclesFun()))
        );

        /*
         * OR A, (HL)
         * */
        put(0xB6,
                instructions,
                Instruction.builder()
                        .loadReg(RegEnum.HL)
                        .loadAddress()
                        .or(RegEnum.A)
                        .store(RegEnum.A)
                        .build(context -> 2)
        );

        /*
         * OR A, # (immediate byte)
         * */
        put(0xF6,
                instructions,
                Instruction.builder()
                        .loadBytes(RegEnum.SINGLE)
                        .or(RegEnum.A)
                        .store(RegEnum.A)
                        .build(context -> 2)
        );


        /*
         * XOR A, n
         * */
        Stream.of(
                new Opcode<>(0xAF, RegEnum.A, 1),
                new Opcode<>(0xA8, RegEnum.B, 1),
                new Opcode<>(0xA9, RegEnum.C, 1),
                new Opcode<>(0xAA, RegEnum.D, 1),
                new Opcode<>(0xAB, RegEnum.E, 1),
                new Opcode<>(0xAC, RegEnum.H, 1),
                new Opcode<>(0xAD, RegEnum.L, 1)
        ).forEach(opcode ->
                put(opcode.getOpcode(),
                        instructions,
                        Instruction.builder()
                                .loadReg(RegEnum.A)
                                .xor(opcode.getTarget().get())
                                .store(RegEnum.A)
                                .build(opcode.getCyclesFun()))
        );

        /*
         * XOR A, (HL)
         * */
        put(0xAE,
                instructions,
                Instruction.builder()
                        .loadReg(RegEnum.HL)
                        .loadAddress()
                        .xor(RegEnum.A)
                        .store(RegEnum.A)
                        .build(context -> 2)
        );

        /*
         * XOR A, # (immediate byte)
         * */
        put(0xEE,
                instructions,
                Instruction.builder()
                        .loadBytes(RegEnum.SINGLE)
                        .xor(RegEnum.A)
                        .store(RegEnum.A)
                        .build(context -> 2)
        );


        /*
         * CP A, n
         * */
        Stream.of(
                new Opcode<>(0xBF, RegEnum.A, 1),
                new Opcode<>(0xB8, RegEnum.B, 1),
                new Opcode<>(0xB9, RegEnum.C, 1),
                new Opcode<>(0xBA, RegEnum.D, 1),
                new Opcode<>(0xBB, RegEnum.E, 1),
                new Opcode<>(0xBC, RegEnum.H, 1),
                new Opcode<>(0xBD, RegEnum.L, 1)
        ).forEach(opcode ->
                put(opcode.getOpcode(),
                        instructions,
                        Instruction.builder()
                                .loadReg(opcode.getTarget().get())
                                .cp(RegEnum.A)
                                .build(opcode.getCyclesFun()))
        );

        /*
         * CP A, (HL)
         * */
        put(0xBE,
                instructions,
                Instruction.builder()
                        .loadReg(RegEnum.HL)
                        .loadAddress()
                        .cp(RegEnum.A)
                        .build(context -> 2)
        );

        /*
         * CP A, # (immediate byte)
         * */
        put(0xFE,
                instructions,
                Instruction.builder()
                        .loadBytes(RegEnum.SINGLE)
                        .cp(RegEnum.A)
                        .build(context -> 2)
        );


        /*
         * INC n
         * */
        Stream.of(
                new Opcode<>(0x3C, RegEnum.A, 1),
                new Opcode<>(0x04, RegEnum.B, 1),
                new Opcode<>(0x0C, RegEnum.C, 1),
                new Opcode<>(0x14, RegEnum.D, 1),
                new Opcode<>(0x1C, RegEnum.E, 1),
                new Opcode<>(0x24, RegEnum.H, 1),
                new Opcode<>(0x2C, RegEnum.L, 1)
        ).forEach(opcode ->
                put(opcode.getOpcode(),
                        instructions,
                        Instruction.builder()
                                .loadReg(opcode.getTarget().get())
                                .inc(opcode.getTarget().get().size)
                                .store(opcode.getTarget().get())
                                .build(opcode.getCyclesFun()))
        );

        /*
         * INC (HL)
         * */
        put(0x34,
                instructions,
                Instruction.builder()
                        .loadReg(RegEnum.HL)
                        .loadAddress()
                        .inc(RegEnum.SINGLE)
                        .storeRegAddressAccumulator(RegEnum.HL)
                        .build(context -> 3)
        );


        /*
         * DEC n
         * */
        Stream.of(
                new Opcode<>(0x3D, RegEnum.A, 1),
                new Opcode<>(0x05, RegEnum.B, 1),
                new Opcode<>(0x0D, RegEnum.C, 1),
                new Opcode<>(0x15, RegEnum.D, 1),
                new Opcode<>(0x1D, RegEnum.E, 1),
                new Opcode<>(0x25, RegEnum.H, 1),
                new Opcode<>(0x2D, RegEnum.L, 1)
        ).forEach(opcode ->
                put(opcode.getOpcode(),
                        instructions,
                        Instruction.builder()
                                .loadReg(opcode.getTarget().get())
                                .dec(opcode.getTarget().get().size)
                                .store(opcode.getTarget().get())
                                .build(opcode.getCyclesFun()))
        );

        /*
         * DEC (HL)
         * */
        put(0x35,
                instructions,
                Instruction.builder()
                        .loadReg(RegEnum.HL)
                        .loadAddress()
                        .dec(RegEnum.SINGLE)
                        .storeRegAddressAccumulator(RegEnum.HL)
                        .build(context -> 3)
        );
    }

}
