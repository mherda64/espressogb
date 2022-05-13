package ppu;

public class TileDisplay extends Display implements Runnable, UtilDisplay {

    public static final int DISPLAY_WIDTH = 128;

    public static final int DISPLAY_HEIGHT = 192;

    private final Tiles tiles;
    private final GPURegsManager regsManager;

    public TileDisplay(Tiles tiles, GPURegsManager regsManager, int scale) {
        super(DISPLAY_WIDTH, DISPLAY_HEIGHT, scale);
        this.tiles = tiles;
        this.regsManager = regsManager;
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

    @Override
    public void updateMap() {
        var x = 0;
        var y = 0;
        for (int i = 0; i < 384; i++) {
            drawTile(x * 8 * scale, y * 8 * scale, tiles.getTileMap()[i]);
            x += 1;
            if (x >= 16) {
                x = 0;
                y += 1;
            }
        }
        requestRefresh();
    }
}