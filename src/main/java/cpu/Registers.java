package cpu;

import static cpu.BitUtils.isByte;
import static cpu.BitUtils.isShort;

public class Registers {

    private int a;
    private int b;
    private int c;
    private int d;
    private int e;
    private FlagsReg f;
    private int h;
    private int l;

    private int sp;
    private int pc;

    public Registers() {
        f = new FlagsReg();
    }

    public void set(RegEnum reg, int value) {
        if (reg.size == RegEnum.SINGLE) {
            switch (reg) {
                case A: {
                    setA(value);
                    break;
                }
                case F: {
                    setFlags(value);
                    break;
                }
                case B: {
                    setB(value);
                    break;
                }
                case C: {
                    setC(value);
                    break;
                }
                case D: {
                    setD(value);
                    break;
                }
                case E: {
                    setE(value);
                    break;
                }
                case H: {
                    setH(value);
                    break;
                }
                case L: {
                    setL(value);
                    break;
                }
            }
        } else {
            switch (reg) {
                case AF: {
                    setAF(value);
                    break;
                }
                case BC: {
                    setBC(value);
                    break;
                }
                case DE: {
                    setDE(value);
                    break;
                }
                case HL: {
                    setHL(value);
                    break;
                }
            }
        }
    }

    public int incPC() {
        pc += 1;
        return pc;
    }

    public int getA() {
        return a;
    }

    public void setA(int a) {
        isByte(a);
        this.a = a;
    }

    public int getB() {
        return b;
    }

    public void setB(int b) {
        isByte(b);
        this.b = b;
    }

    public int getC() {
        return c;
    }

    public void setC(int c) {
        isByte(c);
        this.c = c;
    }

    public int getD() {
        return d;
    }

    public void setD(int d) {
        isByte(d);
        this.d = d;
    }

    public int getE() {
        return e;
    }

    public void setE(int e) {
        isByte(e);
        this.e = e;
    }

    public FlagsReg getFlags() {
        return f;
    }

    public void setFlags(int f) {
        isByte(f);
        this.f.setFlagsByte(f);
    }

    public int getH() {
        return h;
    }

    public void setH(int h) {
        isByte(h);
        this.h = h;
    }

    public int getL() {
        return l;
    }

    public void setL(int l) {
        isByte(l);
        this.l = l;
    }

    public int getSP() {
        return sp;
    }

    public void setSP(int sp) {
        isShort(sp);
        this.sp = sp;
    }

    public int getPC() {
        return pc;
    }

    public void setPC(int pc) {
        isShort(pc);
        this.pc = pc;
    }

    public int getAF() {
        return a << 8 | f.getFlagsByte();
    }

    public void setAF(int af) {
        isShort(af);
        a = (af & 0xFF00) >>> 8;
        f.setFlagsByte(af & 0xFF);
    }

    public int getBC() {
        return b << 8 | c;
    }

    public void setBC(int bc) {
        isShort(bc);
        b = (bc & 0xFF00) >>> 8;
        c = bc & 0xFF;
    }

    public int getDE() {
        return d << 8 | e;
    }

    public void setDE(int de) {
        isShort(de);
        d = (de & 0xFF00) >>> 8;
        e = de & 0xFF;
    }

    public int getHL() {
        return h << 8 | l;
    }

    public void setHL(int hl) {
        isShort(hl);
        h = (hl & 0xFF00) >>> 8;
        l = hl & 0xFF;
    }
}
