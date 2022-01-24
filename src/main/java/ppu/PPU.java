package ppu;

public class PPU {

    Mode currentMode;
    int lineCounter;
    int currentModeClockCounter;

    public PPU() {
        this.currentMode = Mode.OAM_READ;
        this.lineCounter = 0;
        this.currentModeClockCounter = 0;
    }

    public void step(int clocks) {

    }
}
