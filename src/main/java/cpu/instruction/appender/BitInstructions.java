package cpu.instruction.appender;

import cpu.RegEnum;
import cpu.instruction.Instruction;
import cpu.instruction.Opcode;

import java.util.Map;
import java.util.stream.Stream;

public class BitInstructions implements InstructionAppender {

    @Override
    public void add(Map<Integer, Instruction> instructions) {

    }

    @Override
    public void addPrefixed(Map<Integer, Instruction> prefixed) {
        /*
         * BIT b, reg
         * */
        for (int i = 0; i < 8; i++) {
            var baseOp = i * 8 + 0x40;
            final var param = i;
            Stream.of(
                    new Opcode<>(baseOp + 7, RegEnum.A, 2),
                    new Opcode<>(baseOp, RegEnum.B, 2),
                    new Opcode<>(baseOp + 1, RegEnum.C, 2),
                    new Opcode<>(baseOp + 2, RegEnum.D, 2),
                    new Opcode<>(baseOp + 3, RegEnum.E, 2),
                    new Opcode<>(baseOp + 4, RegEnum.H, 2),
                    new Opcode<>(baseOp + 5, RegEnum.L, 2)
            ).forEach(opcode ->
                    put(opcode.getOpcode(),
                            prefixed,
                            Instruction.builder()
                                    .label(String.format("BIT %d, %s", param, opcode.getTarget()))
                                    .loadReg(opcode.getTarget())
                                    .bitTest(param)
                                    .build(opcode.getCyclesFun()))
            );

            put(baseOp + 6,
                    prefixed,
                    Instruction.builder()
                            .label(String.format("BIT %d, (HL)", param))
                            .loadReg(RegEnum.HL)
                            .loadAddress()
                            .bitTest(param)
                            .build(context -> 4)
            );
        }

        /*
         * SET b, reg
         * */
        for (int i = 0; i < 8; i++) {
            var baseOp = i * 8 + 0xC0;
            final var param = i;
            Stream.of(
                    new Opcode<>(baseOp + 7, RegEnum.A, 2),
                    new Opcode<>(baseOp, RegEnum.B, 2),
                    new Opcode<>(baseOp + 1, RegEnum.C, 2),
                    new Opcode<>(baseOp + 2, RegEnum.D, 2),
                    new Opcode<>(baseOp + 3, RegEnum.E, 2),
                    new Opcode<>(baseOp + 4, RegEnum.H, 2),
                    new Opcode<>(baseOp + 5, RegEnum.L, 2)
            ).forEach(opcode ->
                    put(opcode.getOpcode(),
                            prefixed,
                            Instruction.builder()
                                    .label(String.format("SET %d, %s", param, opcode.getTarget()))
                                    .loadReg(opcode.getTarget())
                                    .setBit(param, true)
                                    .store(opcode.getTarget())
                                    .build(opcode.getCyclesFun()))
            );

            put(baseOp + 6,
                    prefixed,
                    Instruction.builder()
                            .label(String.format("SET %d, (HL)", param))
                            .loadReg(RegEnum.HL)
                            .loadAddress()
                            .setBit(param, true)
                            .storeRegAddressAccumulator(RegEnum.HL)
                            .build(context -> 4)
            );
        }

        /*
         * RES b, reg
         * */
        for (int i = 0; i < 8; i++) {
            var baseOp = i * 8 + 0x80;
            final var param = i;
            Stream.of(
                    new Opcode<>(baseOp + 7, RegEnum.A, 2),
                    new Opcode<>(baseOp, RegEnum.B, 2),
                    new Opcode<>(baseOp + 1, RegEnum.C, 2),
                    new Opcode<>(baseOp + 2, RegEnum.D, 2),
                    new Opcode<>(baseOp + 3, RegEnum.E, 2),
                    new Opcode<>(baseOp + 4, RegEnum.H, 2),
                    new Opcode<>(baseOp + 5, RegEnum.L, 2)
            ).forEach(opcode ->
                    put(opcode.getOpcode(),
                            prefixed,
                            Instruction.builder()
                                    .label(String.format("RES %d, %s", param, opcode.getTarget()))
                                    .loadReg(opcode.getTarget())
                                    .setBit(param, false)
                                    .store(opcode.getTarget())
                                    .build(opcode.getCyclesFun()))
            );

            put(baseOp + 6,
                    prefixed,
                    Instruction.builder()
                            .label(String.format("RES %d, (HL)", param))
                            .loadReg(RegEnum.HL)
                            .loadAddress()
                            .setBit(param, false)
                            .storeRegAddressAccumulator(RegEnum.HL)
                            .build(context -> 4)
            );
        }
    }
}
