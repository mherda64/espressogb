package ppu;

import memory.AddressSpace;

public class MapDisplay extends Display implements Runnable, UtilDisplay {

    public static final int DISPLAY_WIDTH = 256;

    public static final int DISPLAY_HEIGHT = 256;

    private boolean backgroundMap = false;
    private boolean backgroundTile = false;

    private final Tiles tiles;

    private final AddressSpace addressSpace;

    public final int[] PALETTE = {
            getColor(255, 255, 255),
            getColor(192, 192, 192),
            getColor(120, 120, 120),
            0
    };

    private int getColor(int r, int g, int b) {
        return 0xFF | r << 16 | g << 8 | b;
    }

    public MapDisplay(AddressSpace addressSpace, Tiles tiles, int scale) {
        super(DISPLAY_WIDTH, DISPLAY_HEIGHT, scale);
        this.addressSpace = addressSpace;
        this.tiles = tiles;
    }

    @Override
    public void drawTile(int baseX, int baseY, int[][] tile) {
        for (int i = 0; i < 8 * scale; i++) {
            for (int j = 0; j < 8 * scale; j++) {
                rgb[baseY + i][baseX + j] = PALETTE[tile[i / scale][j / scale]];
            }
        }
    }

    @Override
    public void updateMap() {
        var mapOffset = 0x8000;
        mapOffset += backgroundMap ? 0x1C00 : 0x1800;
        var tileMap = tiles.getTileMap();

        for (int y = 0; y < 32; y++) {
            for (int x = 0; x < 32; x++) {
                var tile = addressSpace.get(mapOffset + y * 32 + x);
                if (backgroundTile && tile < 128) tile += 256;
                drawTile(x * 8 * scale, y * 8 * scale, tileMap[tile]);

            }
        }
    }
}
