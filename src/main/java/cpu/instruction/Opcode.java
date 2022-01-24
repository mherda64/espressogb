package cpu.instruction;

import cpu.Context;

import java.util.Optional;
import java.util.function.Function;

public class Opcode<K> {
    private int opcode;
    private K target;
    private Function<Context, Integer> cyclesFun;

    public Opcode(int opcode, K target, int cycles) {
        this.opcode = opcode;
        this.target = target;
        this.cyclesFun = context -> cycles;
    }

    public Opcode(int opcode, K target, Function<Context,Integer> cyclesFun) {
        this.opcode = opcode;
        this.target = target;
        this.cyclesFun = cyclesFun;
    }

    public Opcode(int opcode, int cycles) {
        this.opcode = opcode;
        this.target = null;
        this.cyclesFun = context -> cycles;
    }

    public int getOpcode() {
        return opcode;
    }

    public K getTarget() {
        return target;
    }

    public Function<Context, Integer> getCyclesFun() {
        return cyclesFun;
    }
}
