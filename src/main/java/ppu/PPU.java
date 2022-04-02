package ppu;

import memory.AddressSpace;

public class PPU {

    private final Tiles tiles;
    private final Display display;
    private final AddressSpace addressSpace;
    private final GPURegsManager regsManager;
    private Mode currentMode;
    private int lineCounter;
    private int currentModeClockCounter;

    public PPU(AddressSpace addressSpace, Display display, Tiles tiles) {
        this.tiles = tiles;
        this.addressSpace = addressSpace;
        this.display = display;
        this.regsManager = new GPURegsManager(addressSpace);
        this.currentMode = Mode.OAM_READ;
        this.lineCounter = 0;
        this.currentModeClockCounter = 0;
    }

    public void step(int clocks) {
        currentModeClockCounter += clocks;

        switch (currentMode) {
            case OAM_READ:
                if (currentModeClockCounter >= Mode.OAM_READ.clocks) {
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
                        display.requestRefresh();
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
                    }

                }
                break;
        }
        addressSpace.set(0xFF44, lineCounter);
    }

    private void clearScreen() {
        for (int x = 0; x < 144; x++) {
            for (int y = 0; y < 160; y++)
                display.setPixel(x, y, 0);
        }
    }

    private void drawLine() {
        // Offset for the tile map #0 or #1
        var mapOffset = regsManager.isBackgroundMap() ? 0x1C00 : 0x1800;

        // Which line of tiles to use in the map
        // divided by 8 as that's the height of a tile
        mapOffset += 32 * (((lineCounter + regsManager.getSCY()) & 0xFF) >> 3);

        // Which tile to start with in the tile line
        var lineOffset = regsManager.getSCX() >> 3;

        // Which line of pixels to use in the destination tile
        var y = (lineCounter + regsManager.getSCY()) & 7;

        // Which pixel to use in the pixel line
        var x = regsManager.getSCX() & 7;

//        var displayOffset = 160 * lineCounter;

        var tileMapAddress = 0x8000 + mapOffset + lineOffset;
        var tile = addressSpace.get(tileMapAddress);
        if (regsManager.isBackgroundTiles() && tile < 128) tile += 256;

        var tilemap = tiles.getTileMap();

        for (int i = 0; i < 160; i++) {
//            var colour = PALETTE[tilemap[tile][y][x]];
            var colour = regsManager.getPalette()[tilemap[tile][y][x]];
            display.setPixel(lineCounter, i, colour);
//            display.setPixel(displayOffset, colour);
//            displayOffset += 1;
            x += 1;

            if (x == 8) {
                x = 0;
                lineOffset = (lineOffset + 1) & 31;
                tileMapAddress = 0x8000 + mapOffset + lineOffset;
                tile = addressSpace.get(tileMapAddress);
                if (regsManager.isBackgroundTiles() && tile < 128) tile += 256;
            }
        }

    }
}
