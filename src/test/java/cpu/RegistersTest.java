package cpu;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RegistersTest {

    Registers r;

    @Test
    void setAFTest_shouldPass() {
        // given
        r = new Registers();

        // when
        // low 4 bytes of F are masked out
        r.setAF(0x1234);

        // then
        assertEquals(0x12, r.getA());
        assertEquals(0x30, r.getFlags().getByte());
    }

    @Test
    void getAFTest_shouldPass() {
        // given
        r = new Registers();

        // when
        r.setA(0xFA);
        // low 4 bytes of F are masked out
        r.setFlags(0xCC);

        // then
        assertEquals(0xFAC0, r.getAF());
    }

}
