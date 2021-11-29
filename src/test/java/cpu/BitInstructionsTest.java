package cpu;

import memory.AddressSpace;
import memory.Memory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static cpu.InstructionsTest.executeInstruction;
import static org.junit.jupiter.api.Assertions.*;

public class BitInstructionsTest {
    Registers registers;
    AddressSpace addressSpace;

    @BeforeEach
    void init() {
        registers = new Registers();
        addressSpace = new Memory(0xFFFF);
    }

    @Test
    void testBIT_A_2_0xCB_0x57() {
        var flags = registers.getFlags();

        registers.setPC(0x2000);
        registers.setA(0x45);

        var instr = executeInstruction(0x57, true, registers, addressSpace);

        assertEquals(2, instr.getCycles(instr.getContext()));
        assertEquals(0x45, registers.getA());
        assertEquals(0x2000, registers.getPC());
        assertFalse(flags.isZFlag());
        assertFalse(flags.isNFlag());
        assertTrue(flags.isHFlag());
    }

    @Test
    void testBIT_HLAddress_3_0xCB_0x5E() {
        var flags = registers.getFlags();

        registers.setPC(0x2000);
        registers.setHL(0x1234);
        addressSpace.set(0x1234, 0xF1);

        var instr = executeInstruction(0x5E, true, registers, addressSpace);

        assertEquals(4, instr.getCycles(instr.getContext()));
        assertEquals(0x2000, registers.getPC());
        assertTrue(flags.isZFlag());
        assertFalse(flags.isNFlag());
        assertTrue(flags.isHFlag());
    }


    @Test
    void testSET_E_2_0xCB_0xD3() {
        registers.setPC(0x2000);
        registers.setE(0x00);

        var instr = executeInstruction(0xD3, true, registers, addressSpace);

        assertEquals(2, instr.getCycles(instr.getContext()));
        assertEquals(0x04, registers.getE());
        assertEquals(0x2000, registers.getPC());
    }

    @Test
    void testSET_HLAddress_3_0xCB_0xDE() {
        registers.setPC(0x2000);
        registers.setHL(0x1234);
        addressSpace.set(0x1234, 0xF1);

        var instr = executeInstruction(0xDE, true, registers, addressSpace);

        assertEquals(4, instr.getCycles(instr.getContext()));
        assertEquals(0x2000, registers.getPC());
        assertEquals(0xF9, addressSpace.get(0x1234));
    }

    @Test
    void testRES_D_0xCB_0xBA() {
        registers.setPC(0x2000);
        registers.setD(0xFF);

        var instr = executeInstruction(0xBA, true, registers, addressSpace);

        assertEquals(2, instr.getCycles(instr.getContext()));
        assertEquals(0x7F, registers.getD());
        assertEquals(0x2000, registers.getPC());
    }

    @Test
    void testRES_HLAddress_0_0xCB_0x86() {
        registers.setPC(0x2000);
        registers.setHL(0x1234);
        addressSpace.set(0x1234, 0xF1);

        var instr = executeInstruction(0x86, true, registers, addressSpace);

        assertEquals(4, instr.getCycles(instr.getContext()));
        assertEquals(0x2000, registers.getPC());
        assertEquals(0xF0, addressSpace.get(0x1234));
    }
}
