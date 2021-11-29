package cpu;

import cpu.instruction.Instructions;
import memory.AddressSpace;
import memory.Memory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static cpu.InstructionsTest.executeInstruction;
import static org.junit.jupiter.api.Assertions.*;

class ByteAluInstructionsTest {

    Registers registers;
    AddressSpace addressSpace;

    @BeforeEach
    void init() {
        registers = new Registers();
        addressSpace = new Memory(0xFFFF);
    }

    @Test
    void testAddA_C_noFlagSet_0x81() {
        registers.setPC(0x2000);
        registers.setA(0x15);
        registers.setC(0x05);

        var instr = executeInstruction(0x81, false, registers, addressSpace);

        var flags = registers.getFlags();
        assertEquals(1, instr.getCycles(instr.getContext()));
        assertEquals(0x1A, registers.getA());
        assertEquals(0x2000, registers.getPC());
        assertFalse(flags.isZFlag());
        assertFalse(flags.isNFlag());
        assertFalse(flags.isHFlag());
        assertFalse(flags.isCFlag());
    }

    @Test
    void testAddA_H_halfCarrySet_0x84() {
        registers.setPC(0x2000);
        registers.setA(0x1E);
        registers.setH(0x02);

        var instr = executeInstruction(0x84, false, registers, addressSpace);

        var flags = registers.getFlags();
        assertEquals(1, instr.getCycles(instr.getContext()));
        assertEquals(0x20, registers.getA());
        assertEquals(0x2000, registers.getPC());
        assertFalse(flags.isZFlag());
        assertFalse(flags.isNFlag());
        assertTrue(flags.isHFlag());
        assertFalse(flags.isCFlag());
    }

    @Test
    void testAddA_L_carryZeroSet_0x85() {
        registers.setPC(0x2000);
        registers.setA(0xFE);
        registers.setL(0x02);

        var instr = executeInstruction(0x85, false, registers, addressSpace);

        var flags = registers.getFlags();
        assertEquals(1, instr.getCycles(instr.getContext()));
        assertEquals(0x00, registers.getA());
        assertEquals(0x2000, registers.getPC());
        assertTrue(flags.isZFlag());
        assertFalse(flags.isNFlag());
        assertTrue(flags.isHFlag());
        assertTrue(flags.isCFlag());
    }

    @Test
    void testAddA_HLAddress_noFlagSet_0x86() {
        registers.setPC(0x2000);
        registers.setA(0x01);
        registers.setHL(0x10FF);
        addressSpace.set(0x10FF, 0x05);

        var instr = executeInstruction(0x86, false, registers, addressSpace);

        var flags = registers.getFlags();
        assertEquals(2, instr.getCycles(instr.getContext()));
        assertEquals(0x06, registers.getA());
        assertEquals(0x2000, registers.getPC());
        assertFalse(flags.isZFlag());
        assertFalse(flags.isNFlag());
        assertFalse(flags.isHFlag());
        assertFalse(flags.isCFlag());
    }

    @Test
    void testAddA_HLAddress_carrySet_0x86() {
        registers.setPC(0x2000);
        registers.setA(0xF1);
        registers.setHL(0x10FF);
        addressSpace.set(0x10FF, 0x0F);

        var instr = executeInstruction(0x86, false, registers, addressSpace);

        var flags = registers.getFlags();
        assertEquals(2, instr.getCycles(instr.getContext()));
        assertEquals(0x00, registers.getA());
        assertEquals(0x2000, registers.getPC());
        assertTrue(flags.isZFlag());
        assertFalse(flags.isNFlag());
        assertTrue(flags.isHFlag());
        assertTrue(flags.isCFlag());
    }

    @Test
    void testAddA_immediateByte_noFlagSet_0xC6() {
        registers.setPC(0x2000);
        registers.setA(0xAA);
        addressSpace.set(0x2001, 0x12);

        var instr = executeInstruction(0xC6, false, registers, addressSpace);

        var flags = registers.getFlags();
        assertEquals(2, instr.getCycles(instr.getContext()));
        assertEquals(0xBC, registers.getA());
        assertEquals(0x2001, registers.getPC());
        assertFalse(flags.isZFlag());
        assertFalse(flags.isNFlag());
        assertFalse(flags.isHFlag());
        assertFalse(flags.isCFlag());
    }

    @Test
    void testAddA_immediateByte_carrySet_0xC6() {
        registers.setPC(0x2000);
        registers.setA(0x1A);
        addressSpace.set(0x2001, 0xF0);

        var instr = executeInstruction(0xC6, false, registers, addressSpace);

        var flags = registers.getFlags();
        assertEquals(2, instr.getCycles(instr.getContext()));
        assertEquals(0x0A, registers.getA());
        assertEquals(0x2001, registers.getPC());
        assertFalse(flags.isZFlag());
        assertFalse(flags.isNFlag());
        assertFalse(flags.isHFlag());
        assertTrue(flags.isCFlag());
    }

    @Test
    void testAdcA_C_noFlagSet_0x89() {
        registers.setPC(0x2000);
        registers.setA(0x15);
        registers.setC(0x05);
        registers.getFlags().setCFlag(true);

        var instr = Instructions.get(0x89);

        int accumulator = 0;
        for (var operation : instr.getOperations()) {
            accumulator = operation.execute(registers, addressSpace, accumulator, null);
        }

        var flags = registers.getFlags();
        assertEquals(1, instr.getCycles(instr.getContext()));
        assertEquals(0x1B, registers.getA());
        assertEquals(0x2000, registers.getPC());
        assertFalse(flags.isZFlag());
        assertFalse(flags.isNFlag());
        assertFalse(flags.isHFlag());
        assertFalse(flags.isCFlag());
    }

    @Test
    void testAdcA_H_halfCarrySet_0x8C() {
        registers.setPC(0x2000);
        registers.setA(0x1E);
        registers.setH(0x02);
        registers.getFlags().setCFlag(true);

        var instr = Instructions.get(0x8C);

        int accumulator = 0;
        for (var operation : instr.getOperations()) {
            accumulator = operation.execute(registers, addressSpace, accumulator, null);
        }

        var flags = registers.getFlags();
        assertEquals(1, instr.getCycles(instr.getContext()));
        assertEquals(0x21, registers.getA());
        assertEquals(0x2000, registers.getPC());
        assertFalse(flags.isZFlag());
        assertFalse(flags.isNFlag());
        assertTrue(flags.isHFlag());
        assertFalse(flags.isCFlag());
    }

    @Test
    void testAdcA_L_carryZeroSet_0x8D() {
        registers.setPC(0x2000);
        registers.setA(0xFE);
        registers.setL(0x02);
        registers.getFlags().setCFlag(true);

        var instr = Instructions.get(0x8D);

        int accumulator = 0;
        for (var operation : instr.getOperations()) {
            accumulator = operation.execute(registers, addressSpace, accumulator, null);
        }

        var flags = registers.getFlags();
        assertEquals(1, instr.getCycles(instr.getContext()));
        assertEquals(0x01, registers.getA());
        assertEquals(0x2000, registers.getPC());
        assertFalse(flags.isZFlag());
        assertFalse(flags.isNFlag());
        assertTrue(flags.isHFlag());
        assertTrue(flags.isCFlag());
    }

    @Test
    void testAdcA_HLAddress_noFlagSet_0x8E() {
        registers.setPC(0x2000);
        registers.setA(0x01);
        registers.setHL(0x10FF);
        addressSpace.set(0x10FF, 0x05);
        registers.getFlags().setCFlag(true);

        var instr = Instructions.get(0x8E);

        int accumulator = 0;
        for (var operation : instr.getOperations()) {
            accumulator = operation.execute(registers, addressSpace, accumulator, null);
        }

        var flags = registers.getFlags();
        assertEquals(2, instr.getCycles(instr.getContext()));
        assertEquals(0x07, registers.getA());
        assertEquals(0x2000, registers.getPC());
        assertFalse(flags.isZFlag());
        assertFalse(flags.isNFlag());
        assertFalse(flags.isHFlag());
        assertFalse(flags.isCFlag());
    }

    @Test
    void testAdcA_HLAddress_carrySet_0x8E() {
        registers.setPC(0x2000);
        registers.setA(0xF1);
        registers.setHL(0x10FF);
        addressSpace.set(0x10FF, 0x0F);
        registers.getFlags().setCFlag(true);

        var instr = Instructions.get(0x8E);

        int accumulator = 0;
        for (var operation : instr.getOperations()) {
            accumulator = operation.execute(registers, addressSpace, accumulator, null);
        }

        var flags = registers.getFlags();
        assertEquals(2, instr.getCycles(instr.getContext()));
        assertEquals(0x01, registers.getA());
        assertEquals(0x2000, registers.getPC());
        assertFalse(flags.isZFlag());
        assertFalse(flags.isNFlag());
        assertTrue(flags.isHFlag());
        assertTrue(flags.isCFlag());
    }


    @Test
    void testAdcA_immediateByte_noFlagSet_0xCE() {
        registers.setPC(0x2000);
        registers.setA(0x01);
        addressSpace.set(0x2001, 0x05);
        registers.getFlags().setCFlag(true);

        var instr = Instructions.get(0xCE);

        int accumulator = 0;
        for (var operation : instr.getOperations()) {
            accumulator = operation.execute(registers, addressSpace, accumulator, null);
        }

        var flags = registers.getFlags();
        assertEquals(2, instr.getCycles(instr.getContext()));
        assertEquals(0x07, registers.getA());
        assertEquals(0x2001, registers.getPC());
        assertFalse(flags.isZFlag());
        assertFalse(flags.isNFlag());
        assertFalse(flags.isHFlag());
        assertFalse(flags.isCFlag());
    }

    @Test
    void testAdcA_immediateByte_carrySet_0xCE() {
        registers.setPC(0x2000);
        registers.setA(0xF1);
        addressSpace.set(0x2001, 0x0F);
        registers.getFlags().setCFlag(true);

        var instr = Instructions.get(0xCE);

        int accumulator = 0;
        for (var operation : instr.getOperations()) {
            accumulator = operation.execute(registers, addressSpace, accumulator, null);
        }

        var flags = registers.getFlags();
        assertEquals(2, instr.getCycles(instr.getContext()));
        assertEquals(0x01, registers.getA());
        assertEquals(0x2001, registers.getPC());
        assertFalse(flags.isZFlag());
        assertFalse(flags.isNFlag());
        assertTrue(flags.isHFlag());
        assertTrue(flags.isCFlag());
    }

    @Test
    void testSubA_B_noFlagSet_0x90() {
        registers.setPC(0x2000);
        registers.setA(0xF1);
        registers.setB(0x10);

        var instr = Instructions.get(0x90);

        int accumulator = 0;
        for (var operation : instr.getOperations()) {
            accumulator = operation.execute(registers, addressSpace, accumulator, null);
        }

        var flags = registers.getFlags();
        assertEquals(1, instr.getCycles(instr.getContext()));
        assertEquals(0xE1, registers.getA());
        assertEquals(0x2000, registers.getPC());
        assertFalse(flags.isZFlag());
        assertTrue(flags.isNFlag());
        assertFalse(flags.isHFlag());
        assertFalse(flags.isCFlag());
    }

    @Test
    void testSubA_B_halfCarrySet_0x90() {
        registers.setPC(0x2000);
        registers.setA(0xF1);
        registers.setB(0x02);

        var instr = Instructions.get(0x90);

        int accumulator = 0;
        for (var operation : instr.getOperations()) {
            accumulator = operation.execute(registers, addressSpace, accumulator, null);
        }

        var flags = registers.getFlags();
        assertEquals(1, instr.getCycles(instr.getContext()));
        assertEquals(0xEF, registers.getA());
        assertEquals(0x2000, registers.getPC());
        assertFalse(flags.isZFlag());
        assertTrue(flags.isNFlag());
        assertTrue(flags.isHFlag());
        assertFalse(flags.isCFlag());
    }

    @Test
    void testSubA_B_carrySet_0x90() {
        registers.setPC(0x2000);
        registers.setA(0xF1);
        registers.setB(0xF2);

        var instr = Instructions.get(0x90);

        int accumulator = 0;
        for (var operation : instr.getOperations()) {
            accumulator = operation.execute(registers, addressSpace, accumulator, null);
        }

        var flags = registers.getFlags();
        assertEquals(1, instr.getCycles(instr.getContext()));
        assertEquals(0xFF, registers.getA());
        assertEquals(0x2000, registers.getPC());
        assertFalse(flags.isZFlag());
        assertTrue(flags.isNFlag());
        assertTrue(flags.isHFlag());
        assertTrue(flags.isCFlag());
    }

    @Test
    void testSubA_B_zeroSet_0x90() {
        registers.setPC(0x2000);
        registers.setA(0xF1);
        registers.setB(0xF1);

        var instr = Instructions.get(0x90);

        int accumulator = 0;
        for (var operation : instr.getOperations()) {
            accumulator = operation.execute(registers, addressSpace, accumulator, null);
        }

        var flags = registers.getFlags();
        assertEquals(1, instr.getCycles(instr.getContext()));
        assertEquals(0x00, registers.getA());
        assertEquals(0x2000, registers.getPC());
        assertTrue(flags.isZFlag());
        assertTrue(flags.isNFlag());
        assertFalse(flags.isHFlag());
        assertFalse(flags.isCFlag());
    }

    @Test
    void testSubA_immediateAddress_noFlagsSet_0x96() {
        registers.setPC(0x2000);
        registers.setA(0xF1);
        registers.setHL(0x12FF);
        addressSpace.set(0x12FF, 0x01);

        var instr = Instructions.get(0x96);

        int accumulator = 0;
        for (var operation : instr.getOperations()) {
            accumulator = operation.execute(registers, addressSpace, accumulator, null);
        }

        var flags = registers.getFlags();
        assertEquals(2, instr.getCycles(instr.getContext()));
        assertEquals(0xF0, registers.getA());
        assertEquals(0x2000, registers.getPC());
        assertFalse(flags.isZFlag());
        assertTrue(flags.isNFlag());
        assertFalse(flags.isHFlag());
        assertFalse(flags.isCFlag());
    }

    @Test
    void testSubA_immediateByte_halfCarrySet_0xD6() {
        registers.setPC(0x2000);
        registers.setA(0xF1);
        addressSpace.set(0x2001, 0x1F);

        var instr = Instructions.get(0xD6);

        int accumulator = 0;
        for (var operation : instr.getOperations()) {
            accumulator = operation.execute(registers, addressSpace, accumulator, null);
        }

        var flags = registers.getFlags();
        assertEquals(2, instr.getCycles(instr.getContext()));
        assertEquals(0xD2, registers.getA());
        assertEquals(0x2001, registers.getPC());
        assertFalse(flags.isZFlag());
        assertTrue(flags.isNFlag());
        assertTrue(flags.isHFlag());
        assertFalse(flags.isCFlag());
    }

    @Test
    void testSbcA_B_noCarryFlag_0x98() {
        registers.setPC(0x2000);
        registers.setA(0xF1);
        registers.setB(0x10);
        registers.getFlags().setCFlag(false);

        var instr = Instructions.get(0x98);

        int accumulator = 0;
        for (var operation : instr.getOperations()) {
            accumulator = operation.execute(registers, addressSpace, accumulator, null);
        }

        var flags = registers.getFlags();
        assertEquals(1, instr.getCycles(instr.getContext()));
        assertEquals(0xE1, registers.getA());
        assertEquals(0x2000, registers.getPC());
        assertFalse(flags.isZFlag());
        assertTrue(flags.isNFlag());
        assertFalse(flags.isHFlag());
        assertFalse(flags.isCFlag());
    }

    @Test
    void testSbcA_B_carryFlag_0x98() {
        registers.setPC(0x2000);
        registers.setA(0xF1);
        registers.setB(0x10);
        registers.getFlags().setCFlag(true);

        var instr = Instructions.get(0x98);

        int accumulator = 0;
        for (var operation : instr.getOperations()) {
            accumulator = operation.execute(registers, addressSpace, accumulator, null);
        }

        var flags = registers.getFlags();
        assertEquals(1, instr.getCycles(instr.getContext()));
        assertEquals(0xE0, registers.getA());
        assertEquals(0x2000, registers.getPC());
        assertFalse(flags.isZFlag());
        assertTrue(flags.isNFlag());
        assertFalse(flags.isHFlag());
        assertFalse(flags.isCFlag());
    }

    @Test
    void testSbcA_C_halfCarrySet_0x99() {
        registers.setPC(0x2000);
        registers.setA(0xF1);
        registers.setC(0x03);
        registers.getFlags().setCFlag(true);

        var instr = Instructions.get(0x99);

        int accumulator = 0;
        for (var operation : instr.getOperations()) {
            accumulator = operation.execute(registers, addressSpace, accumulator, null);
        }

        var flags = registers.getFlags();
        assertEquals(1, instr.getCycles(instr.getContext()));
        assertEquals(0xED, registers.getA());
        assertEquals(0x2000, registers.getPC());
        assertFalse(flags.isZFlag());
        assertTrue(flags.isNFlag());
        assertTrue(flags.isHFlag());
        assertFalse(flags.isCFlag());
    }

    @Test
    void testSbcA_immediateByte_0xDE() {
        registers.setPC(0x2000);
        registers.setA(0xF1);
        registers.getFlags().setCFlag(false);
        addressSpace.set(0x2001, 0x11);

        var instr = Instructions.get(0xDE);

        int accumulator = 0;
        for (var operation : instr.getOperations()) {
            accumulator = operation.execute(registers, addressSpace, accumulator, null);
        }

        var flags = registers.getFlags();
        assertEquals(2, instr.getCycles(instr.getContext()));
        assertEquals(0xE0, registers.getA());
        assertEquals(0x2001, registers.getPC());
        assertFalse(flags.isZFlag());
        assertTrue(flags.isNFlag());
        assertFalse(flags.isHFlag());
        assertFalse(flags.isCFlag());
    }

    @Test
    void testAnd_immediateByte_0xE6() {
        registers.setPC(0x2000);
        registers.setA(0xF1);
        addressSpace.set(0x2001, 0xC2);

        var instr = Instructions.get(0xE6);

        int accumulator = 0;
        for (var operation : instr.getOperations()) {
            accumulator = operation.execute(registers, addressSpace, accumulator, null);
        }

        var flags = registers.getFlags();
        assertEquals(2, instr.getCycles(instr.getContext()));
        assertEquals(0xC0, registers.getA());
        assertEquals(0x2001, registers.getPC());
        assertFalse(flags.isZFlag());
        assertFalse(flags.isNFlag());
        assertTrue(flags.isHFlag());
        assertFalse(flags.isCFlag());
    }

    @Test
    void testOR_D_0xB2() {
        registers.setPC(0x2000);
        registers.setA(0x81);
        registers.setD(0x44);

        var instr = Instructions.get(0xB2);

        int accumulator = 0;
        for (var operation : instr.getOperations()) {
            accumulator = operation.execute(registers, addressSpace, accumulator, null);
        }

        var flags = registers.getFlags();
        assertEquals(1, instr.getCycles(instr.getContext()));
        assertEquals(0xC5, registers.getA());
        assertEquals(0x2000, registers.getPC());
        assertFalse(flags.isZFlag());
        assertFalse(flags.isNFlag());
        assertFalse(flags.isHFlag());
        assertFalse(flags.isCFlag());
    }

    @Test
    void testOR_B_isZero_0xB0() {
        registers.setPC(0x2000);
        registers.setA(0x00);
        registers.setD(0x00);

        var instr = Instructions.get(0xB0);

        int accumulator = 0;
        for (var operation : instr.getOperations()) {
            accumulator = operation.execute(registers, addressSpace, accumulator, null);
        }

        var flags = registers.getFlags();
        assertEquals(1, instr.getCycles(instr.getContext()));
        assertEquals(0x00, registers.getA());
        assertEquals(0x2000, registers.getPC());
        assertTrue(flags.isZFlag());
        assertFalse(flags.isNFlag());
        assertFalse(flags.isHFlag());
        assertFalse(flags.isCFlag());
    }

    @Test
    void testXOR_B_0xA8() {
        registers.setPC(0x2000);
        registers.setA(0xFF);
        registers.setB(0xEA);

        var instr = Instructions.get(0xA8);

        int accumulator = 0;
        for (var operation : instr.getOperations()) {
            accumulator = operation.execute(registers, addressSpace, accumulator, null);
        }

        var flags = registers.getFlags();
        assertEquals(1, instr.getCycles(instr.getContext()));
        assertEquals(0x15, registers.getA());
        assertEquals(0x2000, registers.getPC());
        assertFalse(flags.isZFlag());
        assertFalse(flags.isNFlag());
        assertFalse(flags.isHFlag());
        assertFalse(flags.isCFlag());
    }

    @Test
    void testCP_C_regAGreater_0xB9() {
        registers.setPC(0x2000);
        registers.setA(0xFF);
        registers.setC(0xEA);

        var instr = Instructions.get(0xB9);

        int accumulator = 0;
        for (var operation : instr.getOperations()) {
            accumulator = operation.execute(registers, addressSpace, accumulator, null);
        }

        var flags = registers.getFlags();
        assertEquals(1, instr.getCycles(instr.getContext()));
        assertEquals(0xFF, registers.getA());
        assertEquals(0xEA, registers.getC());
        assertEquals(0x2000, registers.getPC());
        assertFalse(flags.isZFlag());
        assertTrue(flags.isNFlag());
        assertFalse(flags.isHFlag());
        assertFalse(flags.isCFlag());
    }

    @Test
    void testCP_C_regALess_0xB9() {
        registers.setPC(0x2000);
        registers.setA(0xEA);
        registers.setC(0xFF);

        var instr = Instructions.get(0xB9);

        int accumulator = 0;
        for (var operation : instr.getOperations()) {
            accumulator = operation.execute(registers, addressSpace, accumulator, null);
        }

        var flags = registers.getFlags();
        assertEquals(1, instr.getCycles(instr.getContext()));
        assertEquals(0xEA, registers.getA());
        assertEquals(0xFF, registers.getC());
        assertEquals(0x2000, registers.getPC());
        assertFalse(flags.isZFlag());
        assertTrue(flags.isNFlag());
        assertTrue(flags.isHFlag());
        assertTrue(flags.isCFlag());
    }

    @Test
    void testInc_A_0x3C() {
        registers.setPC(0x2000);
        registers.setA(0xFF);

        var instr = Instructions.get(0x3C);

        int accumulator = 0;
        for (var operation : instr.getOperations()) {
            accumulator = operation.execute(registers, addressSpace, accumulator, null);
        }

        var flags = registers.getFlags();
        assertEquals(1, instr.getCycles(instr.getContext()));
        assertEquals(0x00, registers.getA());
        assertEquals(0x2000, registers.getPC());
        assertTrue(flags.isZFlag());
        assertFalse(flags.isNFlag());
        assertTrue(flags.isHFlag());
    }

    @Test
    void testInc_HL_0x34() {
        registers.setPC(0x2000);
        registers.setHL(0x1234);
        addressSpace.set(0x1234, 0xFF);

        var instr = Instructions.get(0x34);

        int accumulator = 0;
        for (var operation : instr.getOperations()) {
            accumulator = operation.execute(registers, addressSpace, accumulator, null);
        }

        var flags = registers.getFlags();
        assertEquals(3, instr.getCycles(instr.getContext()));
        assertEquals(0x00, addressSpace.get(0x1234));
        assertEquals(0x2000, registers.getPC());
        assertTrue(flags.isZFlag());
        assertFalse(flags.isNFlag());
        assertTrue(flags.isHFlag());
    }

    @Test
    void testDec_E_0x1D() {
        registers.setPC(0x2000);
        registers.setE(0x10);

        var instr = Instructions.get(0x1D);

        int accumulator = 0;
        for (var operation : instr.getOperations()) {
            accumulator = operation.execute(registers, addressSpace, accumulator, null);
        }

        var flags = registers.getFlags();
        assertEquals(1, instr.getCycles(instr.getContext()));
        assertEquals(0x0F, registers.getE());
        assertEquals(0x2000, registers.getPC());
        assertFalse(flags.isZFlag());
        assertTrue(flags.isNFlag());
        assertTrue(flags.isHFlag());
    }

}
