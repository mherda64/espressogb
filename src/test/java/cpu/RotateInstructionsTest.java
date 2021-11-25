package cpu;

import memory.AddressSpace;
import memory.Memory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static cpu.InstructionsTest.executeInstruction;
import static org.junit.jupiter.api.Assertions.*;

class RotateInstructionsTest {

    Registers registers;
    AddressSpace addressSpace;

    @BeforeEach
    void init() {
        registers = new Registers();
        addressSpace = new Memory(0xFFFF);
    }

    @Test
    void testRLCA_resultNotZero_0x07() {
        var flags = registers.getFlags();

        registers.setPC(0x2000);
        registers.setA(0x45);

        var instr = executeInstruction(0x07, false, registers, addressSpace);

        assertEquals(1, instr.getCycles());
        assertEquals(0x8A, registers.getA());
        assertEquals(0x2000, registers.getPC());
        assertFalse(flags.isZFlag());
        assertFalse(flags.isNFlag());
        assertFalse(flags.isHFlag());
        assertFalse(flags.isCFlag());
    }

    @Test
    void testRLCA_allOnes_0x07() {
        var flags = registers.getFlags();

        registers.setPC(0x2000);
        registers.setA(0xFF);

        var instr = executeInstruction(0x07, false, registers, addressSpace);

        assertEquals(1, instr.getCycles());
        assertEquals(0xFF, registers.getA());
        assertEquals(0x2000, registers.getPC());
        assertFalse(flags.isZFlag());
        assertFalse(flags.isNFlag());
        assertFalse(flags.isHFlag());
        assertTrue(flags.isCFlag());
    }

    @Test
    void testRLCA_allZeroes_0x07() {
        var flags = registers.getFlags();

        registers.setPC(0x2000);
        registers.setA(0x00);

        var instr = executeInstruction(0x07, false, registers, addressSpace);

        assertEquals(1, instr.getCycles());
        assertEquals(0x00, registers.getA());
        assertEquals(0x2000, registers.getPC());
        assertTrue(flags.isZFlag());
        assertFalse(flags.isNFlag());
        assertFalse(flags.isHFlag());
        assertFalse(flags.isCFlag());
    }

    @Test
    void testRLA_allOnes_0x17() {
        var flags = registers.getFlags();

        registers.setPC(0x2000);
        registers.setA(0xFF);
        flags.setCFlag(true);

        var instr = executeInstruction(0x17, false, registers, addressSpace);

        assertEquals(1, instr.getCycles());
        assertEquals(0xFF, registers.getA());
        assertEquals(0x2000, registers.getPC());
        assertFalse(flags.isZFlag());
        assertFalse(flags.isNFlag());
        assertFalse(flags.isHFlag());
        assertTrue(flags.isCFlag());
    }

    @Test
    void testRLA_randomNumber_0x17() {
        var flags = registers.getFlags();

        registers.setPC(0x2000);
        registers.setA(0x15);
        flags.setCFlag(true);

        var instr = executeInstruction(0x17, false, registers, addressSpace);

        assertEquals(1, instr.getCycles());
        assertEquals(0x2B, registers.getA());
        assertEquals(0x2000, registers.getPC());
        assertFalse(flags.isZFlag());
        assertFalse(flags.isNFlag());
        assertFalse(flags.isHFlag());
        assertFalse(flags.isCFlag());
    }

    @Test
    void testRLA_insertCarry_0x17() {
        var flags = registers.getFlags();

        registers.setPC(0x2000);
        registers.setA(0x88);
        flags.setCFlag(false);

        var instr = executeInstruction(0x17, false, registers, addressSpace);

        assertEquals(1, instr.getCycles());
        assertEquals(0x10, registers.getA());
        assertEquals(0x2000, registers.getPC());
        assertFalse(flags.isZFlag());
        assertFalse(flags.isNFlag());
        assertFalse(flags.isHFlag());
        assertTrue(flags.isCFlag());
    }

    @Test
    void testRRCA_randomNumber_0x0F() {
        var flags = registers.getFlags();

        registers.setPC(0x2000);
        registers.setA(0x89);
        flags.setCFlag(false);

        var instr = executeInstruction(0x0F, false, registers, addressSpace);

        assertEquals(1, instr.getCycles());
        assertEquals(0xC4, registers.getA());
        assertEquals(0x2000, registers.getPC());
        assertFalse(flags.isZFlag());
        assertFalse(flags.isNFlag());
        assertFalse(flags.isHFlag());
        assertTrue(flags.isCFlag());
    }

    @Test
    void testRRCA_allOnes_0x0F() {
        var flags = registers.getFlags();

        registers.setPC(0x2000);
        registers.setA(0xFF);
        flags.setCFlag(false);

        var instr = executeInstruction(0x0F, false, registers, addressSpace);

        assertEquals(1, instr.getCycles());
        assertEquals(0xFF, registers.getA());
        assertEquals(0x2000, registers.getPC());
        assertFalse(flags.isZFlag());
        assertFalse(flags.isNFlag());
        assertFalse(flags.isHFlag());
        assertTrue(flags.isCFlag());
    }

    @Test
    void testRRA_randomNumber_0x1F() {
        var flags = registers.getFlags();

        registers.setPC(0x2000);
        registers.setA(0x8A);
        flags.setCFlag(false);

        var instr = executeInstruction(0x1F, false, registers, addressSpace);

        assertEquals(1, instr.getCycles());
        assertEquals(0x45, registers.getA());
        assertEquals(0x2000, registers.getPC());
        assertFalse(flags.isZFlag());
        assertFalse(flags.isNFlag());
        assertFalse(flags.isHFlag());
        assertFalse(flags.isCFlag());
    }

    @Test
    void testRRA_isZero_0x1F() {
        var flags = registers.getFlags();

        registers.setPC(0x2000);
        registers.setA(0x01);
        flags.setCFlag(false);

        var instr = executeInstruction(0x1F, false, registers, addressSpace);

        assertEquals(1, instr.getCycles());
        assertEquals(0x00, registers.getA());
        assertEquals(0x2000, registers.getPC());
        assertTrue(flags.isZFlag());
        assertFalse(flags.isNFlag());
        assertFalse(flags.isHFlag());
        assertTrue(flags.isCFlag());
    }

    @Test
    void testRLC_B_randomNumber_0xCB_0x00() {
        var flags = registers.getFlags();

        registers.setPC(0x2000);
        registers.setB(0x55);
        flags.setCFlag(false);

        var instr = executeInstruction(0x00, true, registers, addressSpace);

        assertEquals(2, instr.getCycles());
        assertEquals(0xAA, registers.getB());
        assertEquals(0x2000, registers.getPC());
        assertFalse(flags.isZFlag());
        assertFalse(flags.isNFlag());
        assertFalse(flags.isHFlag());
        assertFalse(flags.isCFlag());
    }

    @Test
    void testRLC_HLAddress_randomNumber_0xCB_0x06() {
        var flags = registers.getFlags();

        registers.setPC(0x2000);
        registers.setHL(0x1234);
        addressSpace.set(0x1234, 0x85);
        flags.setCFlag(false);

        var instr = executeInstruction(0x06, true, registers, addressSpace);

        assertEquals(4, instr.getCycles());
        assertEquals(0x0B, addressSpace.get(0x1234));
        assertEquals(0x2000, registers.getPC());
        assertFalse(flags.isZFlag());
        assertFalse(flags.isNFlag());
        assertFalse(flags.isHFlag());
        assertTrue(flags.isCFlag());
    }

    @Test
    void testRL_B_randomNumber_0xCB_0x10() {
        var flags = registers.getFlags();

        registers.setPC(0x2000);
        registers.setB(0x55);
        flags.setCFlag(true);

        var instr = executeInstruction(0x10, true, registers, addressSpace);

        assertEquals(2, instr.getCycles());
        assertEquals(0xAB, registers.getB());
        assertEquals(0x2000, registers.getPC());
        assertFalse(flags.isZFlag());
        assertFalse(flags.isNFlag());
        assertFalse(flags.isHFlag());
        assertFalse(flags.isCFlag());
    }

    @Test
    void testRL_HLAddress_randomNumber_0xCB_0x16() {
        var flags = registers.getFlags();

        registers.setPC(0x2000);
        registers.setHL(0x1234);
        addressSpace.set(0x1234, 0x85);
        flags.setCFlag(false);

        var instr = executeInstruction(0x16, true, registers, addressSpace);

        assertEquals(4, instr.getCycles());
        assertEquals(0x0A, addressSpace.get(0x1234));
        assertEquals(0x2000, registers.getPC());
        assertFalse(flags.isZFlag());
        assertFalse(flags.isNFlag());
        assertFalse(flags.isHFlag());
        assertTrue(flags.isCFlag());
    }

    @Test
    void testRRC_C_randomNumber_0xCB_0x09() {
        var flags = registers.getFlags();

        registers.setPC(0x2000);
        registers.setC(0xAF);
        flags.setCFlag(false);

        var instr = executeInstruction(0x09, true, registers, addressSpace);

        assertEquals(2, instr.getCycles());
        assertEquals(0xD7, registers.getC());
        assertEquals(0x2000, registers.getPC());
        assertFalse(flags.isZFlag());
        assertFalse(flags.isNFlag());
        assertFalse(flags.isHFlag());
        assertTrue(flags.isCFlag());
    }

    @Test
    void testRRC_HLAddress_randomNumber_0xCB_0x0E() {
        var flags = registers.getFlags();

        registers.setPC(0x2000);
        registers.setHL(0x1234);
        addressSpace.set(0x1234, 0x85);
        flags.setCFlag(false);

        var instr = executeInstruction(0x0E, true, registers, addressSpace);

        assertEquals(4, instr.getCycles());
        assertEquals(0xC2, addressSpace.get(0x1234));
        assertEquals(0x2000, registers.getPC());
        assertFalse(flags.isZFlag());
        assertFalse(flags.isNFlag());
        assertFalse(flags.isHFlag());
        assertTrue(flags.isCFlag());
    }

    @Test
    void testRR_D_randomNumber_0xCB_0x1A() {
        var flags = registers.getFlags();

        registers.setPC(0x2000);
        registers.setD(0xF9);
        flags.setCFlag(false);

        var instr = executeInstruction(0x1A, true, registers, addressSpace);

        assertEquals(2, instr.getCycles());
        assertEquals(0x7C, registers.getD());
        assertEquals(0x2000, registers.getPC());
        assertFalse(flags.isZFlag());
        assertFalse(flags.isNFlag());
        assertFalse(flags.isHFlag());
        assertTrue(flags.isCFlag());
    }

    @Test
    void testRR_HLAddress_randomNumber_0xCB_0x1E() {
        var flags = registers.getFlags();

        registers.setPC(0x2000);
        registers.setHL(0x1234);
        addressSpace.set(0x1234, 0x88);
        flags.setCFlag(true);

        var instr = executeInstruction(0x1E, true, registers, addressSpace);

        assertEquals(4, instr.getCycles());
        assertEquals(0xC4, addressSpace.get(0x1234));
        assertEquals(0x2000, registers.getPC());
        assertFalse(flags.isZFlag());
        assertFalse(flags.isNFlag());
        assertFalse(flags.isHFlag());
        assertFalse(flags.isCFlag());
    }

    @Test
    void testSLA_A_randomNumber_0xCB_0x27() {
        var flags = registers.getFlags();

        registers.setPC(0x2000);
        registers.setA(0xAF);
        flags.setCFlag(false);

        var instr = executeInstruction(0x27, true, registers, addressSpace);

        assertEquals(2, instr.getCycles());
        assertEquals(0x5E, registers.getA());
        assertEquals(0x2000, registers.getPC());
        assertFalse(flags.isZFlag());
        assertFalse(flags.isNFlag());
        assertFalse(flags.isHFlag());
        assertTrue(flags.isCFlag());
    }

    @Test
    void testSLA_HLAddress_randomNumber_0xCB_0x26() {
        var flags = registers.getFlags();

        registers.setPC(0x2000);
        registers.setHL(0x1234);
        addressSpace.set(0x1234, 0xA1);
        flags.setCFlag(false);

        var instr = executeInstruction(0x26, true, registers, addressSpace);

        assertEquals(4, instr.getCycles());
        assertEquals(0x42, addressSpace.get(0x1234));
        assertEquals(0x2000, registers.getPC());
        assertFalse(flags.isZFlag());
        assertFalse(flags.isNFlag());
        assertFalse(flags.isHFlag());
        assertTrue(flags.isCFlag());
    }

    @Test
    void testSRA_A_randomNumber_0xCB_0x2F() {
        var flags = registers.getFlags();

        registers.setPC(0x2000);
        registers.setA(0xAF);
        flags.setCFlag(false);

        var instr = executeInstruction(0x2F, true, registers, addressSpace);

        assertEquals(2, instr.getCycles());
        assertEquals(0xD7, registers.getA());
        assertEquals(0x2000, registers.getPC());
        assertFalse(flags.isZFlag());
        assertFalse(flags.isNFlag());
        assertFalse(flags.isHFlag());
        assertTrue(flags.isCFlag());
    }

    @Test
    void testSRA_HLAddress_randomNumber_0xCB_0x2E() {
        var flags = registers.getFlags();

        registers.setPC(0x2000);
        registers.setHL(0x1234);
        addressSpace.set(0x1234, 0xA0);
        flags.setCFlag(false);

        var instr = executeInstruction(0x2E, true, registers, addressSpace);

        assertEquals(4, instr.getCycles());
        assertEquals(0xD0, addressSpace.get(0x1234));
        assertEquals(0x2000, registers.getPC());
        assertFalse(flags.isZFlag());
        assertFalse(flags.isNFlag());
        assertFalse(flags.isHFlag());
        assertFalse(flags.isCFlag());
    }

    @Test
    void testSRL_E_randomNumber_0xCB_0x3B() {
        var flags = registers.getFlags();

        registers.setPC(0x2000);
        registers.setE(0xAF);
        flags.setCFlag(false);

        var instr = executeInstruction(0x3B, true, registers, addressSpace);

        assertEquals(2, instr.getCycles());
        assertEquals(0x57, registers.getE());
        assertEquals(0x2000, registers.getPC());
        assertFalse(flags.isZFlag());
        assertFalse(flags.isNFlag());
        assertFalse(flags.isHFlag());
        assertTrue(flags.isCFlag());
    }

    @Test
    void testSRL_HLAddress_randomNumber_0xCB_0x3E() {
        var flags = registers.getFlags();

        registers.setPC(0x2000);
        registers.setHL(0x1234);
        addressSpace.set(0x1234, 0xA0);
        flags.setCFlag(false);

        var instr = executeInstruction(0x3E, true, registers, addressSpace);

        assertEquals(4, instr.getCycles());
        assertEquals(0x50, addressSpace.get(0x1234));
        assertEquals(0x2000, registers.getPC());
        assertFalse(flags.isZFlag());
        assertFalse(flags.isNFlag());
        assertFalse(flags.isHFlag());
        assertFalse(flags.isCFlag());
    }

}
