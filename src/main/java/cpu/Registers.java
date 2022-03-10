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

    public int get(RegEnum reg) {
        if (reg.size == RegEnum.SINGLE) {
            switch (reg) {
                case A: {
                    return getA();
                }
                case F: {
                    return getFlags().getByte();
                }
                case B: {
                    return getB();
                }
                case C: {
                    return getC();
                }
                case D: {
                    return getD();
                }
                case E: {
                    return getE();
                }
                case H: {
                    return getH();
                }
                case L: {
                    return getL();
                }
            }
        } else {
            switch (reg) {
                case AF: {
                    return getAF();
                }
                case BC: {
                    return getBC();
                }
                case DE: {
                    return getDE();
                }
                case HL: {
                    return getHL();
                }
                case SP: {
                    return getSP();
                }
                case PC: {
                    return getPC();
                }
            }
        }
        throw new IllegalStateException(String.format("There is no such register as %s!", reg));
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
                case SP: {
                    setSP(value);
                    break;
                }
                case PC: {
                    setPC(value);
                    break;
                }
            }
        }
    }

    public int incPC() {
//        pc += 1;
        return pc++;
    }

    public int decSP() {
        sp -= 1;
        return sp;
    }

    public int incSP() {
        sp += 1;
        return sp;
    }

    public int getA() {
        return a & 0xFF;
    }

    public void setA(int a) {
        isByte(a);
        this.a = a;
    }

    public int getB() {
        return b & 0xFF;
    }

    public void setB(int b) {
        isByte(b);
        this.b = b;
    }

    public int getC() {
        return c & 0xFF;
    }

    public void setC(int c) {
        isByte(c);
        this.c = c;
    }

    public int getD() {
        return d & 0xFF;
    }

    public void setD(int d) {
        isByte(d);
        this.d = d;
    }

    public int getE() {
        return e & 0xFF;
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
        return h & 0xFF;
    }

    public void setH(int h) {
        isByte(h);
        this.h = h;
    }

    public int getL() {
        return l & 0xFF;
    }

    public void setL(int l) {
        isByte(l);
        this.l = l;
    }

    public int getSP() {
        return sp & 0xFFFF;
    }

    public void setSP(int sp) {
        isShort(sp);
        this.sp = sp;
    }

    public int getPC() {
        return pc & 0xFFFF;
    }

    public void setPC(int pc) {
        isShort(pc);
        this.pc = pc;
    }

    public int getAF() {
        return a << 8 | f.getByte();
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
