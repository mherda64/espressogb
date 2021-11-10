package cpu.instructions;

import cpu.Registers;
import memory.AddressSpace;

public interface Operation {
    int execute(Registers registers, AddressSpace addressSpace, int accumulator);
//    int getOperandLength();
}
