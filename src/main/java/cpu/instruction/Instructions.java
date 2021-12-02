package cpu.instruction;

import cpu.Context;
import cpu.instruction.appender.*;

import java.util.*;
import java.util.stream.Stream;

public class Instructions {

    public static final Map<Integer, Instruction> instructions;
    public static final Map<Integer, Instruction> prefixed;

    public static Instruction get(int opcode) {
        var instr = instructions.get(opcode);
        instr.setContext(new Context());
        return instr;
    }

    public static Instruction getPrefixed(int opcode) {
        var instr = prefixed.get(opcode);
        instr.setContext(new Context());
        return instr;
    }

    private static void put(int opcode, Instruction instruction) {
        if (instructions.containsKey(opcode))
            throw new IllegalStateException(String.format("Opcode %02X already exists!", opcode));
        instructions.put(opcode, instruction);
    }

    static {
        instructions = new HashMap<>();
        prefixed = new HashMap<>();

        Stream.of(
                new ByteLoadInstructions(),
                new WordLoadInstructions(),
                new ByteAluInstructions(),
                new WordAluInstructions(),
                new RotateInstructions(),
                new MiscInstructions(),
                new BitInstructions(),
                new JumpInstructions()
        ).forEach(instructionsAppender -> {
            instructionsAppender.add(instructions);
            instructionsAppender.addPrefixed(prefixed);
        });

    }

    private Instructions() {
    }
}
