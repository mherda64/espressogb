package memory.regions;


public class ExternalRAM extends BaseMemory {
    public ExternalRAM() {
        // 8KiB
        super(0x2000, 0xA000);
    }
}
