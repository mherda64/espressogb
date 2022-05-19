package memory.regions;

import ppu.Tiles;

public class VideoRAM extends BaseMemory {

    private final Tiles tiles;

    public VideoRAM(Tiles tiles) {
        // 8KiB
        super(0x2000, 0x8000);
        this.tiles = tiles;
    }

    @Override
    public void set(int address, int value) {
        memory[address - firstAddress] = value;
        if (address <= 0x97FF)
            tiles.updateTile(address);

    }

}
