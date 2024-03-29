package cpu;

import cpu.instruction.Instruction;
import cpu.instruction.Instructions;
import cpu.interrupt.InterruptManager;
import memory.AddressSpace;

public class InstructionsTest {

    public static Instruction executeInstruction(int opcode, boolean prefixed, Registers registers, AddressSpace addressSpace) {
        var instr = prefixed ? Instructions.getPrefixed(opcode) : Instructions.get(opcode);
        var context = instr.getContext();
        var interruptManager = new InterruptManager(null, registers, addressSpace);

        int accumulator = 0;
        for (var operation : instr.getOperations()) {
            accumulator = operation.execute(registers, addressSpace, accumulator, context, interruptManager);
        }
        return instr;
    }

}
