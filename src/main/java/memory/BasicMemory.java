package memory;

import static cpu.BitUtils.*;

public class BasicMemory implements AddressSpace {

    private final int[] memory;

    public BasicMemory(int size) {
        this.memory = new int[size];
    }

    @Override
    public void set(int address, int value) {
        isShort(address);
        isByte(value);

        memory[address] = value;
    }

    @Override
    public int get(int address) {
        isShort(address);

        return memory[address];
    }

    public void forceSet(int address, int value) {
        isByte(value);

        memory[address] = value;
    }

    public int forceGet(int address) {
        return memory[address];
    }

    public int getSize() {
        return memory.length;
    }
}
