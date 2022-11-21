package input;

import cpu.interrupt.InterruptEnum;
import cpu.interrupt.InterruptRegs;
import memory.AddressSpace;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.List;

public class InputManager implements KeyListener {

    private int inputColumn;

    private int leftColumn = 0xF;
    private int rightColumn = 0xF;

    private final List<Integer> ALLOWED_KEYS = List.of(
            KeyEvent.VK_DOWN,
            KeyEvent.VK_UP,
            KeyEvent.VK_LEFT,
            KeyEvent.VK_RIGHT,
            KeyEvent.VK_ENTER,
            KeyEvent.VK_SPACE,
            KeyEvent.VK_Z,
            KeyEvent.VK_X
    );

    private AddressSpace addressSpace;

    public void setAddressSpace(AddressSpace addressSpace) {
        this.addressSpace = addressSpace;
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (ALLOWED_KEYS.contains(e.getKeyCode())) {
            switch (e.getKeyCode()) {
                case KeyEvent.VK_DOWN:
                    leftColumn &= 0x7;
                    break;
                case KeyEvent.VK_UP:
                    leftColumn &= 0xB;
                    break;
                case KeyEvent.VK_LEFT:
                    leftColumn &= 0xD;
                    break;
                case KeyEvent.VK_RIGHT:
                    leftColumn &= 0xE;
                    break;
                case KeyEvent.VK_ENTER:
                    rightColumn &= 0x7;
                    break;
                case KeyEvent.VK_SPACE:
                    rightColumn &= 0xB;
                    break;
                case KeyEvent.VK_Z:
                    rightColumn &= 0xD;
                    break;
                case KeyEvent.VK_X:
                    rightColumn &= 0xE;
                    break;
            }
            addressSpace.set(InterruptRegs.IF.getAddress(), addressSpace.get(InterruptRegs.IF.getAddress()) | InterruptEnum.JOYPAD.get());
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_DOWN:
                leftColumn |= 0x8;
                break;
            case KeyEvent.VK_UP:
                leftColumn |= 0x4;
                break;
            case KeyEvent.VK_LEFT:
                leftColumn |= 0x2;
                break;
            case KeyEvent.VK_RIGHT:
                leftColumn |= 0x1;
                break;
            case KeyEvent.VK_ENTER:
                rightColumn |= 0x8;
                break;
            case KeyEvent.VK_SPACE:
                rightColumn |= 0x4;
                break;
            case KeyEvent.VK_Z:
                rightColumn |= 0x2;
                break;
            case KeyEvent.VK_X:
                rightColumn |= 0x1;
                break;
        }
    }

    public void setInputColumn(int column) {
        inputColumn = column;
    }

    public int getKeys() {
        if (inputColumn == 0x10) return rightColumn;
        else if (inputColumn == 0x20) return leftColumn;

        return 0;
    }
}
