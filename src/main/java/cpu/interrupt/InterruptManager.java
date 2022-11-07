package cpu.interrupt;

import cpu.BitUtils;
import cpu.CPU;
import cpu.Registers;
import memory.AddressSpace;

public class InterruptManager {

    private final CPU cpu;
    private final Registers registers;
    private final AddressSpace addressSpace;

    public InterruptManager(CPU cpu, Registers registers, AddressSpace addressSpace) {
        this.cpu = cpu;
        this.registers = registers;
        this.addressSpace = addressSpace;
    }

    boolean isHalted = false;
    boolean isStopped = false;

    boolean interruptsEnabled = false;
    boolean shouldDisableInterrupts = false;

    public void handleInterrupts() {
        var intEnable = addressSpace.get(InterruptRegs.IE.getAddress());
        var intFlag = addressSpace.get(InterruptRegs.IF.getAddress());
        if (intEnable > 0 && intFlag > 0) {
            var enabled = intEnable & intFlag;

            if ((enabled & InterruptEnum.VBLANK.get()) > 0) {
                addressSpace.set(InterruptRegs.IF.getAddress(), intFlag & 0xFE);
                callInt(0x0040);
            } else if ((enabled & InterruptEnum.LCD_STAT.get()) > 0) {
                addressSpace.set(InterruptRegs.IF.getAddress(), intFlag & 0xFD);
                callInt(0x0048);
            } else if ((enabled & InterruptEnum.TIMER.get()) > 0) {
                addressSpace.set(InterruptRegs.IF.getAddress(), intFlag & 0xFB);
                callInt(0x0050);
            } else if ((enabled & InterruptEnum.SERIAL.get()) > 0) {
                addressSpace.set(InterruptRegs.IF.getAddress(), intFlag & 0xF7);
                callInt(0x0058);
            } else if ((enabled & InterruptEnum.JOYPAD.get()) > 0) {
                addressSpace.set(InterruptRegs.IF.getAddress(), intFlag & 0xEF);
                callInt(0x0060);
            }


        }
    }

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

    public void disableInterrupts() {
        this.interruptsEnabled = false;
    }

    public void enableInterrupts() {
        this.interruptsEnabled = false;
    }


    public void updateEnableInterruptsFlag() {
        this.interruptsEnabled = !shouldDisableInterrupts;
    }

    public boolean shouldDisableInterrupts() {
        return shouldDisableInterrupts;
    }

    public void setShouldDisableInterrupts(boolean shouldDisableInterrupts) {
        this.shouldDisableInterrupts = shouldDisableInterrupts;
    }

    private void callInt(int intVector) {
        disableInterrupts();

        var pc = registers.getPC();
        addressSpace.set(registers.decSP(), BitUtils.getHighByte(pc));
        addressSpace.set(registers.decSP(), BitUtils.getLowByte(pc));
        registers.setPC(intVector);

//        TODO: Find out how many cycles should interrupt call take
        cpu.addCycles(12);
    }

}
