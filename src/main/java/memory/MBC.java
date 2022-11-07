package memory;

public enum MBC {
    NONE(1, 2),
    MBC1(1, 128),
    MBC1_RAM(4, 128);
//    MBC3(8, 128);

    private final int maxRamSize;
    private final int maxRomSize;

    MBC(int ramSize, int romSize) {
        this.maxRamSize = ramSize;
        this.maxRomSize = romSize;
    }

    public static MBC getMBC(int code) {
        switch (code) {
            case 0x00: return NONE;
            case 0x01: return MBC1;
            case 0x02: return MBC1_RAM;
            case 0x03: return MBC1_RAM;
//            case 0x13: return MBC3;
            default: throw new IllegalStateException(String.format("MBC for code: [0x%02X] not implemented!", code));
        }
    }

    public int getMaxRamSize() {
        return maxRamSize;
    }

    public int getMaxRomSize() {
        return maxRomSize;
    }
}
