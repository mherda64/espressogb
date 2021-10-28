package cpu;

import java.util.HashMap;
import java.util.Map;

public class Instructions {

    public Map<Integer, Instruction> instructions;

    public Instructions() {
        this.instructions = new HashMap<>();

        /*
        * 8-Bit Load Instructions
        * */
        instructions.put(0x06,
                Instruction.builder().loadByte(RegEnum.B).build()
                );

    }
}
