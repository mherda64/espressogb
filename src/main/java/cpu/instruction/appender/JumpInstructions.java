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
                        .jp(Optional.empty())
                        .build(context -> 3)
        );

        /*
        * JP cc, nn
        * */
        put(0xC2,
                instructions,
                Instruction.builder()
                        .loadBytes(RegEnum.DOUBLE)
                        .jp(Optional.of(JumpCondition.NZ))
                        .build(context -> context.shouldJump() ? 4 : 3)
        );

        put(0xCA,
                instructions,
                Instruction.builder()
                        .loadBytes(RegEnum.DOUBLE)
                        .jp(Optional.of(JumpCondition.Z))
                        .build(context -> context.shouldJump() ? 4 : 3)
        );

        Stream.of(
                new Opcode<>(0xC2, JumpCondition.NZ, context -> context.shouldJump() ? 4 : 3)
        );
    }

}
