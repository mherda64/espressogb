package cpu;

import memory.AddressSpace;
import memory.Memory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static cpu.InstructionsTest.executeInstruction;
import static org.junit.jupiter.api.Assertions.*;

class MiscInstructionsTest {

    Registers registers;
    AddressSpace addressSpace;

    @BeforeEach
    void init() {
        registers = new Registers();
        addressSpace = new Memory(0xFFFF);
    }

    @Test
    void testSwap_C_0xCB_0x31() {
        registers.setPC(0x2000);
        registers.setC(0x05);

        var instr = executeInstruction(0x31, true, registers, addressSpace);

        var flags = registers.getFlags();
        assertEquals(2, instr.getCycles(instr.getContext()));
        assertEquals(0x50, registers.getC());
        assertEquals(0x2000, registers.getPC());
        assertFalse(flags.isZFlag());
        assertFalse(flags.isNFlag());
        assertFalse(flags.isHFlag());
        assertFalse(flags.isCFlag());
    }

    @Test
    void testSwap_HLAddress_0xCB_0x36() {
        registers.setPC(0x2000);
        registers.setHL(0x1234);
        addressSpace.set(0x1234, 0xF1);

        var instr = executeInstruction(0x36, true, registers, addressSpace);

        var flags = registers.getFlags();
        assertEquals(4, instr.getCycles(instr.getContext()));
        assertEquals(0x1F, addressSpace.get(0x1234));
        assertEquals(0x2000, registers.getPC());
        assertFalse(flags.isZFlag());
        assertFalse(flags.isNFlag());
        assertFalse(flags.isHFlag());
        assertFalse(flags.isCFlag());
    }

    @Test
    void testSwap_HLAddress_zeroFlagSet_0xCB_0x36() {
        registers.setPC(0x2000);
        registers.setHL(0x1234);
        addressSpace.set(0x1234, 0x00);

        var instr = executeInstruction(0x36, true, registers, addressSpace);

        var flags = registers.getFlags();
        assertEquals(4, instr.getCycles(instr.getContext()));
        assertEquals(0x00, addressSpace.get(0x1234));
        assertEquals(0x2000, registers.getPC());
        assertTrue(flags.isZFlag());
        assertFalse(flags.isNFlag());
        assertFalse(flags.isHFlag());
        assertFalse(flags.isCFlag());
    }


    @Test
    void testDAA_afterAdd_0x27() {
        registers.setA(0x19);
        registers.setH(0x28);

        // Add H first to set correct flags
        var instr = executeInstruction(0x84, false, registers, addressSpace);
        instr = executeInstruction(0x27, false, registers, addressSpace);

        var flags = registers.getFlags();
        assertEquals(1, instr.getCycles(instr.getContext()));
        assertEquals(0x47, registers.getA());

    }

    @Test
    void testDAA_afterSub_0x27() {
        registers.setA(0x28);
        registers.setH(0x19);

        // Sub H first to set correct flags
        var instr = executeInstruction(0x94, false, registers, addressSpace);
        instr = executeInstruction(0x27, false, registers, addressSpace);

        var flags = registers.getFlags();
        assertEquals(1, instr.getCycles(instr.getContext()));
        assertEquals(0x09, registers.getA());

    }

}
