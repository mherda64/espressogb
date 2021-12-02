package cpu.instruction.appender;

import cpu.RegEnum;
import cpu.instruction.Instruction;
import cpu.instruction.Opcode;

import java.util.Map;
import java.util.stream.Stream;

public class WordAluInstructions implements InstructionAppender {

    @Override
    public void add(Map<Integer, Instruction> instructions) {

        /*
         * ADD HL, n
         * */
        Stream.of(
                new Opcode<>(0x09, RegEnum.BC, 2),
                new Opcode<>(0x19, RegEnum.DE, 2),
                new Opcode<>(0x29, RegEnum.HL, 2),
                new Opcode<>(0x39, RegEnum.SP, 2)
        ).forEach(opcode ->
                put(opcode.getOpcode(),
                        instructions,
                        Instruction.builder()
                                .loadReg(opcode.getTarget().get())
                                .add(RegEnum.HL, false, false)
                                .store(RegEnum.HL)
                                .build(opcode.getCyclesFun()))
        );

        /*
         * ADD SP, n
         * */
        put(0xE8,
                instructions,
                Instruction.builder()
                        .loadBytes(RegEnum.SINGLE)
                        .toSigned()
                        .add(RegEnum.SP, false, true)
                        .store(RegEnum.SP)
                        .build(context -> 4)
        );

        /*
         * INC nn
         * */
        Stream.of(
                new Opcode<>(0x03, RegEnum.BC, 2),
                new Opcode<>(0x13, RegEnum.DE, 2),
                new Opcode<>(0x23, RegEnum.HL, 2),
                new Opcode<>(0x33, RegEnum.SP, 2)
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
         * DEC nn
         * */
        Stream.of(
                new Opcode<>(0x0B, RegEnum.BC, 2),
                new Opcode<>(0x1B, RegEnum.DE, 2),
                new Opcode<>(0x2B, RegEnum.HL, 2),
                new Opcode<>(0x3B, RegEnum.SP, 2)
        ).forEach(opcode ->
                put(opcode.getOpcode(),
                        instructions,
                        Instruction.builder()
                                .loadReg(opcode.getTarget().get())
                                .dec(opcode.getTarget().get().size)
                                .store(opcode.getTarget().get())
                                .build(opcode.getCyclesFun()))
        );

    }

}
