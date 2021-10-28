package memory;

import static cpu.BitUtils.*;

public class Memory implements AddressSpace {

    private int size;
    private int[] memory;

    public Memory(int size) {
        this.size = size;
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
}
