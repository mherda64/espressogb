package ppu;

import cpu.interrupt.InterruptEnum;
import cpu.interrupt.InterruptRegs;
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
                    setCurrentMode(Mode.VRAM_READ);
                }
                break;

            case VRAM_READ:
                if (currentModeClockCounter >= Mode.VRAM_READ.clocks) {
                    currentModeClockCounter -= Mode.VRAM_READ.clocks;
                    setCurrentMode(Mode.HBLANK);

                    //        LY == LYC interrupt
                    var lineCountersEqual = addressSpace.get(GPURegs.LY.address) == addressSpace.get(GPURegs.LYC.address);
                    regsManager.updateStatLineCountersEqualFlag(lineCountersEqual);
                    if (regsManager.statLineCountEqualInterruptEnabled() && lineCountersEqual) {
                        addressSpace.set(InterruptRegs.IF.getAddress(),
                                addressSpace.get(InterruptRegs.IF.getAddress()) | InterruptEnum.LCD_STAT.get());
                    }

                    drawLine();
                }
                break;

            case HBLANK:
                if (currentModeClockCounter >= Mode.HBLANK.clocks) {
                    currentModeClockCounter -= Mode.HBLANK.clocks;
                    lineCounter++;

                    if (lineCounter == 143) {
                        setCurrentMode(Mode.VBLANK);
//                        VBLANK interrupt
                        addressSpace.set(InterruptRegs.IF.getAddress(),
                                addressSpace.get(InterruptRegs.IF.getAddress()) | InterruptEnum.VBLANK.get());
                        display.requestRefresh();
                    } else {
                        setCurrentMode(Mode.OAM_READ);
                    }
                }
                break;

            case VBLANK:
                if (currentModeClockCounter >= Mode.VBLANK.clocks) {
                    currentModeClockCounter -= Mode.VBLANK.clocks;
                    lineCounter++;

                    if (lineCounter > 153) {
                        setCurrentMode(Mode.OAM_READ);
                        lineCounter = 0;
                    }

                }
                break;
        }
        addressSpace.set(GPURegs.LY.address, lineCounter);

    }

    private void setCurrentMode(Mode mode) {
        currentMode = mode;
        regsManager.updateStatMode(mode);

//        OAM, HBLANK, VBLANK STAT interrupts
        switch (mode) {
            case OAM_READ:
                if (regsManager.statOAMInterruptSourceEnabled()) {
                    addressSpace.set(InterruptRegs.IF.getAddress(),
                            addressSpace.get(InterruptRegs.IF.getAddress()) | InterruptEnum.LCD_STAT.get());
                }
                break;
            case HBLANK:
                if (regsManager.statHBlankInterruptSourceEnabled()) {
                    addressSpace.set(InterruptRegs.IF.getAddress(),
                            addressSpace.get(InterruptRegs.IF.getAddress()) | InterruptEnum.LCD_STAT.get());
                }
                break;
            case VBLANK:
                if (regsManager.statVBlankInterruptSourceEnabled()) {
                    addressSpace.set(InterruptRegs.IF.getAddress(),
                            addressSpace.get(InterruptRegs.IF.getAddress()) | InterruptEnum.LCD_STAT.get());
                }
                break;
        }
    }

    private void drawLine() {
        var tilemap = tiles.getTileMap();

        drawBackgroundLine(tilemap, regsManager.getSCY(), regsManager.getSCX(), regsManager.isBackgroundMap(), false);

        if (regsManager.isWindowEnabled()) {
            var windowY = regsManager.getWY();
            var windowX = regsManager.getWX() - 7;
            if (lineCounter >= windowY) {
                drawBackgroundLine(tilemap, windowY, windowX, regsManager.useSecondMapForWindow(), true);
            }
        }

        if (regsManager.isSpritesEnabled()) {
            drawSpriteLine(tilemap);
        }
    }

    private void drawBackgroundLine(int[][][] tilemap, int initY, int initX, boolean useSecondMap, boolean drawWindow) {
        var bgPalette = regsManager.getBGPalette();

        // Offset for the tile map #0 or #1
        var mapOffset = useSecondMap ? 0x1C00 : 0x1800;

        // Which line of tiles to use in the map
        // divided by 8 as that's the height of a tile
        if (drawWindow) {
            mapOffset += 32 * (((lineCounter - initY) & 0xFF) >> 3);
        } else {
            mapOffset += 32 * (((lineCounter + initY) & 0xFF) >> 3);
        }

        // Which tile to start with in the tile line
        var lineOffset = 0;
        if (!drawWindow) {
            lineOffset = initX >> 3;
        }

        // Which line of pixels to use in the destination tile
        var y = 0;
        if (drawWindow) {
            y = (lineCounter - initY) & 7;
        } else {
            y = (lineCounter + initY) & 7;
        }

        // Which pixel to use in the pixel line
        var x = 0;
        if (!drawWindow) {
            x = initX & 7;
        }

        var tileMapAddress = 0x8000 + mapOffset + lineOffset;
        var tileIndex = addressSpace.get(tileMapAddress);
        if (regsManager.isBackgroundTiles() && tileIndex < 128) tileIndex += 256;

        for (int i = 0; i < 160; i++) {
            if (drawWindow && i < initX) {
                continue;
            }
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
    }

    private void drawSpriteLine(int[][][] tilemap) {
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
                    } else {
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
