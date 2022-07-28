package ppu;

import cpu.InterruptRegs;
import memory.AddressSpace;
import ppu.oam.Sprite;
import ppu.oam.SpriteManager;

public class PPU {

    private final Tiles tiles;
    private final Display display;
    private final AddressSpace addressSpace;
    private final GPURegsManager regsManager;
    private final SpriteManager spriteManager;
    private Mode currentMode;
    private int lineCounter;
    private int currentModeClockCounter;
    private int[] scanRow;

    public PPU(AddressSpace addressSpace, GPURegsManager regsManager, SpriteManager spriteManager, Display display, Tiles tiles) {
        this.tiles = tiles;
        this.addressSpace = addressSpace;
        this.display = display;
        this.regsManager = regsManager;
        this.spriteManager = spriteManager;
        this.currentMode = Mode.OAM_READ;
        this.lineCounter = 0;
        this.currentModeClockCounter = 0;
        scanRow = new int[160];
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
                        addressSpace.set(InterruptRegs.IF.getAddress(), addressSpace.get(InterruptRegs.IF.getAddress()) | 0x01);
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
        addressSpace.set(GPURegs.LY.address, lineCounter);
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

        var tileMapAddress = 0x8000 + mapOffset + lineOffset;
        var tileIndex = addressSpace.get(tileMapAddress);
        if (regsManager.isBackgroundTiles() && tileIndex < 128) tileIndex += 256;

        var tilemap = tiles.getTileMap();
        var bgPalette = regsManager.getBGPalette();


        for (int i = 0; i < 160; i++) {
            scanRow[i] = tilemap[tileIndex][y][x];
            var colour = bgPalette[tilemap[tileIndex][y][x]];
            display.setPixel(lineCounter, i, colour);
            x += 1;

            if (x == 8) {
                x = 0;
                lineOffset = (lineOffset + 1) & 31;
                tileMapAddress = 0x8000 + mapOffset + lineOffset;
                tileIndex = addressSpace.get(tileMapAddress);
                if (regsManager.isBackgroundTiles() && tileIndex < 128) tileIndex += 256;
            }
        }

        if (regsManager.isSpritesEnabled()) {
            var sprites = spriteManager.getSprites();
            var doubleSprites = regsManager.isDoubleSpritesEnabled();
            var spriteHeight = doubleSprites ? 16 : 8;

            for (var sprite : sprites) {
                // If sprite is in line
                if (sprite.getYPos() <= lineCounter && sprite.getYPos() + spriteHeight > lineCounter) {
                    int[][] tile;
                    int[] tileRow;
                    var spriteTileIndex = sprite.getTile();

                    if (doubleSprites) {
                        var topTileIndex = spriteTileIndex & 0xFE;
                        var bottomTileIndex = spriteTileIndex | 0x01;

                        if (lineCounter - sprite.getYPos() <= 7 && !sprite.isYFlip() ||
                                lineCounter - sprite.getYPos() > 7 && sprite.isYFlip()) {
                            tile = tilemap[topTileIndex];
                        }
                        else {
                            tile = tilemap[bottomTileIndex];
                        }

                        if (lineCounter - sprite.getYPos() > 7) {
                            tileRow = sprite.isYFlip()
                                    ? tile[7 - lineCounter + sprite.getYPos() + 8]
                                    : tile[lineCounter - sprite.getYPos() - 8];
                        } else {
                            tileRow = sprite.isYFlip()
                                    ? tile[7 - lineCounter + sprite.getYPos()]
                                    : tile[lineCounter - sprite.getYPos()];
                        }
                    } else {
                        tile = tilemap[spriteTileIndex];
                        tileRow = sprite.isYFlip()
                                ? tile[7 - lineCounter + sprite.getYPos()]
                                : tile[lineCounter - sprite.getYPos()];
                    }

                    drawTileRow(sprite, tileRow);
                }
            }
        }
    }

    private void drawTileRow(Sprite sprite, int[] tileRow) {
        var palette = sprite.isSecondPalette()
                ? regsManager.getSecondObjPalette()
                : regsManager.getFirstObjPalette();

        for (int tileCol = 0; tileCol < 8; tileCol++) {
            var destCol = sprite.getXPos() + tileCol;
            if (destCol >= 0
                    && destCol < 160
                    && (tileRow[tileCol] > 0 && !sprite.isXFlip() || tileRow[7 - tileCol] > 0 && sprite.isXFlip())
                    && (!sprite.isAboveBgPriority() || scanRow[destCol] == 0)) {
                var colour = palette[tileRow[sprite.isXFlip() ? 7 - tileCol : tileCol]];
                display.setPixel(lineCounter, destCol, colour);
            }
        }
    }
}
