package cpu;

import cpu.instruction.Instructions;
import memory.AddressSpace;
import memory.Memory;
import ppu.*;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class CPU {

    private Registers registers;
    private AddressSpace memory;

    private int cycleCounter;

    private int freq = 1048576;

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
        var cpu = new CPU(registers, memory);

        var tiles = new Tiles(memory);
        memory.setTiles(tiles);

        var display = new Display();
        display.setPreferredSize(new Dimension(160, 144));

        var mainWindow = new JFrame("screen");
        mainWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainWindow.setLocationRelativeTo(null);
        mainWindow.setContentPane(display);
        mainWindow.setResizable(false);
        mainWindow.setVisible(true);
        mainWindow.pack();


        var tileSetDisplay = new TileDisplay(tiles);
        tileSetDisplay.setPreferredSize(new Dimension(200, 200));

        var tileSetWindow = new JFrame("tileset");
        tileSetWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        tileSetWindow.setLocationRelativeTo(mainWindow);
        tileSetWindow.setContentPane(tileSetDisplay);
        tileSetWindow.setResizable(false);
        tileSetWindow.setVisible(true);
        tileSetWindow.pack();

        var mapDisplay = new MapDisplay(memory, tiles);
        mapDisplay.setPreferredSize(new Dimension(300, 300));

        var tileMapWindow = new JFrame("tilemap");
        tileMapWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        tileMapWindow.setLocationRelativeTo(tileSetWindow);
        tileMapWindow.setContentPane(mapDisplay);
        tileMapWindow.setResizable(false);
        tileMapWindow.setVisible(true);
        tileMapWindow.pack();


        var ppu = new PPU(memory, display, tiles);

        new Thread(display).start();
        new Thread(tileSetDisplay).start();
        new Thread(mapDisplay).start();

        cpu.loadFile("/home/musiek/github_repos/espressogb/src/main/resources/tetris.gb", 0x0);
//        cpu.loadFile("/home/musiek/github_repos/espressogb/src/main/resources/gb240p.gb", 0x0);
//        cpu.loadFile("/home/musiek/github_repos/espressogb/src/main/resources/opus5.gb", 0x0);
//        cpu.loadFile("/home/musiek/github_repos/espressogb/src/main/resources/individual/01-special.gb", 0x0);
        cpu.loadFile("/home/musiek/github_repos/espressogb/src/main/resources/DMG_ROM.bin", 0x0);

//        for (int i = 0; i < 0x100; i++) {
//            System.out.println(String.format("byte %d - %02X", i, memory.get(i)));
//        }

        var interruptManager = new InterruptManager();

        int currentCycles = 0;
        var lastTime = System.nanoTime();
        int desiredCycles = cpu.freq;

//        registers.setPC(0x100);

        while (true) {
            boolean prefixed = false;
            int opcode = memory.get(registers.incPC());

            if (opcode == 0xCB) {
                prefixed = true;
                opcode = memory.get(registers.incPC());
            }

            if (registers.getPC() == 0x000C) {
//                System.out.println("break");
            }

            var instr = prefixed ? Instructions.getPrefixed(opcode) : Instructions.get(opcode);
            var context = instr.getContext();

            System.out.println(
                    String.format("PC %04X - %s", prefixed ? registers.getPC() - 1 : registers.getPC(), instr.getLabel())
            );

            int accumulator = 0;
            for (var operation : instr.getOperations()) {
                accumulator = operation.execute(registers, memory, accumulator, context, interruptManager);
            }

            var cycles = instr.getCycles(context);
            cpu.addCycles(cycles);
            ppu.step(cycles);
            currentCycles += cycles;
//            System.out.println(cpu.getCycleCounter());

            if (currentCycles > desiredCycles) {

                mapDisplay.updateMap();
                tileSetDisplay.updateTileMap();

                while (lastTime + 1_000_000_000 > System.nanoTime()) ;
                lastTime = System.nanoTime();
                currentCycles = 0;

            }
        }
    }

}
