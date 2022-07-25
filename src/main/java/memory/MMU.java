package memory;

import input.InputManager;
import memory.regions.*;
import ppu.Tiles;
import ppu.oam.SpriteManager;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static cpu.BitUtils.isByte;
import static cpu.BitUtils.isShort;
import static memory.RamMode.ROM;

public class MMU implements AddressSpace {

    private boolean romInitialized = false;

    private final BasicMemory loadedROM;
    private final FixedROM fixedROM;
    private final SwitchableROM[] switchableROMs;
    private final VideoRAM videoRAM;
    private final ExternalRAM[] switchableRAMs;
    private final WorkingRAM workingRAM;
    private final OAM oam;
    private final HighMem highMem;

    private final int ramBank = 0x1;
    private final int romBank = 0x0;
    private final RamMode mode = ROM;

    public MMU(InputManager inputManager, SpriteManager spriteManager, Tiles tiles, String romPath, String biosPath) throws IOException {
        loadedROM = loadFullGame(romPath);
        var mbc = MBC.getMBC(loadedROM.get(0x0147));

        fixedROM = new FixedROM();

        switchableROMs = new SwitchableROM[mbc.getMaxRomSize()];
        for (int i = 0; i < mbc.getMaxRomSize(); i++)
            switchableROMs[i] = new SwitchableROM();

        switchableRAMs = new ExternalRAM[mbc.getMaxRamSize()];
        for (int i = 0; i < mbc.getMaxRamSize(); i++)
            switchableRAMs[i] = new ExternalRAM();

        videoRAM = new VideoRAM(tiles);
        workingRAM = new WorkingRAM();
        oam = new OAM(spriteManager);
        highMem = new HighMem(inputManager);

        loadGame();
        loadBios(biosPath);
    }

    public void loadGameAndInitialize() {
        loadGame();
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
            switchableROMs[romBank].set(address, value);
        } else if (address <= 0x9FFF) {
            videoRAM.set(address, value);
        } else if (address <= 0xBFFF) {
            switchableRAMs[ramBank].set(address, value);
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
            return switchableROMs[romBank].get(address);
        } else if (address <= 0x9FFF) {
            return videoRAM.get(address);
        } else if (address <= 0xBFFF) {
            return switchableRAMs[ramBank].get(address);
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

    private void loadBios(String biosPath) throws IOException {
        byte[] program = Files.readAllBytes(new File(biosPath).toPath());

        var pointer = 0x00;
        for (byte x : program) {
            set(pointer, x & 0xFF);
            pointer++;
        }
    }

    private void loadGame() {
        for (int i = 0; i < 0x8000; i++)
            set(i, loadedROM.get(i));
    }

    private BasicMemory loadFullGame(String romPath) throws IOException {
        var memory = new BasicMemory((int) Files.size(Paths.get(romPath)));
        byte[] program = Files.readAllBytes(new File(romPath).toPath());

        var pointer = 0x00;
        for (byte x : program) {
            memory.set(pointer, x & 0xFF);
            pointer++;
        }
        return memory;
    }
}
