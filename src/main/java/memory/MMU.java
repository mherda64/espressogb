package memory;

import input.InputManager;
import memory.regions.*;
import ppu.Tiles;
import ppu.oam.SpriteManager;

import static cpu.BitUtils.isByte;
import static cpu.BitUtils.isShort;

public class MMU implements AddressSpace {

    private boolean romInitialized = false;

    private final FixedROM fixedROM;
    private final SwitchableROM switchableROM;
    private final VideoRAM videoRAM;
    private final ExternalRAM externalRAM;
    private final WorkingRAM workingRAM;
    private final OAM oam;
    private final HighMem highMem;

    public MMU(InputManager inputManager, SpriteManager spriteManager, Tiles tiles) {
        fixedROM = new FixedROM();
        switchableROM = new SwitchableROM();
        videoRAM = new VideoRAM(tiles);
        externalRAM = new ExternalRAM();
        workingRAM = new WorkingRAM();
        oam = new OAM(spriteManager);
        highMem = new HighMem(inputManager);
    }

    public void initializeROM() {
        romInitialized = true;
    }

    @Override
    public void set(int address, int value) {
        isShort(address);
        isByte(value);

        if (romInitialized && address <= 0x3FFF) {
            System.out.println((String.format("Trying to write to ROM memory PC: %04X bank 0, skipping...", address)));
            return;
        }

        if (romInitialized && address >= 0x4000 && address <= 0x7FFF) {
            System.out.println((String.format("Trying to write to ROM memory PC: %04X bank N, skipping...", address)));
            return;
        }

        // OAM DMA
        if (address == 0xFF46) {
            var destAdress = (value << 8) & 0xFF00;
            for (int i = 0; i < 160; i++) {
                set(0xFE00 + i, get(destAdress + i));
            }
        }

        if (address <= 0x3FFF) {
            fixedROM.set(address, value);
        } else if (address <= 0x7FFF) {
            switchableROM.set(address, value);
        } else if (address <= 0x9FFF) {
            videoRAM.set(address, value);
        } else if (address <= 0xBFFF) {
            externalRAM.set(address, value);
        } else if (address <= 0xDFFF) {
            workingRAM.set(address, value);
        } else if (address >= 0xFE00 && address <= 0xFE9F) {
            oam.set(address, value);
        } else if (address >= 0xFF00 && address <= 0xFFFF) {
            highMem.set(address, value);
        } else
            System.out.println((String.format("Trying to write to address: %04X, skipping...", address)));
    }

    @Override
    public int get(int address) {
        isShort(address);

        if (address <= 0x3FFF) {
            return fixedROM.get(address);
        } else if (address <= 0x7FFF) {
            return switchableROM.get(address);
        } else if (address <= 0x9FFF) {
            return videoRAM.get(address);
        } else if (address <= 0xBFFF) {
            return externalRAM.get(address);
        } else if (address <= 0xDFFF) {
            return workingRAM.get(address);
        } else if (address >= 0xFE00 && address <= 0xFE9F) {
            return oam.get(address);
        } else if (address >= 0xFF00 && address <= 0xFFFF) {
            return highMem.get(address);
        } else {
            throw new IllegalStateException((String.format("Trying to read address: %04X", address)));
        }
    }

}
