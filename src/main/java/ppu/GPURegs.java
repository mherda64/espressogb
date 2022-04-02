package ppu;

public enum GPURegs {

    LDCD(0xFF40),
    SCY(0xFF42),
    SCX(0xFF43),
    LY(0xFF44),
    LYC(0xFF45),
    WY(0xFF4A),
    WX(0xFF4B),
    BGP(0xFF47);

    int address;

    GPURegs(int address) {
        this.address = address;
    }
}
