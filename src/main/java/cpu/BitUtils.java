package cpu;

public class BitUtils {

    private BitUtils() {}

    public static boolean isByte(int value) {
        if (value < 0x0 || value > 0xFF) {
            throw new IllegalArgumentException("Byte value of the argument exceeded!");
        }
        return true;
    }

    public static boolean isShort(int value) {
        if (value < 0x0 || value > 0xFFFF) {
            throw new IllegalArgumentException("Short value of the argument exceeded!");
        }
        return true;
    }

    public static boolean getByteBit(int value, int bit) {
        if (bit > 7 || bit < 0) throw new IllegalArgumentException("Bit out of range 0-7");
        isByte(value);
        return ((value >>> bit) & 0x1) == 1;
    }

    public static boolean getShortBit(int value, int bit) {
        if (bit > 15 || bit < 0) throw new IllegalArgumentException("Bit out of range 0-15");
        isShort(value);
        return ((value >>> bit) & 0x1) == 1;
    }

    public static int setByteBit(int number, int bit, boolean value) {
        if (bit > 7 || bit < 0) throw new IllegalArgumentException("Bit out of range 0-7");
        isByte(number);
        return value ? number | (1 << bit) : number & ~(1 << bit);
    }

    public static int setShortBit(int number, int bit, boolean value) {
        if (bit > 15 || bit < 0) throw new IllegalArgumentException("Bit out of range 0-15");
        isShort(number);
        return value ? number | (1 << bit) : number & ~(1 << bit);
    }

    public static int getHighByte(int value) {
        isShort(value);
        return (value >>> 8) & 0xFF;
    }

    public static int getLowByte(int value) {
        isShort(value);
        return value & 0xFF;
    }

}
