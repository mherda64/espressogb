package memory.regions;

import input.InputManager;

public class HighMem extends BaseMemory {

    private final InputManager inputManager;

    public HighMem(InputManager inputManager) {
        // 256B
        super(0x0100, 0xFF00);
        this.inputManager = inputManager;
    }

    @Override
    public void set(int address, int value) {
        memory[address - firstAddress] = value;

        if (address == 0xFF00) {
            inputManager.setInputColumn(value & 0x30);
        }
    }

    @Override
    public int get(int address) {
        if (address == 0xFF00)
            return inputManager.getKeys();
        if (address == 0xFF41)
            return memory[address - firstAddress] | 0x80;

        return memory[address - firstAddress];
    }

}
