package ppu;

import memory.AddressSpace;

public class PPU {

    // TODO: varying
    private boolean backgroundMap = false;
    // TODO: varying
    private boolean backgroundTile = false;

    public final int[] PALETTE = {
            getColor(255, 255, 255),
            getColor(192, 192, 192),
            getColor(96, 96, 96),
            0
    };

    private final Tiles tiles;
    private final Display display;
    private final AddressSpace addressSpace;
    private Mode currentMode;
    private int lineCounter;
    private int currentModeClockCounter;

    public PPU(AddressSpace addressSpace, Display display, Tiles tiles) {
        this.tiles = tiles;
        this.addressSpace = addressSpace;
        this.display = display;
        this.currentMode = Mode.OAM_READ;
        this.lineCounter = 0;
        this.currentModeClockCounter = 0;
    }

    // TODO: Implement Pixel FIFO rather than drawing whole HBLANK after certain amount of time
    public void step(int clocks) {
        currentModeClockCounter += clocks;

        switch (currentMode) {
            case OAM_READ:
                if (currentModeClockCounter >= Mode.OAM_READ.clocks) {
                    //TODO set to zero or rather subtract??
                    currentModeClockCounter -= Mode.OAM_READ.clocks;
                    currentMode = Mode.VRAM_READ;
                }
                break;

            case VRAM_READ:
                if (currentModeClockCounter >= Mode.VRAM_READ.clocks) {
                    currentModeClockCounter -= Mode.VRAM_READ.clocks;
                    currentMode = Mode.HBLANK;

                    drawLine();
                }
                break;

            case HBLANK:
                if (currentModeClockCounter >= Mode.HBLANK.clocks) {
                    currentModeClockCounter -= Mode.HBLANK.clocks;
                    lineCounter++;

                    if (lineCounter == 143) {
                        currentMode = Mode.VBLANK;
                        //TODO put image data
                    } else {
                        currentMode = Mode.OAM_READ;
                    }
                }
                break;

            case VBLANK:
                if (currentModeClockCounter >= Mode.VBLANK.clocks) {
                    currentModeClockCounter -= Mode.VBLANK.clocks;
                    lineCounter++;

                    if (lineCounter > 153) {
                        currentMode = Mode.OAM_READ;
                        lineCounter = 0;
//                        clearScreen();
                    }

                }
                break;
        }
        addressSpace.set(0xFF44, lineCounter);
    }

    private int getSCY() {
//        return 0;
        var scy = addressSpace.get(GPURegs.SCY.address);
//        System.out.println("SCY:" + scy);
        return scy;
    }

    private int getSCX() {
//        return 0;
        var scx = addressSpace.get(GPURegs.SCX.address);
//        System.out.println("SCX:" + scx);
        return scx;
    }

    private static int getColor(int r, int g, int b) {
        return 0xFF | r << 16 | g << 8 | b;
    }

    private void clearScreen() {
        for (int x = 0; x < 144; x++) {
            for (int y = 0; y < 160; y++)
                display.setPixel(x, y, 0);
        }
    }

    private void drawLine() {
        // Offset for the tile map #0 or #1
        var mapOffset = backgroundMap ? 0x1C00 : 0x1800;

        // Which line of tiles to use in the map
        // divided by 8 as that's the height of a tile
        mapOffset += 32 * (((lineCounter + getSCY()) & 0xFF) >> 3);

        // Which tile to start with in the tile line
        var lineOffset = getSCX() >> 3;

        // Which line of pixels to use in the destination tile
        var y = (lineCounter + getSCY()) & 7;

        // Which pixel to use in the pixel line
        var x = getSCX() & 7;

//        var displayOffset = 160 * lineCounter;

        var tileMapAddress = 0x8000 + mapOffset + lineOffset;
        var tile = addressSpace.get(tileMapAddress);
        if (backgroundTile && tile < 128) tile += 256;

        var tilemap = tiles.getTileMap();

        for (int i = 0; i < 160; i++) {
            var colour = PALETTE[tilemap[tile][y][x]];
            display.setPixel(lineCounter, i, colour);
//            display.setPixel(displayOffset, colour);
//            displayOffset += 1;
            x += 1;

            if (x == 8) {
                x = 0;
                lineOffset = (lineOffset + 1) & 31;
                tileMapAddress = 0x8000 + mapOffset + lineOffset;
                tile = addressSpace.get(tileMapAddress);
                if (backgroundTile && tile < 128) tile += 256;
            }
        }

    }
}
