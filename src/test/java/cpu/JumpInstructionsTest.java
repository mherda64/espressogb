package cpu;

import cpu.instruction.Opcode;
import memory.AddressSpace;
import memory.Memory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.stream.Stream;

import static cpu.InstructionsTest.executeInstruction;
import static org.junit.jupiter.api.Assertions.assertEquals;

class JumpInstructionsTest {
    Registers registers;
    AddressSpace addressSpace;

    @BeforeEach
    void init() {
        registers = new Registers();
        addressSpace = new Memory(0xFFFF);
    }

    @Test
    void testJP_0xC3() {
        registers.setPC(0x2001);
        addressSpace.set(0x2001, 0xFF);
        addressSpace.set(0x2002, 0x25);

        var instr = executeInstruction(0xC3, false, registers, addressSpace);

        assertEquals(4, instr.getCycles(instr.getContext()));
        assertEquals(0x25FF, registers.getPC());
    }

    @Test
    void testJP_sameAddress_0xC3() {
        registers.setPC(0x2001);
        addressSpace.set(0x2001, 0x23);
        addressSpace.set(0x2002, 0x21);

        var instr = executeInstruction(0xC3, false, registers, addressSpace);

        assertEquals(4, instr.getCycles(instr.getContext()));
        assertEquals(0x2123, registers.getPC());
    }

    @Test
    void testJP_NZ_shouldJump_0xC2() {
        registers.setPC(0x2001);
        addressSpace.set(0x2001, 0x23);
        addressSpace.set(0x2002, 0x21);
        registers.getFlags().setZFlag(false);

        var instr = executeInstruction(0xC2, false, registers, addressSpace);

        assertEquals(4, instr.getCycles(instr.getContext()));
        assertEquals(0x2123, registers.getPC());
    }

    @Test
    void testJP_NZ_shouldNotJump_0xC2() {
        registers.setPC(0x2001);
        addressSpace.set(0x2001, 0x23);
        addressSpace.set(0x2002, 0x21);
        registers.getFlags().setZFlag(true);

        var instr = executeInstruction(0xC2, false, registers, addressSpace);

        assertEquals(3, instr.getCycles(instr.getContext()));
        assertEquals(0x2003, registers.getPC());
    }

    @Test
    void testJP_Z_shouldNotJump_0xCA() {
        registers.setPC(0x2001);
        addressSpace.set(0x2001, 0x23);
        addressSpace.set(0x2002, 0x21);
        registers.getFlags().setZFlag(false);

        var instr = executeInstruction(0xCA, false, registers, addressSpace);

        assertEquals(3, instr.getCycles(instr.getContext()));
        assertEquals(0x2003, registers.getPC());
    }

    @Test
    void testJP_Z_shouldJump_0xCA() {
        registers.setPC(0x2001);
        addressSpace.set(0x2001, 0x37);
        addressSpace.set(0x2002, 0x21);
        registers.getFlags().setZFlag(true);

        var instr = executeInstruction(0xCA, false, registers, addressSpace);

        assertEquals(4, instr.getCycles(instr.getContext()));
        assertEquals(0x2137, registers.getPC());
    }

    @Test
    void testJP_C_shouldJump_0xDA() {
        registers.setPC(0x2001);
        addressSpace.set(0x2001, 0xFF);
        addressSpace.set(0x2002, 0x24);
        registers.getFlags().setCFlag(true);

        var instr = executeInstruction(0xDA, false, registers, addressSpace);

        assertEquals(4, instr.getCycles(instr.getContext()));
        assertEquals(0x24FF, registers.getPC());
    }

    @Test
    void testJP_HL_0xE9() {
        registers.setPC(0x2000);
        registers.setHL(0x1234);

        var instr = executeInstruction(0xE9, false, registers, addressSpace);

        assertEquals(1, instr.getCycles(instr.getContext()));
        assertEquals(0x1234, registers.getPC());
    }

    @Test
    void testJR_add127_0x18() {
        registers.setPC(0x2000);
        addressSpace.set(0x2000, 0x7F);

        var instr = executeInstruction(0x18, false, registers, addressSpace);

        assertEquals(3, instr.getCycles(instr.getContext()));
        assertEquals(0x2080, registers.getPC());
    }

    @Test
    void testJR_sub128_0x18() {
        registers.setPC(0x2000);
        addressSpace.set(0x2000, 0x80);

        var instr = executeInstruction(0x18, false, registers, addressSpace);

        assertEquals(3, instr.getCycles(instr.getContext()));
        assertEquals(0x1F81, registers.getPC());
    }


    @Test
    void testJR_NZ_add128_0x20() {
        registers.setPC(0x2000);
        addressSpace.set(0x2000, 0x7F);
        registers.getFlags().setZFlag(false);

        var instr = executeInstruction(0x20, false, registers, addressSpace);

        assertEquals(3, instr.getCycles(instr.getContext()));
        assertEquals(0x2080, registers.getPC());
    }

    @Test
    void testJR_Z_sub128_0x28() {
        registers.setPC(0x2000);
        addressSpace.set(0x2000, 0x80);
        registers.getFlags().setZFlag(true);

        var instr = executeInstruction(0x28, false, registers, addressSpace);

        assertEquals(3, instr.getCycles(instr.getContext()));
        assertEquals(0x1F81, registers.getPC());
    }


    @Test
    void testJR_C_shouldNotJump_0x38() {
        registers.setPC(0x2000);
        addressSpace.set(0x2000, 0x80);
        registers.getFlags().setCFlag(false);

        var instr = executeInstruction(0x38, false, registers, addressSpace);

        assertEquals(2, instr.getCycles(instr.getContext()));
        assertEquals(0x2001, registers.getPC());
    }

    @Test
    void testCall_0xCD() {
        registers.setPC(0x2001);
        registers.setSP(0xDFFF);
        addressSpace.set(0x2001, 0x80);
        addressSpace.set(0x2002, 0x31);

        var instr = executeInstruction(0xCD, false, registers, addressSpace);

        assertEquals(6, instr.getCycles(instr.getContext()));
        assertEquals(0x3180, registers.getPC());
        assertEquals(0xDFFD, registers.getSP());
        assertEquals(0x20, addressSpace.get(0xDFFF));
        assertEquals(0x03, addressSpace.get(0xDFFE));
    }

    @Test
    void testCall_NC_shouldCall_0xD4() {
        registers.setPC(0x2001);
        registers.setSP(0xDFFF);
        addressSpace.set(0x2001, 0x80);
        addressSpace.set(0x2002, 0x31);
        registers.getFlags().setCFlag(false);

        var instr = executeInstruction(0xD4, false, registers, addressSpace);

        assertEquals(6, instr.getCycles(instr.getContext()));
        assertEquals(0x3180, registers.getPC());
        assertEquals(0xDFFD, registers.getSP());
        assertEquals(0x20, addressSpace.get(0xDFFF));
        assertEquals(0x03, addressSpace.get(0xDFFE));

    }

    @Test
    void testCall_NC_shouldNotCall_0xD4() {
        registers.setPC(0x2001);
        registers.setSP(0xE000);
        addressSpace.set(0x2001, 0x80);
        addressSpace.set(0x2002, 0x31);
        registers.getFlags().setCFlag(true);

        var instr = executeInstruction(0xD4, false, registers, addressSpace);

        assertEquals(3, instr.getCycles(instr.getContext()));
        assertEquals(0x2003, registers.getPC());
        assertEquals(0xE000, registers.getSP());
    }

    @Test
    void testRST() {
        Stream.of(
                new Opcode<>(0xC7, 0x00, 4),
                new Opcode<>(0xCF, 0x08, 4),
                new Opcode<>(0xD7, 0x10, 4),
                new Opcode<>(0xDF, 0x18, 4),
                new Opcode<>(0xE7, 0x20, 4),
                new Opcode<>(0xEF, 0x28, 4),
                new Opcode<>(0xF7, 0x30, 4),
                new Opcode<>(0xFF, 0x38, 4)
        ).forEach(opcode -> {
                    registers.setPC(0x2000);
                    registers.setSP(0xDFFF);

                    var instr = executeInstruction(opcode.getOpcode(), false, registers, addressSpace);

                    assertEquals(4, instr.getCycles(instr.getContext()));
                    assertEquals((int) opcode.getTarget(), registers.getPC());
                    assertEquals(0x20, addressSpace.get(0xDFFF));
                    assertEquals(0x00, addressSpace.get(0xDFFE));
                    assertEquals(0xDFFD, registers.getSP());
                }
        );
    }

    @Test
    void testRet_0xC9() {
        registers.setPC(0x2000);
        registers.setSP(0xDFFD);
        addressSpace.set(0xDFFE, 0x80);
        addressSpace.set(0xDFFF, 0x31);

        var instr = executeInstruction(0xC9, false, registers, addressSpace);

        assertEquals(4, instr.getCycles(instr.getContext()));
        assertEquals(0x3180, registers.getPC());
        assertEquals(0xDFFF, registers.getSP());
    }

    @Test
    void testRet_Z_shouldRet_0xC8() {
        registers.setPC(0x2000);
        registers.setSP(0xDFFD);
        addressSpace.set(0xDFFE, 0x80);
        addressSpace.set(0xDFFF, 0x31);
        registers.getFlags().setZFlag(true);

        var instr = executeInstruction(0xC8, false, registers, addressSpace);

        assertEquals(5, instr.getCycles(instr.getContext()));
        assertEquals(0x3180, registers.getPC());
        assertEquals(0xDFFF, registers.getSP());
    }

    @Test
    void testRet_Z_shouldNotRet_0xC8() {
        registers.setPC(0x2001);
        registers.setSP(0xDFFD);
        addressSpace.set(0xDFFE, 0x80);
        addressSpace.set(0xDFFF, 0x31);
        registers.getFlags().setZFlag(false);

        var instr = executeInstruction(0xC8, false, registers, addressSpace);

        assertEquals(2, instr.getCycles(instr.getContext()));
        assertEquals(0x2001, registers.getPC());
        assertEquals(0xDFFD, registers.getSP());
    }
}
