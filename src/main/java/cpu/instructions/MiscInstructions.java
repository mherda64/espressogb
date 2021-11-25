package cpu.instructions;

import cpu.RegEnum;

import java.util.Map;
import java.util.stream.Stream;

public class MiscInstructions implements InstructionAppender {

    @Override
    public void add(Map<Integer, Instruction> instructions) {

//        put(0x27,
//                instructions,
//                Instruction.builder()
//                        .decimalAdjust
//        );

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
                                .build(opcode.getCycles()))
        );

        put(0x36,
                instructions,
                Instruction.builder()
                        .loadReg(RegEnum.HL)
                        .loadAddress()
                        .swap()
                        .storeRegAddressAccumulator(RegEnum.HL)
                        .build(4)
        );
    }
}
