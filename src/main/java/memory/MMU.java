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
import static memory.RamMode.RAM;
import static memory.RamMode.ROM;

public class MMU implements AddressSpace {

    private boolean afterBios = false;

    private final BasicMemory bios;
    private final BasicMemory loadedROM;
    private final SwitchableROM[] switchableROMs;
    private final VideoRAM videoRAM;
    private final ExternalRAM[] switchableRAMs;
    private final WorkingRAM workingRAM;
    private final OAM oam;
    private final HighMem highMem;

    private int ramBank = 0x0;
    private int romBank = 0x1;
    private RamMode ramMode = ROM;
    private final MBC mbc;

    public MMU(InputManager inputManager, SpriteManager spriteManager, Tiles tiles, String romPath, String biosPath) throws IOException {
        bios = loadFile(biosPath);
        loadedROM = loadFile(romPath);
        mbc = MBC.getMBC(loadedROM.get(0x0147));

        switchableROMs = new SwitchableROM[mbc.getMaxRomSize()];
        for (int i = 0; i < mbc.getMaxRomSize(); i++) {
            switchableROMs[i] = i == 0 ? new SwitchableROM(0x0) : new SwitchableROM(0x4000);
            loadRomBank(i);
        }

        switchableRAMs = new ExternalRAM[mbc.getMaxRamSize()];
        for (int i = 0; i < mbc.getMaxRamSize(); i++)
            switchableRAMs[i] = new ExternalRAM();

        videoRAM = new VideoRAM(tiles);
        workingRAM = new WorkingRAM();
        oam = new OAM(spriteManager);
        highMem = new HighMem(inputManager);

    }

    private void loadRomBank(int index) {
        var romSize = loadedROM.getSize();
        if (index == 0) {
            for (int i = 0; i < 0x4000 && i < romSize; i++) {
                switchableROMs[index].set(i, loadedROM.forceGet(i));
            }
        } else {
            for (int i = 0x4000; i < (0x4000 * 2) && ((index - 1) * 0x4000 + i) < romSize; i++) {
                switchableROMs[index].set(i, loadedROM.forceGet((index - 1) * 0x4000 + i));
            }
        }

    }

    public void setAfterBios() {
        afterBios = true;
    }

    @Override
    public void set(int address, int value) {
        isShort(address);
        isByte(value);

        // OAM DMA
        if (address == 0xFF46) {
            var destAdress = (value << 8) & 0xFF00;
            for (int i = 0; i < 160; i++) {
                set(0xFE00 + i, get(destAdress + i));
            }
        }

        if (address <= 0x3FFF) {
            if (!afterBios) {
                switchableROMs[0].set(address, value);
            } else if (mbc != MBC.NONE && address >= 0x2000) {
                // ROM bank setting
                switch (value & 0x1F) {
                    case 0x0:
                        romBank = (romBank & 0x60) | 0x01;
                        break;
                    case 0x20:
                        romBank = (romBank & 0x60) | 0x21;
                        break;
                    case 0x40:
                        romBank = (romBank & 0x60) | 0x41;
                        break;
                    case 0x60:
                        romBank = (romBank & 0x60) | 0x61;
                        break;
                    default:
                        romBank = (romBank & 0x60) | (value & 0x1F);
                }
            }
        } else if (address <= 0x7FFF) {
            if (!afterBios) {
                switchableROMs[romBank].set(address, value);
            } else if (mbc != MBC.NONE && address <= 0x5FFF) {
                // RAM bank setting
                switch (ramMode) {
                    case ROM:
                        romBank = (romBank & 0x1F) | ((value & 0x3) << 5);
                        break;
                    case RAM:
                        ramBank = value & 0x3;
                        break;
                }

            } else {
                ramMode = (value & 0x1) > 0 ? RAM : ROM;
            }
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

        if (!afterBios && address < 0x0100) {
            return bios.get(address);
        } else if (address <= 0x3FFF) {
            return switchableROMs[0].get(address);
        } else if (address <= 0x7FFF) {
            return switchableROMs[romBank].get(address);
        } else if (address <= 0x9FFF) {
            return videoRAM.get(address);
        } else if (address <= 0xBFFF) {
            return switchableRAMs[ramBank].get(address);
        } else if (address <= 0xDFFF) {
            return workingRAM.get(address);
        } else if (address <= 0xFDFF) {
            return workingRAM.get(address - 0x2000);
        } else if (address <= 0xFE9F) {
            return oam.get(address);
        } else if (address >= 0xFF00 && address <= 0xFFFF) {
            return highMem.get(address);
        } else {
            throw new IllegalStateException((String.format("Trying to read address: %04X", address)));
        }
    }

    private BasicMemory loadFile(String romPath) throws IOException {
        var memory = new BasicMemory((int) Files.size(Paths.get(romPath)) + 1);
        byte[] program = Files.readAllBytes(new File(romPath).toPath());

        var pointer = 0x00;
        for (byte x : program) {
            memory.forceSet(pointer, x & 0xFF);
            pointer++;
        }
        return memory;
    }
}
