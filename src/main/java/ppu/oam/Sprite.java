package ppu.oam;

public class Sprite {
    private int yPos;
    private int xPos;
    private int tile;
    private boolean aboveBgPriority;
    private boolean yFlip;
    private boolean xFlip;
    private boolean secondPalette;

    public Sprite() {
        xPos = -16;
        yPos = -8;
        tile = 0;
        secondPalette = false;
        xFlip = false;
        yFlip = false;
        aboveBgPriority = false;
    }

    public int getYPos() {
        return yPos;
    }

    public void setYPos(int yPos) {
        this.yPos = yPos;
    }

    public int getXPos() {
        return xPos;
    }

    public void setXPos(int xPos) {
        this.xPos = xPos;
    }

    public int getTile() {
        return tile;
    }

    public void setTile(int tile) {
        this.tile = tile;
    }

    public boolean isAboveBgPriority() {
        return aboveBgPriority;
    }

    public void setAboveBgPriority(boolean aboveBgPriority) {
        this.aboveBgPriority = aboveBgPriority;
    }

    public boolean isYFlip() {
        return yFlip;
    }

    public void setYFlip(boolean yFlip) {
        this.yFlip = yFlip;
    }

    public boolean isXFlip() {
        return xFlip;
    }

    public void setXFlip(boolean xFlip) {
        this.xFlip = xFlip;
    }

    public boolean isSecondPalette() {
        return secondPalette;
    }

    public void setSecondPalette(boolean secondPalette) {
        this.secondPalette = secondPalette;
    }
}
