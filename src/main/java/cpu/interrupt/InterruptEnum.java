package cpu.interrupt;

public enum InterruptEnum {

    VBLANK(0x01),
    LCD_STAT(0x02),
    TIMER(0x04),
    SERIAL(0x08),
    JOYPAD(0x10);

    private int number;

    InterruptEnum(int number) {
        this.number = number;
    }

    public int get() {
        return number;
    }
}
