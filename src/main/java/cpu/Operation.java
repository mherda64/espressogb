package cpu;

import memory.AddressSpace;

public interface Operation {
    void execute(Registers registers, AddressSpace addressSpace);
    int getCycleLength();
}
