package ppu;

public enum Mode {
    OAM_READ(20),
    VRAM_READ(43),
    HBLANK(51),
    VBLANK(114);

    int clocks;

    Mode(int clocks) {
        this.clocks = clocks;
    }
}
