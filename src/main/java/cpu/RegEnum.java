package cpu;

public enum RegEnum {
    A(RegEnum.SINGLE),
    F(RegEnum.SINGLE),
    B(RegEnum.SINGLE),
    C(RegEnum.SINGLE),
    D(RegEnum.SINGLE),
    E(RegEnum.SINGLE),
    H(RegEnum.SINGLE),
    L(RegEnum.SINGLE),
    SP(RegEnum.DOUBLE),
    PC(RegEnum.DOUBLE),
    AF(RegEnum.DOUBLE),
    BC(RegEnum.DOUBLE),
    DE(RegEnum.DOUBLE),
    HL(RegEnum.DOUBLE);

    public final int size;

    private RegEnum(int size) {
        this.size = size;
    }

    public static final int SINGLE = 1;
    public static final int DOUBLE = 2;

}
