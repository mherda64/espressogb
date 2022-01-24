package cpu;

import cpu.instruction.Instructions;
import memory.AddressSpace;
import memory.Memory;

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
        var memory = new Memory(0xFFFF);
        var cpu = new CPU(registers, memory);

        cpu.loadFile("/home/musiek/github_repos/espressogb/src/main/resources/tetris.gb", 0x0);
        cpu.loadFile("/home/musiek/github_repos/espressogb/src/main/resources/DMG_ROM.bin", 0x0);

//        for (int i = 0; i < 0x100; i++) {
//            System.out.println(String.format("byte %d - %02X", i, memory.get(i)));
//        }

        var interruptManager = new InterruptManager();

        int currentCycles = 0;
        var lastTime = System.nanoTime();
        int desiredCycles = cpu.freq;

        while (true) {
            boolean prefixed = false;
            int opcode = memory.get(registers.incPC());

            if (opcode == 0xCB) {
                prefixed = true;
                opcode = memory.get(registers.incPC());
            }

            if (registers.getPC() == 0x0021) {
                System.out.println("break");
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
            currentCycles += cycles;
            System.out.println(cpu.getCycleCounter());

            if (currentCycles > desiredCycles) {
                while (lastTime + 1_000_000_000 > System.nanoTime()) ;
                lastTime = System.nanoTime();
                currentCycles = 0;
            }
        }
    }

}
