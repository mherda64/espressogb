package ppu;

public enum Mode {
    OAM_READ(20, 0x02),
    VRAM_READ(43, 0x03),
    HBLANK(51, 0x00),
    VBLANK(114, 0x01);

    final int clocks;
    final int statIndex;

    Mode(int clocks, int statIndex) {
        this.clocks = clocks;
        this.statIndex = statIndex;
    }
}
