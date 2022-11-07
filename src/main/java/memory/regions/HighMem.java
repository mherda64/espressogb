package memory.regions;

import cpu.interrupt.InterruptEnum;
import cpu.interrupt.InterruptRegs;
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
            set(InterruptRegs.IF.getAddress(), get(InterruptRegs.IF.getAddress()) | InterruptEnum.JOYPAD.get());
        }
    }

    @Override
    public int get(int address) {
        if (address == 0xFF00)
            return inputManager.getKeys();

        return memory[address - firstAddress];
    }

}
