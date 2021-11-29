package cpu;

public class Context {

    private int contextValue;

    private boolean conditionTrue = false;

    public int get() {
        return contextValue;
    }

    public void set(int contextValue) {
        this.contextValue = contextValue;
    }

    public boolean isConditionTrue() {
        return conditionTrue;
    }

    public void setConditionTrue(boolean conditionTrue) {
        this.conditionTrue = conditionTrue;
    }
}
