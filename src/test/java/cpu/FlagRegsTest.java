package cpu;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FlagRegsTest {

    FlagsReg f;

    @Test
    void isZFlagTest_shouldPass() {
        f = new FlagsReg(0x80);

        assertEquals(true, f.isZFlag());
    }

    @Test
    void isZFlagTest_shouldThrow() {
        assertThrows(IllegalArgumentException.class, () -> new FlagsReg(0xF00));
    }

    @Test
    void setZFlag_shouldPass() {
        // given
        f = new FlagsReg(0x0);

        // when
        f.setZFlag(true);

        // then
        assertTrue(f.isZFlag());
    }

}
