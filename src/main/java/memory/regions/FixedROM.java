package memory.regions;

public class FixedROM extends BaseMemory {

    public FixedROM() {
        // 16KiB
        super(0x4000, 0x0);
    }

}
