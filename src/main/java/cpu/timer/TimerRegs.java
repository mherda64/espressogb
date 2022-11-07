package cpu.timer;

public enum TimerRegs {

    DIV(0xFF04),
    TIMA(0xFF05),
    TMA(0xFF06),
    TAC(0xFF07);

    int address;

    TimerRegs(int address) {
        this.address = address;
    }
}