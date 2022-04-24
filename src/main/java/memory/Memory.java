package memory;

import input.InputManager;
import ppu.Tiles;
import ppu.oam.SpriteManager;

import static cpu.BitUtils.*;

public class Memory implements AddressSpace {

    private final int size;
    private final int[] memory;
    private Tiles tiles;
    private SpriteManager spriteManager;
    private InputManager inputManager;

    public Memory(int size) {
        this.size = size;
        this.memory = new int[size];
    }

    public void setTiles(Tiles tiles) {
        this.tiles = tiles;
    }

    public void setSprites(SpriteManager spriteManager) {
        this.spriteManager = spriteManager;
    }

    public void setInputManager(InputManager inputManager) {
        this.inputManager = inputManager;
    }

    @Override
    public void set(int address, int value) {
        isShort(address);
        isByte(value);
        memory[address] = value;

        if (address == 0xFF00)
            inputManager.setInputColumn(value & 0x30);

        // Writing to VRAM
        if (address >= 0x8000 && address <= 0x97FF) {
            tiles.updateTile(address);
        }

        if (address >= 0xFE00 && address <= 0xFE9F) {
            spriteManager.updateSprite(address, value);
        }
    }

    @Override
    public int get(int address) {
        isShort(address);

        if (address == 0xFF00)
            return inputManager.getKeys();

        return memory[address];
    }
}
