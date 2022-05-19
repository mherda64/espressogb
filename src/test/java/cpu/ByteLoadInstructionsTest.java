package cpu;

import memory.AddressSpace;
import memory.BasicMemory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static cpu.InstructionsTest.executeInstruction;
import static org.junit.jupiter.api.Assertions.*;

class ByteLoadInstructionsTest {

    Registers registers;
    AddressSpace addressSpace;

    @BeforeEach
    void init() {
        registers = new Registers();
        addressSpace = new BasicMemory(0xFFFF);
    }

    @Test
    void testLoadByte_0x0E_shouldPass() {
        registers.setPC(0x2002);
        addressSpace.set(0x2002, 0x99);

        var instr = executeInstruction(0x0E, false, registers, addressSpace);

        assertEquals(2, instr.getCycles(instr.getContext()));
        assertEquals(0x99, registers.getC());
        assertEquals(0x2003, registers.getPC());
    }

    @Test
    void testLoadByte_0x06_shouldPass() {
        registers.setPC(0x2001);
        addressSpace.set(0x2001, 0x99);

        var instr = executeInstruction(0x06, false, registers, addressSpace);

        assertEquals(2, instr.getCycles(instr.getContext()));
        assertEquals(0x99, registers.getB());
        assertEquals(0x2002, registers.getPC());
    }

    @Test
    void testLoadA_0x7A() {
        registers.setPC(0x2001);
        registers.setD(0xF1);

        var instr = executeInstruction(0x7A, false, registers, addressSpace);

        assertEquals(1, instr.getCycles(instr.getContext()));
        assertEquals(0xF1, registers.getA());
        assertEquals(0x2001, registers.getPC());
    }

    @Test
    void testLoadA_0x0A() {
        registers.setPC(0x2001);
        registers.setBC(0x2222);
        addressSpace.set(0x2222, 0x15);

        var instr = executeInstruction(0x0A, false, registers, addressSpace);

        assertEquals(2, instr.getCycles(instr.getContext()));
        assertEquals(0x15, registers.getA());
        assertEquals(0x2001, registers.getPC());
    }

    @Test
    void testLoadInstAddress_0xFA() {
        registers.setPC(0x2001);
        addressSpace.set(0x2001, 0x15);
        addressSpace.set(0x2002, 0x2A);
        addressSpace.set(0x2A15, 0xFF);

        var instr = executeInstruction(0xFA, false, registers, addressSpace);


        assertEquals(4, instr.getCycles(instr.getContext()));
        assertEquals(0xFF, registers.getA());
        assertEquals(0x2003, registers.getPC());
    }

    @Test
    void testLoadBWithA_0x47() {
        registers.setPC(0x2000);
        registers.setA(0xFA);

        var instr = executeInstruction(0x47, false, registers, addressSpace);

        assertEquals(1, instr.getCycles(instr.getContext()));
        assertEquals(0xFA, registers.getB());
        assertEquals(0x2000, registers.getPC());
    }

    @Test
    void testLoadBCAddressWithA_0x02() {
        registers.setPC(0x2000);
        registers.setA(0xFA);
        registers.setBC(0x4001);

        var instr = executeInstruction(0x02, false, registers, addressSpace);


        assertEquals(2, instr.getCycles(instr.getContext()));
        assertEquals(0xFA, addressSpace.get(0x4001));
        assertEquals(0x2000, registers.getPC());
    }

    @Test
    void testLoadImmediateAddressWithA_0xEA() {
        registers.setPC(0xC001);
        registers.setA(0xFA);
        addressSpace.set(0xC001, 0x15);
        addressSpace.set(0xC002, 0xC1);

        var instr = executeInstruction(0xEA, false, registers, addressSpace);


        assertEquals(4, instr.getCycles(instr.getContext()));
        assertEquals(0xFA, addressSpace.get(0xC115));
        assertEquals(0xC003, registers.getPC());
    }

    @Test
    void testLoadA_regCAddress_0xF2() {
        registers.setPC(0x2000);
        registers.setC(0x15);
        addressSpace.set(0xFF15, 0x88);

        var instr = executeInstruction(0xF2, false, registers, addressSpace);


        assertEquals(2, instr.getCycles(instr.getContext()));
        assertEquals(0x88, registers.getA());
        assertEquals(0x2000, registers.getPC());
    }

    @Test
    void testLoadRegCAddress_regA_0xE2() {
        registers.setPC(0x2000);
        registers.setA(0xFE);
        registers.setC(0x15);
        addressSpace.set(0xFF15, 0x88);

        var instr = executeInstruction(0xE2, false, registers, addressSpace);


        assertEquals(2, instr.getCycles(instr.getContext()));
        assertEquals(0xFE, addressSpace.get(0xFF15));
        assertEquals(0x2000, registers.getPC());
    }

    @Test
    void testLoadRegA_regHLAddress_decHL_0x3A() {
        registers.setPC(0x2000);
        registers.setA(0xFE);
        registers.setHL(0xC123);
        addressSpace.set(0xC123, 0x88);

        var instr = executeInstruction(0x3A, false, registers, addressSpace);


        assertEquals(2, instr.getCycles(instr.getContext()));
        assertEquals(0x88, registers.getA());
        assertEquals(0x2000, registers.getPC());
        assertEquals(0xC122, registers.getHL());
    }

    @Test
    void testLoadRegHLAddress_regA_decHL_0x32() {
        registers.setPC(0x2000);
        registers.setA(0xFE);
        registers.setHL(0xC123);

        var instr = executeInstruction(0x32, false, registers, addressSpace);


        assertEquals(2, instr.getCycles(instr.getContext()));
        assertEquals(0xFE, addressSpace.get(0xC123));
        assertEquals(0x2000, registers.getPC());
        assertEquals(0xC122, registers.getHL());
    }

    @Test
    void testLoadRegA_regHLAddress_incHL_0x2A() {
        registers.setPC(0x2000);
        registers.setA(0xFE);
        registers.setHL(0xC123);
        addressSpace.set(0xC123, 0x88);

        var instr = executeInstruction(0x2A, false, registers, addressSpace);


        assertEquals(2, instr.getCycles(instr.getContext()));
        assertEquals(0x88, registers.getA());
        assertEquals(0x2000, registers.getPC());
        assertEquals(0xC124, registers.getHL());
    }

    @Test
    void testLoadRegHLAddress_regA_incHL_0x22() {
        registers.setPC(0x2000);
        registers.setA(0xFE);
        registers.setHL(0xC123);

        var instr = executeInstruction(0x22, false, registers, addressSpace);


        assertEquals(2, instr.getCycles(instr.getContext()));
        assertEquals(0xFE, addressSpace.get(0xC123));
        assertEquals(0x2000, registers.getPC());
        assertEquals(0xC124, registers.getHL());
    }

    @Test
    void testLoadImmediateAddress_regA_0xE0() {
        registers.setPC(0x2000);
        registers.setA(0x1C);
        addressSpace.set(0x2000, 0x12);

        var instr = executeInstruction(0xE0, false, registers, addressSpace);


        assertEquals(3, instr.getCycles(instr.getContext()));
        assertEquals(0x1C, addressSpace.get(0xFF12));
        assertEquals(0x2001, registers.getPC());
    }

    @Test
    void testLoadRegA_immediateAddress_0xF0() {
        registers.setPC(0x2000);
        registers.setA(0x1C);
        addressSpace.set(0x2000, 0x12);
        addressSpace.set(0xFF12, 0x9A);

        var instr = executeInstruction(0xF0, false, registers, addressSpace);
        
        assertEquals(3, instr.getCycles(instr.getContext()));
        assertEquals(0x9A, registers.getA());
        assertEquals(0x2001, registers.getPC());
    }

}
