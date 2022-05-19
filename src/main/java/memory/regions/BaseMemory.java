package memory.regions;

import memory.AddressSpace;

public abstract class BaseMemory implements AddressSpace {

    protected final int firstAddress;
    protected final int size;
    protected final int[] memory;

    protected BaseMemory(int size, int firstAddress) {
        this.firstAddress = firstAddress;
        this.size = size;
        memory = new int[size];
    }

    @Override
    public void set(int address, int value) {
        memory[address - firstAddress] = value;
    }

    @Override
    public int get(int address) {
        return memory[address - firstAddress];
    }
}
