package cpu;

import cpu.instructions.Instruction;
import cpu.instructions.Instructions;
import memory.AddressSpace;

public class InstructionsTest {

    public static Instruction executeInstruction(int opcode, boolean prefixed, Registers registers, AddressSpace addressSpace) {
        var instr = prefixed ? Instructions.getPrefixed(opcode) : Instructions.get(opcode);

        int accumulator = 0;
        for (var operation : instr.getOperations()) {
            accumulator = operation.execute(registers, addressSpace, accumulator);
        }
        return instr;
    }

}
