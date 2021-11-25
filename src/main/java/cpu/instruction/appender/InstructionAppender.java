package cpu.instruction.appender;

import cpu.instruction.Instruction;

import java.util.Map;

public interface InstructionAppender {

    default void put(int opcode, Map<Integer, Instruction> instructions, Instruction instruction) {
        if (instructions.containsKey(opcode))
            throw new IllegalStateException(String.format("Opcode %02X already exists!", opcode));
        instructions.put(opcode, instruction);
    }

    void add(Map<Integer, Instruction> instructions);

    default void addPrefixed(Map<Integer, Instruction> prefixed) {}

}
