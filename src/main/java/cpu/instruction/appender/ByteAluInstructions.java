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
                                .label(String.format("ADD A, %s", opcode.getTarget()))
                                .loadReg(RegEnum.A)
                                .add(opcode.getTarget(), false, false)
                                .store(RegEnum.A)
                                .build(opcode.getCyclesFun()))
        );

        /*
        * ADD A, (HL)
        * */
        put(0x86,
                instructions,
                Instruction.builder()
                        .label("ADD A, HL")
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
                        .label("ADD A, # (immediate byte)")
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
                                .label(String.format("ADC A, %s", opcode.getTarget()))
                                .loadReg(RegEnum.A)
                                .add(opcode.getTarget(), true, false)
                                .store(RegEnum.A)
                                .build(opcode.getCyclesFun()))
        );

        /*
         * ADC A, (HL)
         * */
        put(0x8E,
                instructions,
                Instruction.builder()
                        .label("ADC A, (HL)")
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
                        .label("ADC A, # (immediate byte)")
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
                                .label(String.format("SUB A, %s", opcode.getTarget()))
                                .loadReg(RegEnum.A)
                                .sub(opcode.getTarget(), false)
                                .store(RegEnum.A)
                                .build(opcode.getCyclesFun()))
        );

        /*
         * SUB A, (HL)
         * */
        put(0x96,
                instructions,
                Instruction.builder()
                        .label("SUB A, (HL)")
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
                        .label("SUB A, # (immediate byte)")
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
                                .label(String.format("SBC A, %s", opcode.getTarget()))
                                .loadReg(RegEnum.A)
                                .sub(opcode.getTarget(), true)
                                .store(RegEnum.A)
                                .build(opcode.getCyclesFun()))
        );

        /*
         * SBC A, (HL)
         * */
        put(0x9E,
                instructions,
                Instruction.builder()
                        .label("SBC A, (HL)")
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
                        .label("SBC A, # (immediate byte)")
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
                                .label(String.format("AND A, %s", opcode.getTarget()))
                                .loadReg(RegEnum.A)
                                .and(opcode.getTarget())
                                .store(RegEnum.A)
                                .build(opcode.getCyclesFun()))
        );

        /*
         * AND A, (HL)
         * */
        put(0xA6,
                instructions,
                Instruction.builder()
                        .label("AND A, (HL)")
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
                        .label("AND A, # (immediate byte)")
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
                                .label(String.format("OR A, %s", opcode.getTarget()))
                                .loadReg(RegEnum.A)
                                .or(opcode.getTarget())
                                .store(RegEnum.A)
                                .build(opcode.getCyclesFun()))
        );

        /*
         * OR A, (HL)
         * */
        put(0xB6,
                instructions,
                Instruction.builder()
                        .label("OR A, (HL)")
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
                        .label("OR A, # (immediate byte)")
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
                                .label(String.format("XOR A, %s", opcode.getTarget()))
                                .loadReg(RegEnum.A)
                                .xor(opcode.getTarget())
                                .store(RegEnum.A)
                                .build(opcode.getCyclesFun()))
        );

        /*
         * XOR A, (HL)
         * */
        put(0xAE,
                instructions,
                Instruction.builder()
                        .label("XOR A, (HL)")
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
                        .label("XOR A, # (immediate byte)")
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
                                .label(String.format("CP A, %s", opcode.getTarget()))
                                .loadReg(opcode.getTarget())
                                .cp(RegEnum.A)
                                .build(opcode.getCyclesFun()))
        );

        /*
         * CP A, (HL)
         * */
        put(0xBE,
                instructions,
                Instruction.builder()
                        .label("CP A, (HL)")
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
                        .label("CP A, # (immediate byte)")
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
                                .label(String.format("INC %s", opcode.getTarget()))
                                .loadReg(opcode.getTarget())
                                .inc(opcode.getTarget().size)
                                .store(opcode.getTarget())
                                .build(opcode.getCyclesFun()))
        );

        /*
         * INC (HL)
         * */
        put(0x34,
                instructions,
                Instruction.builder()
                        .label("INC (HL)")
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
                                .label(String.format("DEC %s", opcode.getTarget()))
                                .loadReg(opcode.getTarget())
                                .dec(opcode.getTarget().size)
                                .store(opcode.getTarget())
                                .build(opcode.getCyclesFun()))
        );

        /*
         * DEC (HL)
         * */
        put(0x35,
                instructions,
                Instruction.builder()
                        .label("DEC (HL)")
                        .loadReg(RegEnum.HL)
                        .loadAddress()
                        .dec(RegEnum.SINGLE)
                        .storeRegAddressAccumulator(RegEnum.HL)
                        .build(context -> 3)
        );
    }

}
