package cpu.instruction;

import cpu.instruction.appender.*;

import java.util.*;
import java.util.stream.Stream;

public class Instructions {

    public static final Map<Integer, Instruction> instructions;
    public static final Map<Integer, Instruction> prefixed;

    public static Instruction get(int opcode) {
        return instructions.get(opcode);
    }

    public static Instruction getPrefixed(int opcode) {
        return prefixed.get(opcode);
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
                new RotateInstructions(),
                new MiscInstructions(),
                new BitInstructions()
        ).forEach(instructionsAppender -> {
            instructionsAppender.add(instructions);
            instructionsAppender.addPrefixed(prefixed);
        });

    }

    private Instructions() {
    }
}
