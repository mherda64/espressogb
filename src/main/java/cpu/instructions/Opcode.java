package cpu.instructions;

import java.util.Optional;

public class Opcode<K> {
    private int opcode;
    private Optional<K> target;
    private int cycles;

    public Opcode(int opcode, K target, int cycles) {
        this.opcode = opcode;
        this.target = Optional.of(target);
        this.cycles = cycles;
    }

    public Opcode(int opcode, int cycles) {
        this.opcode = opcode;
        this.target = Optional.empty();
        this.cycles = cycles;
    }

    public int getOpcode() {
        return opcode;
    }

    public Optional<K> getTarget() {
        return target;
    }

    public int getCycles() {
        return cycles;
    }
}
