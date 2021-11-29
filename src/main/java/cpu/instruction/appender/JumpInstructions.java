package cpu.instruction.appender;

import cpu.RegEnum;
import cpu.instruction.Instruction;
import cpu.instruction.Opcode;

import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

public class JumpInstructions implements InstructionAppender {

    @Override
    public void add(Map<Integer, Instruction> instructions) {

        /*
         * JP nn
         * */
        put(0xC3,
                instructions,
                Instruction.builder()
                        .loadBytes(RegEnum.DOUBLE)
                        .store(RegEnum.PC)
                        .build(context -> 4)
        );

        /*
         * JP cc, nn
         * */
        Stream.of(
                new Opcode<>(0xC2, JumpCondition.NZ, context -> context.isConditionTrue() ? 4 : 3),
                new Opcode<>(0xCA, JumpCondition.Z, context -> context.isConditionTrue() ? 4 : 3),
                new Opcode<>(0xD2, JumpCondition.NC, context -> context.isConditionTrue() ? 4 : 3),
                new Opcode<>(0xDA, JumpCondition.C, context -> context.isConditionTrue() ? 4 : 3)
        ).forEach(opcode ->
                put(opcode.getOpcode(),
                        instructions,
                        Instruction.builder()
                                .loadBytes(RegEnum.DOUBLE)
                                .jp(opcode.getTarget(), false)
                                .build(opcode.getCyclesFun()))
        );

        /*
         * JP HL
         * */
        put(0xE9,
                instructions,
                Instruction.builder()
                        .loadReg(RegEnum.HL)
                        .store(RegEnum.PC)
                        .build(context -> 1)
        );

        /*
         * JR n
         * */
        put(0x18,
                instructions,
                Instruction.builder()
                        .loadBytes(RegEnum.SINGLE)
                        .toSigned()
                        .jp(Optional.empty(), true)
                        .build(context -> 3)
        );

        Stream.of(
                new Opcode<>(0x20, JumpCondition.NZ, context -> context.isConditionTrue() ? 3 : 2),
                new Opcode<>(0x28, JumpCondition.Z, context -> context.isConditionTrue() ? 3 : 2),
                new Opcode<>(0x30, JumpCondition.NC, context -> context.isConditionTrue() ? 3 : 2),
                new Opcode<>(0x38, JumpCondition.C, context -> context.isConditionTrue() ? 3 : 2)
        ).forEach(opcode ->
                put(opcode.getOpcode(),
                        instructions,
                        Instruction.builder()
                                .loadBytes(RegEnum.SINGLE)
                                .toSigned()
                                .jp(opcode.getTarget(), true)
                                .build(opcode.getCyclesFun()))
        );

        /*
         * CALL nn
         * */
        put(0xCD,
                instructions,
                Instruction.builder()
                        .loadBytes(RegEnum.DOUBLE)
                        .call(Optional.empty())
                        .build(context -> 6)
        );

        /*
         * CALL cc, nn
         * */
        Stream.of(
                new Opcode<>(0xC4, JumpCondition.NZ, context -> context.isConditionTrue() ? 6 : 3),
                new Opcode<>(0xCC, JumpCondition.Z, context -> context.isConditionTrue() ? 6 : 3),
                new Opcode<>(0xD4, JumpCondition.NC, context -> context.isConditionTrue() ? 6 : 3),
                new Opcode<>(0xDC, JumpCondition.C, context -> context.isConditionTrue() ? 6 : 3)
        ).forEach(opcode ->
                put(opcode.getOpcode(),
                        instructions,
                        Instruction.builder()
                                .loadBytes(RegEnum.DOUBLE)
                                .call(opcode.getTarget())
                                .build(opcode.getCyclesFun()))
        );

        /*
         * RST n
         * */
        Stream.of(
                new Opcode<>(0xC7, 0x00, 4),
                new Opcode<>(0xCF, 0x08, 4),
                new Opcode<>(0xD7, 0x10, 4),
                new Opcode<>(0xDF, 0x18, 4),
                new Opcode<>(0xE7, 0x20, 4),
                new Opcode<>(0xEF, 0x28, 4),
                new Opcode<>(0xF7, 0x30, 4),
                new Opcode<>(0xFF, 0x38, 4)
        ).forEach(opcode ->
                put(opcode.getOpcode(),
                        instructions,
                        Instruction.builder()
                                .load(0x0000)
                                .add(opcode.getTarget().get())
                                .call(Optional.empty())
                                .build(opcode.getCyclesFun()))
        );

        /*
         * RET
         * */
        put(0xC9,
                instructions,
                Instruction.builder()
                        .ret(Optional.empty())
                        .build(context -> 4)
        );

        /*
         * RET cc
         * */
        Stream.of(
                new Opcode<>(0xC0, JumpCondition.NZ, context -> context.isConditionTrue() ? 5 : 2),
                new Opcode<>(0xC8, JumpCondition.Z, context -> context.isConditionTrue() ? 5 : 2),
                new Opcode<>(0xD0, JumpCondition.NC, context -> context.isConditionTrue() ? 5 : 2),
                new Opcode<>(0xD8, JumpCondition.C, context -> context.isConditionTrue() ? 5 : 2)
        ).forEach(opcode ->
                put(opcode.getOpcode(),
                        instructions,
                        Instruction.builder()
                                .ret(opcode.getTarget())
                                .build(opcode.getCyclesFun()))
        );

        /*
         * RETI
         * */
        // TODO: implement with interrupts
//        put(0xC9,
//                instructions,
//                Instruction.builder()
//                        .ret(Optional.empty())
//                        .build(context -> 4)
//        );
    }

}
