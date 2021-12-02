package cpu;

import memory.AddressSpace;
import memory.Memory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static cpu.InstructionsTest.executeInstruction;
import static org.junit.jupiter.api.Assertions.*;

public class WordAluInstructionsTest {

    Registers registers;
    AddressSpace addressSpace;

    @BeforeEach
    void init() {
        registers = new Registers();
        addressSpace = new Memory(0xFFFF);
    }

    @Test
    void testAddHL_HL_noFlagSet_0x29() {
        var flags = registers.getFlags();
        registers.setPC(0x2000);
        registers.setHL(0x1004);
        flags.setZFlag(true);

        var instr = executeInstruction(0x29, false, registers, addressSpace);

        assertEquals(2, instr.getCycles(instr.getContext()));
        assertEquals(0x2008, registers.getHL());
        assertEquals(0x2000, registers.getPC());
        // Zero flag not affected
        assertTrue(flags.isZFlag());
        assertFalse(flags.isNFlag());
        assertFalse(flags.isHFlag());
        assertFalse(flags.isCFlag());
    }

    @Test
    void testAddHL_HL_bothCarryFlagSet_0x29() {
        var flags = registers.getFlags();
        registers.setPC(0x2000);
        registers.setHL(0xFFFE);
        flags.setZFlag(true);

        var instr = executeInstruction(0x29, false, registers, addressSpace);

        assertEquals(2, instr.getCycles(instr.getContext()));
        assertEquals(0xFFFC, registers.getHL());
        assertEquals(0x2000, registers.getPC());
        // Zero flag not affected
        assertTrue(flags.isZFlag());
        assertFalse(flags.isNFlag());
        assertTrue(flags.isHFlag());
        assertTrue(flags.isCFlag());
    }

    @Test
    void testAddHL_SP_halfCarryFlagSet_0x39() {
        var flags = registers.getFlags();
        registers.setPC(0x2000);
        registers.setHL(0x1FFE);
        registers.setSP(0x0010);
        flags.setZFlag(true);

        var instr = executeInstruction(0x39, false, registers, addressSpace);

        assertEquals(2, instr.getCycles(instr.getContext()));
        assertEquals(0x200E, registers.getHL());
        assertEquals(0x2000, registers.getPC());
        // Zero flag not affected
        assertTrue(flags.isZFlag());
        assertFalse(flags.isNFlag());
        assertTrue(flags.isHFlag());
        assertFalse(flags.isCFlag());
    }

    @Test
    void testAddHL_SP_noFlagSet_0x39() {
        var flags = registers.getFlags();
        registers.setPC(0x2000);
        registers.setHL(0x10FE);
        registers.setSP(0x0010);
        flags.setZFlag(true);

        var instr = executeInstruction(0x39, false, registers, addressSpace);

        assertEquals(2, instr.getCycles(instr.getContext()));
        assertEquals(0x110E, registers.getHL());
        assertEquals(0x2000, registers.getPC());
        // Zero flag not affected
        assertTrue(flags.isZFlag());
        assertFalse(flags.isNFlag());
        assertFalse(flags.isHFlag());
        assertFalse(flags.isCFlag());
    }

    @Test
    void testAddSP_immediateSigned_noFlagSet_0xE8() {
        var flags = registers.getFlags();
        registers.setPC(0x2000);
        registers.setSP(0x0100);
        addressSpace.set(0x2001, -25 & 0xFF);
        flags.setZFlag(true);

        var instr = executeInstruction(0xE8, false, registers, addressSpace);

        assertEquals(4, instr.getCycles(instr.getContext()));
        assertEquals(0x00E7, registers.getSP());
        assertEquals(0x2001, registers.getPC());
        // Zero flag not affected
        assertTrue(flags.isZFlag());
        assertFalse(flags.isNFlag());
        // Not sure how flags should behave with negative numbers and borrowing
        assertFalse(flags.isHFlag());
        assertFalse(flags.isCFlag());
    }

    @Test
    void testAddSP_immediateSigned_halfCarrySet_0xE8() {
        var flags = registers.getFlags();
        registers.setPC(0x2000);
        registers.setSP(0x011F);
        addressSpace.set(0x2001, 0x01);
        flags.setZFlag(true);

        var instr = executeInstruction(0xE8, false, registers, addressSpace);

        assertEquals(4, instr.getCycles(instr.getContext()));
        assertEquals(0x0120, registers.getSP());
        assertEquals(0x2001, registers.getPC());
        // Zero flag not affected
        assertTrue(flags.isZFlag());
        assertFalse(flags.isNFlag());
        assertTrue(flags.isHFlag());
        assertFalse(flags.isCFlag());
    }

    @Test
    void testIncBC_0x03() {
        var flags = registers.getFlags();
        registers.setPC(0x2000);
        registers.setBC(0x2000);

        var instr = executeInstruction(0x03, false, registers, addressSpace);

        assertEquals(2, instr.getCycles(instr.getContext()));
        assertEquals(0x2001, registers.getBC());
        assertEquals(0x2000, registers.getPC());
    }

    @Test
    void testIncBC_overflow_0x03() {
        var flags = registers.getFlags();
        registers.setPC(0x2000);
        registers.setBC(0xFFFF);

        var instr = executeInstruction(0x03, false, registers, addressSpace);

        assertEquals(2, instr.getCycles(instr.getContext()));
        assertEquals(0x0000, registers.getBC());
        assertEquals(0x2000, registers.getPC());
    }



    @Test
    void testDecSP_0x3B() {
        var flags = registers.getFlags();
        registers.setPC(0x2000);
        registers.setSP(0x2000);

        var instr = executeInstruction(0x3B, false, registers, addressSpace);

        assertEquals(2, instr.getCycles(instr.getContext()));
        assertEquals(0x1FFF, registers.getSP());
        assertEquals(0x2000, registers.getPC());
    }

    @Test
    void testDecBC_underflow_0x0B() {
        var flags = registers.getFlags();
        registers.setPC(0x2000);
        registers.setBC(0x0000);

        var instr = executeInstruction(0x0B, false, registers, addressSpace);

        assertEquals(2, instr.getCycles(instr.getContext()));
        assertEquals(0xFFFF, registers.getBC());
        assertEquals(0x2000, registers.getPC());
    }

}
