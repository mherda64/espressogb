package cpu.instruction.appender;

import cpu.RegEnum;
import cpu.instruction.Instruction;
import cpu.instruction.Opcode;

import java.util.Map;
import java.util.stream.Stream;

public class MiscInstructions implements InstructionAppender {

    @Override
    public void add(Map<Integer, Instruction> instructions) {

        /*
         * DAA
         * */
        put(0x27,
                instructions,
                Instruction.builder()
                        .daa()
                        .build(context -> 1)
        );

        /*
         * CPL
         * */
        put(0x2F,
                instructions,
                Instruction.builder()
                        .cpl()
                        .build(context -> 1)
        );

        /*
         * CCF
         * */
        put(0x3F,
                instructions,
                Instruction.builder()
                        .ccf()
                        .build(context -> 1)
        );

        /*
         * SCF
         * */
        put(0x37,
                instructions,
                Instruction.builder()
                        .setCarry()
                        .build(context -> 1)
        );

        /*
         * NOP
         * */
        put(0x00,
                instructions,
                Instruction.builder()
                        .build(context -> 1)
        );

        /*
         * HALT
         * */
        put(0x76,
                instructions,
                Instruction.builder()
                        .halt()
                        .build(context -> 1)
        );

        /*
         * STOP
         * */
        put(0x10,
                instructions,
                Instruction.builder()
                        .stop()
                        .build(context -> 1)
        );

        /*
         * DI
         * */
        put(0xF3,
                instructions,
                Instruction.builder()
                        .disableInterrupts()
                        .build(context -> 1)
        );

        /*
         * EI
         * */
        put(0xFB,
                instructions,
                Instruction.builder()
                        .enableInterrupts()
                        .build(context -> 1)
        );




    }

    @Override
    public void addPrefixed(Map<Integer, Instruction> instructions) {
        /*
         * SWAP n
         * */
        Stream.of(
                new Opcode<>(0x37, RegEnum.A, 2),
                new Opcode<>(0x30, RegEnum.B, 2),
                new Opcode<>(0x31, RegEnum.C, 2),
                new Opcode<>(0x32, RegEnum.D, 2),
                new Opcode<>(0x33, RegEnum.E, 2),
                new Opcode<>(0x34, RegEnum.H, 2),
                new Opcode<>(0x35, RegEnum.L, 2)
        ).forEach(opcode ->
                put(opcode.getOpcode(),
                        instructions,
                        Instruction.builder()
                                .loadReg(opcode.getTarget().get())
                                .swap()
                                .store(opcode.getTarget().get())
                                .build(opcode.getCyclesFun()))
        );

        put(0x36,
                instructions,
                Instruction.builder()
                        .loadReg(RegEnum.HL)
                        .loadAddress()
                        .swap()
                        .storeRegAddressAccumulator(RegEnum.HL)
                        .build(context -> 4)
        );
    }
}
