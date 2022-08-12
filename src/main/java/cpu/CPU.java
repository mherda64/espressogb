package cpu;

import cpu.instruction.Instructions;
import input.InputManager;
import memory.AddressSpace;
import memory.MMU;
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

    private int freq;

    private static final int TILESET_SCALE = 2;
    private static final int BGMAP_SCALE = 2;

    public CPU(Registers registers, MMU mmu, int freq) {
        this.registers = registers;
        this.memory = mmu;
        this.freq = freq;
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

    public static void main(String[] args) throws IOException, InterruptedException {
        var filePath = args[0];
        var biosPath = args[1];
        var freq = Integer.parseInt(args[2]);

        var DISPLAY_SCALE = 1;
        if (args.length >= 4 && args[3] != null)
            DISPLAY_SCALE = Integer.parseInt(args[3]);

        var inputManager = new InputManager();
        var tiles = new Tiles();
        var sprites = new SpriteManager();

        var registers = new Registers();
        var memory = new MMU(inputManager, sprites, tiles, filePath, biosPath);
        var gpuRegsManager = new GPURegsManager(memory);
        var cpu = new CPU(registers, memory, freq);

        tiles.setAddressSpace(memory);

        var display = new Display(DISPLAY_SCALE);
        display.setPreferredSize(new Dimension(160 * DISPLAY_SCALE, 144 * DISPLAY_SCALE));

        var tileSetDisplay = new TileDisplay(tiles, gpuRegsManager, TILESET_SCALE);
        tileSetDisplay.setPreferredSize(new Dimension(150 * TILESET_SCALE, 200 * TILESET_SCALE));
//
//        var tileSetWindow = new JFrame("tileset");
//        tileSetWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        tileSetWindow.setLocation(100, 100);
//        tileSetWindow.setContentPane(tileSetDisplay);
//        tileSetWindow.setResizable(false);
//        tileSetWindow.setVisible(true);
//        tileSetWindow.pack();

        var mapDisplay = new MapDisplay(memory, gpuRegsManager, tiles, BGMAP_SCALE);
        mapDisplay.setPreferredSize(new Dimension(260 * BGMAP_SCALE, 260 * BGMAP_SCALE));

//        var tileMapWindow = new JFrame("tilemap");
//        tileMapWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        tileMapWindow.setLocationRelativeTo(tileSetWindow);
//        tileMapWindow.setContentPane(mapDisplay);
//        tileMapWindow.setResizable(false);
//        tileMapWindow.setVisible(true);
//        tileMapWindow.pack();

        var mainWindow = new JFrame("screen");
        mainWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        mainWindow.setLocationRelativeTo(tileMapWindow);
        mainWindow.setContentPane(display);
        mainWindow.setResizable(false);
        mainWindow.setVisible(true);
        mainWindow.pack();
        mainWindow.addKeyListener(inputManager);

        var ppu = new PPU(memory, gpuRegsManager, sprites, display, tiles);

        new Thread(display).start();
        new Thread(tileSetDisplay).start();
        new Thread(mapDisplay).start();

        var interruptManager = new InterruptManager(cpu, registers, memory);

        int currentCycles = 0;
        int desiredCycles = cpu.freq;

        while (true) {

            if (registers.getPC() == 0x100)
                memory.setAfterBios();

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
                if (desiredCycles != 0)
                    Thread.sleep(0, 200);

//                mapDisplay.updateMap();
//                tileSetDisplay.updateMap();

                currentCycles = 0;
            }
        }
    }

}
