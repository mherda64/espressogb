package cpu.instruction.appender;

import cpu.RegEnum;
import cpu.instruction.Instruction;
import cpu.instruction.Opcode;

import java.util.Map;
import java.util.stream.Stream;

public class RotateInstructions implements InstructionAppender {

    @Override
    public void add(Map<Integer, Instruction> instructions) {

        /*
         * RLCA
         * */
        put(0x07,
                instructions,
                Instruction.builder()
                        .label("RLCA")
                        .loadReg(RegEnum.A)
                        .rlc()
                        .store(RegEnum.A)
                        .build(context -> 1)
        );

        /*
         * RLA
         * */
        put(0x17,
                instructions,
                Instruction.builder()
                        .label("RLA")
                        .loadReg(RegEnum.A)
                        .rl()
                        .store(RegEnum.A)
                        .build(context -> 1)
        );

        /*
         * RRCA
         * */
        put(0x0F,
                instructions,
                Instruction.builder()
                        .label("RRCA")
                        .loadReg(RegEnum.A)
                        .rrc()
                        .store(RegEnum.A)
                        .build(context -> 1)
        );

        /*
         * RRA
         * */
        put(0x1F,
                instructions,
                Instruction.builder()
                        .label("RRA")
                        .loadReg(RegEnum.A)
                        .rr()
                        .store(RegEnum.A)
                        .build(context -> 1)
        );

    }

    @Override
    public void addPrefixed(Map<Integer, Instruction> prefixed) {

        /*
         * RLC n
         * */
        Stream.of(
                new Opcode<>(0x07, RegEnum.A, 2),
                new Opcode<>(0x00, RegEnum.B, 2),
                new Opcode<>(0x01, RegEnum.C, 2),
                new Opcode<>(0x02, RegEnum.D, 2),
                new Opcode<>(0x03, RegEnum.E, 2),
                new Opcode<>(0x04, RegEnum.H, 2),
                new Opcode<>(0x05, RegEnum.L, 2)
        ).forEach(opcode ->
                put(opcode.getOpcode(),
                        prefixed,
                        Instruction.builder()
                                .label(String.format("RLC %s", opcode.getTarget()))
                                .loadReg(opcode.getTarget())
                                .rlc()
                                .store(opcode.getTarget())
                                .build(opcode.getCyclesFun()))
        );

        /*
         * RLC (HL)
         * */

        put(0x06,
                prefixed,
                Instruction.builder()
                        .label("RLC (HL)")
                        .loadReg(RegEnum.HL)
                        .loadAddress()
                        .rlc()
                        .storeRegAddressAccumulator(RegEnum.HL)
                        .build(context -> 4)
        );

        /*
         * RL n
         * */
        Stream.of(
                new Opcode<>(0x17, RegEnum.A, 2),
                new Opcode<>(0x10, RegEnum.B, 2),
                new Opcode<>(0x11, RegEnum.C, 2),
                new Opcode<>(0x12, RegEnum.D, 2),
                new Opcode<>(0x13, RegEnum.E, 2),
                new Opcode<>(0x14, RegEnum.H, 2),
                new Opcode<>(0x15, RegEnum.L, 2)
        ).forEach(opcode ->
                put(opcode.getOpcode(),
                        prefixed,
                        Instruction.builder()
                                .label(String.format("RL %s", opcode.getTarget()))
                                .loadReg(opcode.getTarget())
                                .rl()
                                .store(opcode.getTarget())
                                .build(opcode.getCyclesFun()))
        );

        /*
         * RL (HL)
         * */

        put(0x16,
                prefixed,
                Instruction.builder()
                        .label("RL (HL)")
                        .loadReg(RegEnum.HL)
                        .loadAddress()
                        .rl()
                        .storeRegAddressAccumulator(RegEnum.HL)
                        .build(context -> 4)
        );


        /*
         * RRC n
         * */
        Stream.of(
                new Opcode<>(0x0F, RegEnum.A, 2),
                new Opcode<>(0x08, RegEnum.B, 2),
                new Opcode<>(0x09, RegEnum.C, 2),
                new Opcode<>(0x0A, RegEnum.D, 2),
                new Opcode<>(0x0B, RegEnum.E, 2),
                new Opcode<>(0x0C, RegEnum.H, 2),
                new Opcode<>(0x0D, RegEnum.L, 2)
        ).forEach(opcode ->
                put(opcode.getOpcode(),
                        prefixed,
                        Instruction.builder()
                                .label(String.format("RRC %s", opcode.getTarget()))
                                .loadReg(opcode.getTarget())
                                .rrc()
                                .store(opcode.getTarget())
                                .build(opcode.getCyclesFun()))
        );

        /*
         * RRC (HL)
         * */

        put(0x0E,
                prefixed,
                Instruction.builder()
                        .label("RRC (HL)")
                        .loadReg(RegEnum.HL)
                        .loadAddress()
                        .rrc()
                        .storeRegAddressAccumulator(RegEnum.HL)
                        .build(context -> 4)
        );

        /*
         * RR n
         * */
        Stream.of(
                new Opcode<>(0x1F, RegEnum.A, 2),
                new Opcode<>(0x18, RegEnum.B, 2),
                new Opcode<>(0x19, RegEnum.C, 2),
                new Opcode<>(0x1A, RegEnum.D, 2),
                new Opcode<>(0x1B, RegEnum.E, 2),
                new Opcode<>(0x1C, RegEnum.H, 2),
                new Opcode<>(0x1D, RegEnum.L, 2)
        ).forEach(opcode ->
                put(opcode.getOpcode(),
                        prefixed,
                        Instruction.builder()
                                .label(String.format("RR %s", opcode.getTarget()))
                                .loadReg(opcode.getTarget())
                                .rr()
                                .store(opcode.getTarget())
                                .build(opcode.getCyclesFun()))
        );

        /*
         * RR (HL)
         * */

        put(0x1E,
                prefixed,
                Instruction.builder()
                        .label("RR (HL)")
                        .loadReg(RegEnum.HL)
                        .loadAddress()
                        .rr()
                        .storeRegAddressAccumulator(RegEnum.HL)
                        .build(context -> 4)
        );

        /*
         * SLA n
         * */
        Stream.of(
                new Opcode<>(0x27, RegEnum.A, 2),
                new Opcode<>(0x20, RegEnum.B, 2),
                new Opcode<>(0x21, RegEnum.C, 2),
                new Opcode<>(0x22, RegEnum.D, 2),
                new Opcode<>(0x23, RegEnum.E, 2),
                new Opcode<>(0x24, RegEnum.H, 2),
                new Opcode<>(0x25, RegEnum.L, 2)
        ).forEach(opcode ->
                put(opcode.getOpcode(),
                        prefixed,
                        Instruction.builder()
                                .label(String.format("SLA %s", opcode.getTarget()))
                                .loadReg(opcode.getTarget())
                                .sla()
                                .store(opcode.getTarget())
                                .build(opcode.getCyclesFun()))
        );

        /*
         * SLA (HL)
         * */

        put(0x26,
                prefixed,
                Instruction.builder()
                        .label("SLA (HL)")
                        .loadReg(RegEnum.HL)
                        .loadAddress()
                        .sla()
                        .storeRegAddressAccumulator(RegEnum.HL)
                        .build(context -> 4)
        );

        /*
         * SRA n
         * */
        Stream.of(
                new Opcode<>(0x2F, RegEnum.A, 2),
                new Opcode<>(0x28, RegEnum.B, 2),
                new Opcode<>(0x29, RegEnum.C, 2),
                new Opcode<>(0x2A, RegEnum.D, 2),
                new Opcode<>(0x2B, RegEnum.E, 2),
                new Opcode<>(0x2C, RegEnum.H, 2),
                new Opcode<>(0x2D, RegEnum.L, 2)
        ).forEach(opcode ->
                put(opcode.getOpcode(),
                        prefixed,
                        Instruction.builder()
                                .label(String.format("SRA %s", opcode.getTarget()))
                                .loadReg(opcode.getTarget())
                                .sra()
                                .store(opcode.getTarget())
                                .build(opcode.getCyclesFun()))
        );

        /*
         * SRA (HL)
         * */

        put(0x2E,
                prefixed,
                Instruction.builder()
                        .label("SRA (HL)")
                        .loadReg(RegEnum.HL)
                        .loadAddress()
                        .sra()
                        .storeRegAddressAccumulator(RegEnum.HL)
                        .build(context -> 4)
        );

        /*
         * SRL n
         * */
        Stream.of(
                new Opcode<>(0x3F, RegEnum.A, 2),
                new Opcode<>(0x38, RegEnum.B, 2),
                new Opcode<>(0x39, RegEnum.C, 2),
                new Opcode<>(0x3A, RegEnum.D, 2),
                new Opcode<>(0x3B, RegEnum.E, 2),
                new Opcode<>(0x3C, RegEnum.H, 2),
                new Opcode<>(0x3D, RegEnum.L, 2)
        ).forEach(opcode ->
                put(opcode.getOpcode(),
                        prefixed,
                        Instruction.builder()
                                .label(String.format("SRL %s", opcode.getTarget()))
                                .loadReg(opcode.getTarget())
                                .srl()
                                .store(opcode.getTarget())
                                .build(opcode.getCyclesFun()))
        );

        /*
         * SRL (HL)
         * */

        put(0x3E,
                prefixed,
                Instruction.builder()
                        .label("SRL (HL)")
                        .loadReg(RegEnum.HL)
                        .loadAddress()
                        .srl()
                        .storeRegAddressAccumulator(RegEnum.HL)
                        .build(context -> 4)
        );
    }

}
