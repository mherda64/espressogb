package cpu;

public class InterruptManager {
    boolean isHalted = false;
    boolean isStopped = false;

    boolean interruptsEnabled = true;
    boolean shouldDisableInterrupts = false;

    public boolean isHalted() {
        return isHalted;
    }

    public void setHalted(boolean halted) {
        isHalted = halted;
    }

    public boolean isStopped() {
        return isStopped;
    }

    public void setStopped(boolean stopped) {
        isStopped = stopped;
    }

    public boolean isInterruptsEnabled() {
        return interruptsEnabled;
    }

    public void setInterruptsEnabled(boolean interruptsEnabled) {
        this.interruptsEnabled = interruptsEnabled;
    }

    public boolean isShouldDisableInterrupts() {
        return shouldDisableInterrupts;
    }

    public void setShouldDisableInterrupts(boolean shouldDisableInterrupts) {
        this.shouldDisableInterrupts = shouldDisableInterrupts;
    }
}
