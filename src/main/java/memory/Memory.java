package memory;

import ppu.Tiles;

import static cpu.BitUtils.*;

public class Memory implements AddressSpace {

    private int size;
    private int[] memory;
    private Tiles tiles;

    public Memory(int size) {
        this.size = size;
        this.memory = new int[size];
    }

    public void setTiles(Tiles tiles) {
        this.tiles = tiles;
    }

    @Override
    public void set(int address, int value) {
        isShort(address);
        isByte(value);
        memory[address] = value;

        // Writing to VRAM
        if (address >= 0x8000 && address <= 0x97FF) {
            tiles.updateTile(address);
        }
    }

    @Override
    public int get(int address) {
        isShort(address);
        return memory[address];
    }
}
