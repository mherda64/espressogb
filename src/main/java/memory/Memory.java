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

    boolean initialized = false;

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

    public void setInitialized() {
        initialized = true;
    }

    @Override
    public void set(int address, int value) {
        isShort(address);
        isByte(value);

        if (initialized && address <= 0x3FFF) {
//            throw new IllegalStateException(String.format("Writing to ROM %04X bank 0", address));
            System.out.println((String.format("Trying to write to ROM memory PC: %04X bank 0, skipping...", address)));
            return;
        }

        if (initialized && address >= 0x4000 && address <= 0x7FFF) {
//            throw new IllegalStateException(String.format("Writing to ROM %04X bank N", address));
            System.out.println((String.format("Trying to write to ROM memory PC: %04X bank N, skipping...", address)));
            return;
        }

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

        // OAM
        if (address == 0xFF46) {
            var destAdress = (value << 8) & 0xFF00;
            for (int i = 0; i < 160; i++) {
                set(0xFE00 + i, get(destAdress + i));
            }
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
