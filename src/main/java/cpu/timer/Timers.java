package cpu.timer;

import cpu.interrupt.InterruptEnum;
import cpu.interrupt.InterruptRegs;
import memory.MMU;

public class Timers {

    private MMU mmu;
    private long lastCycles;

    private long mainCounter;
    private long subCounter;
    private long divCounter;

    public Timers(MMU mmu, long inputCycles) {
        this.mmu = mmu;
        this.lastCycles = inputCycles;
    }

    public void step(long cycles) {
        var diffCycles = cycles - lastCycles;
        lastCycles = cycles;
        subCounter += diffCycles;

        while (subCounter >= 4) {
            mainCounter++;
            subCounter -= 4;

            // Div increments at 1/16 base timer
            divCounter++;
            if (divCounter == 16) {
                mmu.set(TimerRegs.DIV.address, (mmu.get(TimerRegs.DIV.address) + 1) & 0xFF);
                divCounter = 0;
            }
        }

        if (isTimerOn()) {
            var threshold = getThreshold();
            if (mainCounter >= threshold) incTimer();
        }
    }

    private void incTimer() {
        mainCounter = 0;
        var tima = mmu.get(TimerRegs.TIMA.address);

        if (tima + 1 > 0xFF) {
            // Overflow interrupt
            mmu.set(TimerRegs.TIMA.address, mmu.get(TimerRegs.TMA.address));

            mmu.set(InterruptRegs.IF.getAddress(), mmu.get(InterruptRegs.IF.getAddress()) | InterruptEnum.TIMER.get());
        } else {
            mmu.set(TimerRegs.TIMA.address, tima + 1);
        }
    }

    private boolean isTimerOn() {
        return (mmu.get(TimerRegs.TAC.address) & 0x04) > 0;
    }

    private int getThreshold() {
        var speed = mmu.get(TimerRegs.TAC.address) & 0x03;
        switch (speed) {
            case 0x0: return 64;
            case 0x1: return 1;
            case 0x2: return 4;
            case 0x3: return 16;
            default: throw new IllegalStateException(String.format("Timer speed reg [%d] not in range 0-3", speed));
        }
    }

}
