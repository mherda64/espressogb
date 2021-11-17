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
        r.setAF(0x1234);

        // then
        assertEquals(0x12, r.getA());
        assertEquals(0x34, r.getFlags().getByte());
    }

    @Test
    void getAFTest_shouldPass() {
        // given
        r = new Registers();

        // when
        r.setA(0xFA);
        r.setFlags(0xCC);

        // then
        assertEquals(0xFACC, r.getAF());
    }

}
