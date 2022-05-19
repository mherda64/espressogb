package memory.regions;

public class SwitchableROM extends BaseMemory {
    public SwitchableROM() {
        // 16KiB
        super(0x4000, 0x4000);
    }

}
