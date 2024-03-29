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
        int scx = addressSpace.get(GPURegs.SCX.address);
//        System.out.println(scx);
        return scx;
    }

    public int getWY() {
        return addressSpace.get(GPURegs.WY.address);
    }

    public int getWX() {
        return addressSpace.get(GPURegs.WX.address);
    }

    public boolean isBackgroundEnabled() {
        return BitUtils.getByteBit(addressSpace.get(GPURegs.LCDC.address), 0);
    }

    public boolean isSpritesEnabled() {
        return BitUtils.getByteBit(addressSpace.get(GPURegs.LCDC.address), 1);
    }

    public boolean isDoubleSpritesEnabled() {
        return BitUtils.getByteBit(addressSpace.get(GPURegs.LCDC.address), 2);
    }

    public boolean isBackgroundMap() {
        return BitUtils.getByteBit(addressSpace.get(GPURegs.LCDC.address), 3);
    }

    public boolean isBackgroundTiles() {
        return !BitUtils.getByteBit(addressSpace.get(GPURegs.LCDC.address), 4);
    }

    public boolean isWindowEnabled() {
        return BitUtils.getByteBit(addressSpace.get(GPURegs.LCDC.address), 5);
    }

    public boolean useSecondMapForWindow() {
        return BitUtils.getByteBit(addressSpace.get(GPURegs.LCDC.address), 6);
    }

    public boolean isLCDOn() {
        return BitUtils.getByteBit(addressSpace.get(GPURegs.LCDC.address), 7);
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
        return r << 16 | g << 8 | b;
    }

    public void updateStatMode(Mode mode) {
        // TODO: update two read only fields not via set method
        addressSpace.set(GPURegs.STAT.address, (addressSpace.get(GPURegs.STAT.address) & 0xFC) | mode.statIndex);
    }

    public void updateStatLineCountersEqualFlag(boolean value) {
        addressSpace.set(GPURegs.STAT.address, (addressSpace.get(GPURegs.STAT.address) & 0xFB) | (value ? 1 : 0) << 2);
    }

    public boolean statLineCountEqualInterruptEnabled() {
        var stat = addressSpace.get(GPURegs.STAT.address);
//        System.out.println(stat);
//        System.out.println((stat & 0x80) > 0);
        return (stat & 0x40) > 0;
    }

    public boolean statOAMInterruptSourceEnabled() {
        return (addressSpace.get(GPURegs.STAT.address) & 0x20) > 0;
    }

    public boolean statVBlankInterruptSourceEnabled() {
        return (addressSpace.get(GPURegs.STAT.address) & 0x10) > 0;
    }

    public boolean statHBlankInterruptSourceEnabled() {
        return (addressSpace.get(GPURegs.STAT.address) & 0x08) > 0;
    }
}
