package ppu.oam;


public class SpriteManager {

    private final Sprite[] sprites = new Sprite[40];

    public SpriteManager() {
        for (int i = 0; i < 40; i++)
            sprites[i] = new Sprite();
    }

    public Sprite[] getSprites() {
        return sprites;
    }

    public void updateSprite(int address, int value) {
        var index = (address & 0x00FF) >> 2;
        if (index >= 40)
            throw new IllegalStateException("Sprite index exceeded!");

        switch (address & 0x3) {
            case 0:
                sprites[index].setYPos(value - 16);
                break;
            case 1:
                sprites[index].setXPos(value - 8);
                break;
            case 2:
                sprites[index].setTile(value);
                break;
            case 3:
                sprites[index].setAboveBgPriority((value & 0x80) > 0);
                sprites[index].setYFlip((value & 0x40) > 0);
                sprites[index].setXFlip((value & 0x20) > 0);
                sprites[index].setSecondPalette((value & 0x10) > 0);
                break;
            default:
                throw new IllegalStateException("Sprite index exceeded!");
        }
    }
}
