package cpu;

public class Context {

    private int contextValue;

    private boolean shouldJump = false;

    public int get() {
        return contextValue;
    }

    public void set(int contextValue) {
        this.contextValue = contextValue;
    }

    public boolean shouldJump() {
        return shouldJump;
    }

    public void setShouldJump(boolean shouldJump) {
        this.shouldJump = shouldJump;
    }
}
