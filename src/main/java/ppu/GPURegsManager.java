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

    private int[] palette = new int[4];

//    private boolean switchBackground = false;
//    private boolean backgroundMap = false;
//    private boolean backgroundTiles = false;
//    private boolean switchLCD = false;

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

    public boolean isBackgroundMap() {
        return BitUtils.getByteBit(addressSpace.get(GPURegs.LDCD.address), 3);
    }

    public boolean isBackgroundTiles() {
        return !BitUtils.getByteBit(addressSpace.get(GPURegs.LDCD.address), 4);
    }

    public int[] getPalette() {
        var paletteReg = addressSpace.get(GPURegs.BGP.address);
        for (int i = 0; i < 4; i++) {
            palette[i] = COLORS[(paletteReg >> (i * 2)) & 3];
        }
        return palette;
    }

    private static int getColor(int r, int g, int b) {
        return 0xFF | r << 16 | g << 8 | b;
    }
}
