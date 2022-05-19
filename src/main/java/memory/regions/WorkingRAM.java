package memory.regions;

public class WorkingRAM extends BaseMemory {
    public WorkingRAM() {
        // 8KiB
        super(0x2000, 0xC000);
    }
}
