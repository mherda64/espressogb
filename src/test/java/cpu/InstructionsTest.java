package cpu;

import memory.AddressSpace;
import memory.Memory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class InstructionsTest {

    Registers registers;
    AddressSpace addressSpace;
//    Instructions instructions = new Instructions();

    @Mock
    AddressSpace mockAddressSpace;

    @BeforeEach
    void init() {
        registers = new Registers();
        addressSpace = new Memory(0xFFFF);
    }

    @Test
    void testLoadByte_0x0E_shouldPass() {
        registers.setPC(0x2001);
        addressSpace.set(0x2001, 0x99);

        Instruction instr = Instructions.get(0x0E);

        int accumulator = 0;
        for (var operation : instr.getOperations()) {
            accumulator = operation.execute(registers, addressSpace, accumulator);
            registers.incPC();
        }

        assertEquals(0x99, registers.getC());
        assertEquals(0x2003, registers.getPC());
    }

    @Test
    void testLoadByte_0x06_shouldPass() {
        registers.setPC(0x2001);
        addressSpace.set(0x2001, 0x99);

        Instruction instr = Instructions.get(0x06);

        int accumulator = 0;
        for (var operation : instr.getOperations()) {
            accumulator = operation.execute(registers, addressSpace, accumulator);
            registers.incPC();
        }
        assertEquals(0x99, registers.getB());
        assertEquals(0x2003, registers.getPC());
    }

//    @Test
//    void testLoadByte_0x06_shouldFail() {
//        registers.setPC(0x2001);
//        // Even though it's not possible, it should fail
//        when(mockAddressSpace.get(anyInt())).thenReturn(0xF00);
//
//        Instruction instr = Instructions.get(0x06);
//
//
//        assertThrows(IllegalArgumentException.class, () -> instr.execute(registers, mockAddressSpace));
//    }

}
