package ppu;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class Display extends JPanel implements Runnable {

    private boolean doRefresh = false;

    public int DISPLAY_WIDTH = 160;

    public int DISPLAY_HEIGHT = 144;

    private final BufferedImage img;

    protected final int scale;

    protected final int[][] rgb;

    private final int[] singleDimensionRGB;

    public Display(int scale) {
        this(160, 144, scale);
    }

    public Display(int width, int height, int scale) {
        this.DISPLAY_WIDTH = width;
        this.DISPLAY_HEIGHT = height;
        this.scale = scale;
        this.img = GraphicsEnvironment
                .getLocalGraphicsEnvironment()
                .getDefaultScreenDevice()
                .getDefaultConfiguration()
                .createCompatibleImage(DISPLAY_WIDTH * scale, DISPLAY_HEIGHT * scale);
        rgb = new int[DISPLAY_HEIGHT * scale][DISPLAY_WIDTH * scale];
        singleDimensionRGB = new int[DISPLAY_WIDTH * scale * DISPLAY_HEIGHT * scale];
    }

    public void requestRefresh() {
        this.doRefresh = true;
    }

    public void clearScreen() {
        for (int x = 0; x < 144; x++) {
            for (int y = 0; y < 160; y++)
                setPixel(x, y, 0);
        }
    }

    public void setPixel(int x, int y, int value) {
        for (int i = 0; i < scale; i++) {
            for (int j = 0; j < scale; j++) {
                rgb[x * scale + i][y * scale + j] = value;
            }
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        var g2d = (Graphics2D) g.create();
        g2d.drawImage(img, 0, 0, DISPLAY_WIDTH * scale, DISPLAY_HEIGHT * scale, null);
        g2d.dispose();
    }

    @Override
    public void run() {
        while (true) {
            synchronized (this) {
                try {
                    wait(1);
                } catch (InterruptedException e) {
                    break;
                }
            }

            if (doRefresh) {
                doRefresh = false;

                for (int i = 0; i < rgb.length; i++) {
                    for (int j = 0; j < rgb[i].length; j++) {
                        singleDimensionRGB[i * DISPLAY_WIDTH * scale + j] = rgb[i][j];
                    }
                }

                img.setRGB(0, 0, DISPLAY_WIDTH * scale, DISPLAY_HEIGHT * scale, singleDimensionRGB, 0, DISPLAY_WIDTH * scale);

                validate();
                repaint();
            }
        }
    }
}
