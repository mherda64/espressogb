package ppu;

public enum GPURegs {

    LDCD(0xFF40),
    STAT(0xFF41),
    SCY(0xFF42),
    SCX(0xFF43),
    LY(0xFF44),
    LYC(0xFF45),
    BGP(0xFF47),
    OBJ_PAL_1(0xFF48),
    OBJ_PAL_2(0xFF49),
    WY(0xFF4A),
    WX(0xFF4B);

    int address;

    GPURegs(int address) {
        this.address = address;
    }
}
