package ppu;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class Display extends JPanel implements Runnable {

    public final int DISPLAY_WIDTH;

    public final int DISPLAY_HEIGHT;

    private final BufferedImage img;

    protected final int scale;

    protected final int[][] rgb;

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
        var singleDimensionRGB = new int[DISPLAY_WIDTH * scale * DISPLAY_HEIGHT * scale];
        while (true) {

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
