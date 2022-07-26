package memory.regions;

public class SwitchableROM extends BaseMemory {
    public SwitchableROM(int firstAddress) {
        // 16KiB
        super(0x4000, firstAddress);
    }

}
