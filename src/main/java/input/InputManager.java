package input;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class InputManager implements KeyListener {

    private int inputColumn;

    private int leftColumn = 0xF;
    private int rightColumn = 0xF;

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
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
        if (inputColumn == 0x10) return leftColumn;
        else if (inputColumn == 0x20) return rightColumn;

        return 0;
    }
}
