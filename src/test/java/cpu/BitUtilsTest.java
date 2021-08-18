package cpu;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static cpu.BitUtils.*;

class BitUtilsTest {

    @Test
    void isByteValue_shouldReturnTrue() {
        assertTrue(isByte(0x15));
        assertTrue(isByte(0xFF));
        assertTrue(isByte(0x0));
    }

    @Test
    void isByteValue_shouldThrow() {
        assertThrows(IllegalArgumentException.class, () -> isByte(0xFFFF));
        assertThrows(IllegalArgumentException.class, () -> isByte(0xFF0));
        assertThrows(IllegalArgumentException.class, () -> isByte(-0x1));
    }

    @Test
    void isShortValue_shouldReturnTrue() {
        assertTrue(isShort(0xFF));
        assertTrue(isShort(0x01));
        assertTrue(isShort(0xFFF));
        assertTrue(isShort(0xFFFF));
        assertTrue(isShort(0x0));
    }

    @Test
    void isShortValue_shouldThrow() {
        assertThrows(IllegalArgumentException.class, () -> isShort(0xFFFF1));
        assertThrows(IllegalArgumentException.class, () -> isShort(0x12345));
        assertThrows(IllegalArgumentException.class, () -> isShort(-0x1));
    }

    @Test
    void getByteBit_shouldPass() {
        assertTrue(getByteBit(0xFF, 7));
        assertTrue(getByteBit(0x80, 7));
        assertFalse(getByteBit(0x0, 0));
        assertFalse(getByteBit(0xE1, 4));

    }

    @Test
    void getByteBit_shouldThrow() {
        assertThrows(IllegalArgumentException.class, () -> getByteBit(0xFF, -1));
        assertThrows(IllegalArgumentException.class, () -> getByteBit(0xFF, 8));
        assertThrows(IllegalArgumentException.class, () -> getByteBit(0xFFF, 2));
        assertThrows(IllegalArgumentException.class, () -> getByteBit(-0xF, 2));
    }

    @Test
    void getShortBit_shouldPass() {
        assertTrue(getShortBit(0xFFFF, 15));
        assertTrue(getShortBit(0x1080, 12));
        assertTrue(getShortBit(0x1, 0));
        assertTrue(getShortBit(0x1F11, 11));
    }

    @Test
    void getShortBit_shouldThrow() {
        assertThrows(IllegalArgumentException.class, () -> getShortBit(0xFF1FF, -1));
        assertThrows(IllegalArgumentException.class, () -> getShortBit(0xFF, 16));
        assertThrows(IllegalArgumentException.class, () -> getShortBit(0x11FFF, 2));
        assertThrows(IllegalArgumentException.class, () -> getShortBit(-0xF, 2));
    }

    @Test
    void setByteBit_shouldPass() {
        assertEquals(0x80, setByteBit(0x0, 7, true));
        assertEquals(0x40, setByteBit(0x0, 6, true));
        assertEquals(0x0F, setByteBit(0x1F, 4, false));
        assertEquals(0x07, setByteBit(0x0F, 3, false));
    }

    @Test
    void setByteBit_shouldThrow() {
        assertThrows(IllegalArgumentException.class, () -> setByteBit(0x0, 9, true));
        assertThrows(IllegalArgumentException.class, () -> setByteBit(0x0, -1, true));
        assertThrows(IllegalArgumentException.class, () -> setByteBit(0x11F, 4, false));
        assertThrows(IllegalArgumentException.class, () -> setByteBit(-0x0F, 3, false));
    }

    @Test
    void setShortBit_shouldPass() {
        assertEquals(0x0180, setShortBit(0x80, 8, true));
        assertEquals(0xEFFF, setShortBit(0xFFFF, 12, false));
        assertEquals(0x000F, setShortBit(0x000F, 15, false));
        assertEquals(0xFFF7, setShortBit(0xFFFF, 3, false));
    }

    @Test
    void setShortBit_shouldThrow() {
        assertThrows(IllegalArgumentException.class, () -> setShortBit(0x0, 16, true));
        assertThrows(IllegalArgumentException.class, () -> setShortBit(0x0, -1, true));
        assertThrows(IllegalArgumentException.class, () -> setShortBit(0xFF11F, 4, false));
        assertThrows(IllegalArgumentException.class, () -> setShortBit(-0x0F, 3, false));
    }
}
