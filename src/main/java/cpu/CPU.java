package cpu;

import memory.AddressSpace;
import memory.Memory;

public class CPU {

    private Registers registers;
    private AddressSpace memory;

    public CPU(Registers registers) {
        this.registers = registers;
        this.memory = new Memory(0xFFFF);
    }

    private void step() {
        var opcode = memory.get(registers.getPC());
//        execute(opcode);
    }

//    private int execute(int opcode) {
//        var instruction = Instruction.ofByte(opcode);
//
//    }

}
