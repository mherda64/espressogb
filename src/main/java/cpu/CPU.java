package cpu;

import cpu.instruction.Instruction;
import cpu.instruction.Instructions;
import memory.AddressSpace;
import memory.Memory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class CPU {

    private Registers registers;
    private AddressSpace memory;

    public CPU(Registers registers, Memory memory) {
        this.registers = registers;
        this.memory = memory;
    }

    public int loadFile(String path) throws IOException {
        byte[] program = Files.readAllBytes(new File(path).toPath());

        short pointer = 0x00;
        for (byte x : program) {
            memory.set(pointer, x & 0xFF);
            pointer++;
        }
        return program.length;
    }

    public static void main(String[] args) throws IOException {
        var registers = new Registers();
        var memory = new Memory(0xFFFF);
        var cpu = new CPU(registers, memory);

        cpu.loadFile("/home/musiek/github_repos/espressogb/src/main/resources/DMG_ROM.bin");

//        for (int i = 0; i < 0x100; i++) {
//            System.out.println(String.format("byte %d - %02X", i, memory.get(i)));
//        }

        var interruptManager = new InterruptManager();


        while (true) {
            boolean prefixed = false;
            int opcode = memory.get(registers.incPC());

            if (opcode == 0xCB) {
                prefixed = true;
                opcode = memory.get(registers.incPC());
            }

            if (registers.getPC() == 0x0064) {
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
        }
    }

}
