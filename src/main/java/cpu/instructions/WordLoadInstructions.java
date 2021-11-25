package cpu.instructions;

import cpu.RegEnum;

import java.util.Map;
import java.util.stream.Stream;

public class WordLoadInstructions implements InstructionAppender {


    public void add(Map<Integer, Instruction> instructions) {

        /*
         * LD n, nn
         * */
        Stream.of(
                new Opcode<>(0x01, RegEnum.BC, 3),
                new Opcode<>(0x11, RegEnum.DE, 3),
                new Opcode<>(0x21, RegEnum.HL, 3),
                new Opcode<>(0x31, RegEnum.SP, 3)
        ).forEach(opcode ->
                put(opcode.getOpcode(),
                        instructions,
                        Instruction.builder()
                                .loadBytes(RegEnum.DOUBLE)
                                .store(opcode.getTarget().get())
                                .build(opcode.getCycles()))
        );

        /*
         * LD SP, HL
         * */
        put(0xF9,
                instructions,
                Instruction.builder()
                        .loadReg(RegEnum.HL)
                        .store(RegEnum.SP)
                        .build(2)
        );

        /*
         * LDHL SP, n
         * */
        put(0xF8,
                instructions,
                Instruction.builder()
                        .loadBytes(RegEnum.SINGLE)
                        .toSigned()
                        .addRegSetFlags(RegEnum.SP)
                        .store(RegEnum.HL)
                        .build(3)
        );

        /*
         * LD (nn), SP
         * */
        put(0x08,
                instructions,
                Instruction.builder()
                        .loadBytes(RegEnum.DOUBLE)
                        .storeAccumulatorAddressReg(RegEnum.SP)
                        .build(5)
        );

        /*
         * PUSH nn
         * */
        Stream.of(
                new Opcode<>(0xF5, RegEnum.AF, 4),
                new Opcode<>(0xC5, RegEnum.BC, 4),
                new Opcode<>(0xD5, RegEnum.DE, 4),
                new Opcode<>(0xE5, RegEnum.HL, 4)
        ).forEach(opcode ->
                put(opcode.getOpcode(),
                        instructions,
                        Instruction.builder()
                                .push(opcode.getTarget().get())
                                .build(opcode.getCycles()))
        );

        /*
         * POP nn
         * */
        Stream.of(
                new Opcode<>(0xF1, RegEnum.AF, 3),
                new Opcode<>(0xC1, RegEnum.BC, 3),
                new Opcode<>(0xD1, RegEnum.DE, 3),
                new Opcode<>(0xE1, RegEnum.HL, 3)
        ).forEach(opcode ->
                put(opcode.getOpcode(),
                        instructions,
                        Instruction.builder()
                                .pop(opcode.getTarget().get())
                                .build(opcode.getCycles()))
        );
    }
}
