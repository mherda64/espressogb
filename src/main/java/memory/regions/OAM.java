package memory.regions;

import ppu.oam.SpriteManager;

public class OAM extends BaseMemory {

    private final SpriteManager spriteManager;

    public OAM(SpriteManager spriteManager) {
        // 160B
        super(0x00A0, 0xFE00);
        this.spriteManager = spriteManager;
    }

    @Override
    public void set(int address, int value) {
        memory[address - firstAddress] = value;
        spriteManager.updateSprite(address, value);
    }

}
