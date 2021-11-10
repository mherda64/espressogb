package cpu.instructions;

import java.util.*;

public class Instructions {

    public static final Map<Integer, Instruction> instructions;

    public static Instruction get(int opcode) {
        return instructions.get(opcode);
    }

    private static void put(int opcode, Instruction instruction) {
        if (instructions.containsKey(opcode))
            throw new IllegalStateException(String.format("Opcode %02X already exists!", opcode));
        instructions.put(opcode, instruction);
    }

    static {
        instructions = new HashMap<>();

        ByteLoadInstructions.add(instructions);
    }

    private Instructions() {
    }
}
