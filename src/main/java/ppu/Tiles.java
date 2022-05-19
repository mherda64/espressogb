package ppu;

import memory.AddressSpace;

public class Tiles {

    private AddressSpace addressSpace;

    private final int[][][] tileMap = new int[384][8][8];

    public Tiles(AddressSpace addressSpace) {
        this.addressSpace = addressSpace;
    }

    public Tiles() {
    }

    public void setAddressSpace(AddressSpace addressSpace) {
        this.addressSpace = addressSpace;
    }

    public int[][][] getTileMap() {
        return tileMap;
    }

    public void updateTile(int address) {
        // Get even address for tile row
        address &= 0xFFFE;

        // Each tile takes up 16 bytes - 0x10 elements
        // So we can easily get index of each tile
        var tile = (address >> 4) & 0x1FF;

        // We can also get y index in the tile
        var y = (address >> 1) & 0x7;

        // Update single row of the tile in tilemap
        for (int x = 0; x < 8; x++) {
            var mask = 1 << (7 - x);
            tileMap[tile][y][x] =
                    ((addressSpace.get(address) & mask) > 0 ? 1 : 0) + ((addressSpace.get(address + 1) & mask) > 0 ? 2 : 0);

        }
    }

}
