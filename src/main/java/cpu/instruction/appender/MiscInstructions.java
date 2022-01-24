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
                        .label("DAA")
                        .daa()
                        .build(context -> 1)
        );

        /*
         * CPL
         * */
        put(0x2F,
                instructions,
                Instruction.builder()
                        .label("CPL")
                        .cpl()
                        .build(context -> 1)
        );

        /*
         * CCF
         * */
        put(0x3F,
                instructions,
                Instruction.builder()
                        .label("CCF")
                        .ccf()
                        .build(context -> 1)
        );

        /*
         * SCF
         * */
        put(0x37,
                instructions,
                Instruction.builder()
                        .label("SCF")
                        .setCarry()
                        .build(context -> 1)
        );

        /*
         * NOP
         * */
        put(0x00,
                instructions,
                Instruction.builder()
                        .label("NOP")
                        .build(context -> 1)
        );

        /*
         * HALT
         * */
        put(0x76,
                instructions,
                Instruction.builder()
                        .label("HALT")
                        .halt()
                        .build(context -> 1)
        );

        /*
         * STOP
         * */
        put(0x10,
                instructions,
                Instruction.builder()
                        .label("STOP")
                        .stop()
                        .build(context -> 1)
        );

        /*
         * DI
         * */
        put(0xF3,
                instructions,
                Instruction.builder()
                        .label("DI")
                        .disableInterrupts()
                        .build(context -> 1)
        );

        /*
         * EI
         * */
        put(0xFB,
                instructions,
                Instruction.builder()
                        .label("EI")
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
                                .label(String.format("SWAP %s", opcode.getTarget()))
                                .loadReg(opcode.getTarget())
                                .swap()
                                .store(opcode.getTarget())
                                .build(opcode.getCyclesFun()))
        );

        /*
         * SWAP (HL)
         * */

        put(0x36,
                instructions,
                Instruction.builder()
                        .label("SWAP (HL)")
                        .loadReg(RegEnum.HL)
                        .loadAddress()
                        .swap()
                        .storeRegAddressAccumulator(RegEnum.HL)
                        .build(context -> 4)
        );
    }
}
