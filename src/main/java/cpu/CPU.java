package cpu;

import cpu.instruction.Instructions;
import input.InputManager;
import memory.AddressSpace;
import memory.Memory;
import ppu.*;
import ppu.oam.SpriteManager;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class CPU {

    private Registers registers;
    private AddressSpace memory;

    private int cycleCounter;

    private final int freq = 2000;

    private static final int DISPLAY_SCALE = 5;
    private static final int TILESET_SCALE = 2;
    private static final int BGMAP_SCALE = 2;

    public CPU(Registers registers, Memory memory) {
        this.registers = registers;
        this.memory = memory;
    }

    public int loadFile(String path, int pointer) throws IOException {
        byte[] program = Files.readAllBytes(new File(path).toPath());

        pointer = 0x00;
        for (byte x : program) {
            memory.set(pointer, x & 0xFF);
            pointer++;
        }
        return program.length;
    }

    public int addCycles(int cycles) {
        cycleCounter += cycles;
        return cycleCounter;
    }

    public int getCycleCounter() {
        return cycleCounter;
    }

    public static void main(String[] args) throws IOException {
        var registers = new Registers();
        var memory = new Memory(0x10000);
        var gpuRegsManager = new GPURegsManager(memory);
        var cpu = new CPU(registers, memory);

        var inputManager = new InputManager();
        memory.setInputManager(inputManager);

        var tiles = new Tiles(memory);
        var sprites = new SpriteManager();
        memory.setTiles(tiles);
        memory.setSprites(sprites);

        var display = new Display(DISPLAY_SCALE);
        display.setPreferredSize(new Dimension(160 * DISPLAY_SCALE, 144 * DISPLAY_SCALE));

        var tileSetDisplay = new TileDisplay(tiles, gpuRegsManager, TILESET_SCALE);
        tileSetDisplay.setPreferredSize(new Dimension(150 * TILESET_SCALE, 200 * TILESET_SCALE));

        var tileSetWindow = new JFrame("tileset");
        tileSetWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        tileSetWindow.setLocation(0, 0);
        tileSetWindow.setContentPane(tileSetDisplay);
        tileSetWindow.setResizable(false);
        tileSetWindow.setVisible(true);
        tileSetWindow.pack();

        var mapDisplay = new MapDisplay(memory, gpuRegsManager, tiles, BGMAP_SCALE);
        mapDisplay.setPreferredSize(new Dimension(260 * BGMAP_SCALE, 260 * BGMAP_SCALE));

        var tileMapWindow = new JFrame("tilemap");
        tileMapWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        tileMapWindow.setLocationRelativeTo(tileSetWindow);
        tileMapWindow.setContentPane(mapDisplay);
        tileMapWindow.setResizable(false);
        tileMapWindow.setVisible(true);
        tileMapWindow.pack();

        var mainWindow = new JFrame("screen");
        mainWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainWindow.setLocationRelativeTo(tileMapWindow);
        mainWindow.setContentPane(display);
        mainWindow.setResizable(false);
        mainWindow.setVisible(true);
        mainWindow.pack();
        mainWindow.addKeyListener(inputManager);

        var ppu = new PPU(memory, gpuRegsManager, sprites, display, tiles);

        new Thread(display).start();
        new Thread(tileSetDisplay).start();
        new Thread(mapDisplay).start();

//        var filePath = "/home/musiek/github_repos/espressogb/src/main/resources/individual/01-special.gb"; //passed
//        var filePath = "/home/musiek/github_repos/espressogb/src/main/resources/individual/02-interrupts.gb"; //failed
//        var filePath = "/home/musiek/github_repos/espressogb/src/main/resources/individual/03-op sp,hl.gb"; //passed
//        var filePath = "/home/musiek/github_repos/espressogb/src/main/resources/individual/04-op r,imm.gb"; //passed
//        var filePath = "/home/musiek/github_repos/espressogb/src/main/resources/individual/05-op rp.gb"; //passed
//        var filePath = "/home/musiek/github_repos/espressogb/src/main/resources/individual/06-ld r,r.gb"; //passed
//        var filePath = "/home/musiek/github_repos/espressogb/src/main/resources/individual/07-jr,jp,call,ret,rst.gb"; //passed
//        var filePath = "/home/musiek/github_repos/espressogb/src/main/resources/individual/08-misc instrs.gb"; //passed
//        var filePath = "/home/musiek/github_repos/espressogb/src/main/resources/individual/09-op r,r.gb"; //failed
//        var filePath = "/home/musiek/github_repos/espressogb/src/main/resources/individual/10-bit ops.gb"; //passed
//        var filePath = "/home/musiek/github_repos/espressogb/src/main/resources/individual/11-op a,(hl).gb"; //passed
//        var filePath = "/home/musiek/github_repos/espressogb/src/main/resources/dmg-acid2.gb";
//        var filePath = "/home/musiek/github_repos/espressogb/src/main/resources/tetris.gb";
//        var filePath = "/home/musiek/github_repos/espressogb/src/main/resources/mario.gb";
//        var filePath = "/home/musiek/github_repos/espressogb/src/main/resources/dr_mario.gb";
//        var filePath = "/home/musiek/github_repos/espressogb/src/main/resources/ttt.gb";
        var filePath = "/home/musiek/github_repos/espressogb/src/main/resources/opus5.gb";

        cpu.loadFile(filePath, 0x0);

        cpu.loadFile("/home/musiek/github_repos/espressogb/src/main/resources/DMG_ROM.bin", 0x0);
//        cpu.loadFile("/home/musiek/github_repos/espressogb/src/main/resources/bootix_dmg.bin", 0x0);

//        for (int i = 0; i < 0x100; i++) {
//            System.out.println(String.format("byte %d - %02X", i, memory.get(i)));
//        }


        var interruptManager = new InterruptManager(cpu, registers, memory);

        int currentCycles = 0;
        var lastTime = System.nanoTime();
        int desiredCycles = cpu.freq;

        while (true) {

            if (registers.getPC() == 0x100)
                cpu.loadFile(filePath, 0x0);

            interruptManager.updateEnableInterruptsFlag();

            if (interruptManager.isInterruptsEnabled()) {
                interruptManager.handleInterrupts();
            }

            boolean prefixed = false;
            int opcode = memory.get(registers.incPC());

            if (opcode == 0xCB) {
                prefixed = true;
                opcode = memory.get(registers.incPC());
            }

            var instr = prefixed ? Instructions.getPrefixed(opcode) : Instructions.get(opcode);
            var context = instr.getContext();

//            System.out.println(
//                    String.format("PC %04X - %s", prefixed ? registers.getPC() - 1 : registers.getPC(), instr.getLabel())
//            );

            int accumulator = 0;
            for (var operation : instr.getOperations()) {
                accumulator = operation.execute(registers, memory, accumulator, context, interruptManager);
            }

            var cycles = instr.getCycles(context);
            cpu.addCycles(cycles);
            ppu.step(cycles);
            currentCycles += cycles;

            if (currentCycles > desiredCycles) {

                mapDisplay.updateMap();

                tileSetDisplay.updateMap();

                lastTime = System.nanoTime();
                currentCycles = 0;
            }
        }
    }

}
