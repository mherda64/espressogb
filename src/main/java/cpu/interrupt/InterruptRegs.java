package cpu.interrupt;

public enum InterruptRegs {

    IE(0xFFFF),
    IF(0xFF0F);

    private int address;

    InterruptRegs(int address) {
        this.address = address;
    }

    public int getAddress() {
        return address;
    }
}