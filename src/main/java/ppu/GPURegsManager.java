package ppu;

import cpu.BitUtils;
import memory.AddressSpace;

public class GPURegsManager {

    private final int[] COLORS = {
            getColor(255, 255, 255),
            getColor(192, 192, 192),
            getColor(96, 96, 96),
            0
    };

    private final int[] bgPalette = new int[4];
    private final int[] firstObjPalette = new int[4];
    private final int[] secondObjPalette = new int[4];

    private final AddressSpace addressSpace;

    public GPURegsManager(AddressSpace addressSpace) {
        this.addressSpace = addressSpace;
    }

    public int getSCY() {
        return addressSpace.get(GPURegs.SCY.address);
    }

    public int getSCX() {
        return addressSpace.get(GPURegs.SCX.address);
    }

    public boolean isSpritesEnabled() {
        return BitUtils.getByteBit(addressSpace.get(GPURegs.LCDC.address), 1);
    }

    public boolean isBackgroundMap() {
        return BitUtils.getByteBit(addressSpace.get(GPURegs.LCDC.address), 3);
    }

    public boolean isBackgroundTiles() {
        return !BitUtils.getByteBit(addressSpace.get(GPURegs.LCDC.address), 4);
    }

    // TODO: update palettes in the future only when necessary
    public int[] getBGPalette() {
        var paletteReg = addressSpace.get(GPURegs.BGP.address);
        for (int i = 0; i < 4; i++) {
            bgPalette[i] = COLORS[(paletteReg >> (i * 2)) & 3];
        }
        return bgPalette;
    }

    public int[] getFirstObjPalette() {
        var paletteReg = addressSpace.get(GPURegs.OBJ_PAL_1.address);
        for (int i = 0; i < 4; i++) {
            firstObjPalette[i] = COLORS[(paletteReg >> (i * 2)) & 3];
        }
        return firstObjPalette;
    }

    public int[] getSecondObjPalette() {
        var paletteReg = addressSpace.get(GPURegs.OBJ_PAL_2.address);
        for (int i = 0; i < 4; i++) {
            secondObjPalette[i] = COLORS[(paletteReg >> (i * 2)) & 3];
        }
        return secondObjPalette;
    }

    private static int getColor(int r, int g, int b) {
        return 0xFF | r << 16 | g << 8 | b;
    }
}
