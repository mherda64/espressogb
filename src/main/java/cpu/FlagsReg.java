package cpu;

import static cpu.BitUtils.*;

public class FlagsReg {

    private int f;

    private static final int Z_FLAG = 7;

    private static final int N_FLAG = 6;

    private static final int H_FLAG = 5;

    private static final int C_FLAG = 4;

    public FlagsReg(int f) {
        isByte(f);
        this.f = f;
    }

    public FlagsReg() {
    }

    public boolean isZFlag() {
        return getByteBit(f, Z_FLAG);
    }

    public boolean isNFlag() {
        return getByteBit(f, N_FLAG);
    }

    public boolean isHFlag() {
        return getByteBit(f, H_FLAG);
    }

    public boolean isCFlag() {
        return getByteBit(f, C_FLAG);
    }

    public void setZFlag(boolean value) {
        f = setByteBit(f, Z_FLAG, value);
    }

    public void setNFlag(boolean value) {
        f = setByteBit(f, N_FLAG, value);
    }

    public void setHFlag(boolean value) {
        f = setByteBit(f, H_FLAG, value);
    }

    public void setCFlag(boolean value) {
        f = setByteBit(f, C_FLAG, value);
    }

    public int getByte() {
        return f & 0xFF;
    }

    public void setFlagsByte(int value) {
        isByte(value);
        f = value & 0xF0;
    }

}
