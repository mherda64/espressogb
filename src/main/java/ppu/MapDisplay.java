package ppu;

import memory.AddressSpace;

public class MapDisplay extends Display implements Runnable, UtilDisplay {

    public static final int DISPLAY_WIDTH = 256;
    public static final int DISPLAY_HEIGHT = 256;

    private final Tiles tiles;

    private final AddressSpace addressSpace;
    private final GPURegsManager regsManager;

    public MapDisplay(AddressSpace addressSpace, GPURegsManager regsManager, Tiles tiles, int scale) {
        super(DISPLAY_WIDTH, DISPLAY_HEIGHT, scale);
        this.addressSpace = addressSpace;
        this.regsManager = regsManager;
        this.tiles = tiles;
    }

    @Override
    public void drawTile(int baseX, int baseY, int[][] tile) {
        var bgPalette = regsManager.getBGPalette();
        for (int i = 0; i < 8 * scale; i++) {
            for (int j = 0; j < 8 * scale; j++) {
                rgb[baseY + i][baseX + j] = bgPalette[tile[i / scale][j / scale]];
            }
        }
    }

    private void drawDisplayRectangle() {
        var color = 0xFF & (~regsManager.getBGPalette()[0]);
        var scy = regsManager.getSCY();
        var scx = regsManager.getSCX();

        for (int x = 0; x < 160; x++) {
            setPixel((scy) % DISPLAY_HEIGHT, (scx + x) % DISPLAY_WIDTH, color);
            setPixel((scy + 144) % DISPLAY_HEIGHT, (scx + x) % DISPLAY_WIDTH, color);
        }

        for (int y = 0; y < 144; y++) {
            setPixel((scy + y) % DISPLAY_HEIGHT, (scx) % DISPLAY_WIDTH, color);
            setPixel((scy + y) % DISPLAY_HEIGHT, (scx + 160) % DISPLAY_WIDTH, color);
        }
    }

    @Override
    public void updateMap() {
        var mapOffset = 0x8000;
        mapOffset += regsManager.isBackgroundMap() ? 0x1C00 : 0x1800;
        var tileMap = tiles.getTileMap();

        for (int y = 0; y < 32; y++) {
            for (int x = 0; x < 32; x++) {
                var tile = addressSpace.get(mapOffset + y * 32 + x);
                if (regsManager.isBackgroundTiles() && tile < 128) tile += 256;
                drawTile(x * 8 * scale, y * 8 * scale, tileMap[tile]);
            }
        }

        drawDisplayRectangle();
        requestRefresh();
    }
}
