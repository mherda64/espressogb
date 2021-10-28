package memory;

public interface AddressSpace {
    void set(int address, int value);
    int get(int address);
}
