package ppu;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class TileDisplay extends JPanel implements Runnable {

    public static final int DISPLAY_WIDTH = 128;

    public static final int DISPLAY_HEIGHT = 192;

    public static final int SCALE = 1;

    private final Tiles tiles;

    private final BufferedImage img;

    private final int[][] rgb;

    public final int[] PALETTE = {
            getColor(255, 255, 255),
            getColor(192, 192, 192),
            getColor(120, 120, 120),
            0
    };

    private int getColor(int r, int g, int b) {
        return 0xFF | r << 16 | g << 8 | b;
    }

    public TileDisplay(Tiles tiles) {
        this.tiles = tiles;
        this.img = GraphicsEnvironment
                .getLocalGraphicsEnvironment()
                .getDefaultScreenDevice()
                .getDefaultConfiguration()
                .createCompatibleImage(DISPLAY_WIDTH * SCALE, DISPLAY_HEIGHT * SCALE);
        rgb = new int[DISPLAY_HEIGHT * SCALE][DISPLAY_WIDTH * SCALE];
    }

    public void updateTileMap() {
        var x = 0;
        var y = 0;
        for (int i = 0; i < 384; i++) {
            drawTile(x * 8 * SCALE, y * 8 * SCALE, tiles.getTileMap()[i]);
            x += 1;
            if (x >= 16) {
                x = 0;
                y += 1;
            }
        }
    }

    private void drawTile(int baseX, int baseY, int[][] tile) {
        for (int i = 0; i < 8 * SCALE; i++) {
            for (int j = 0; j < 8 * SCALE; j++) {
                rgb[baseY + i][baseX + j] = PALETTE[tile[i / SCALE][j / SCALE]];
            }
        }
    }

//    private void setPixel(int pixel, int value) {
//        rgb[pixel] = value;
//    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        var g2d = (Graphics2D) g.create();
        g2d.drawImage(img, 0, 0, DISPLAY_WIDTH * SCALE, DISPLAY_HEIGHT * SCALE, null);
        g2d.dispose();
    }

    @Override
    public void run() {
        while (true) {
            var singleDimensionRGB = new int[DISPLAY_WIDTH * SCALE * DISPLAY_HEIGHT * SCALE];

            for (int i = 0; i < rgb.length; i++) {
                for (int j = 0; j < rgb[i].length; j++) {
                    singleDimensionRGB[i * DISPLAY_WIDTH * SCALE + j] = rgb[i][j];
                }
            }

            img.setRGB(0, 0, DISPLAY_WIDTH * SCALE, DISPLAY_HEIGHT * SCALE, singleDimensionRGB, 0, DISPLAY_WIDTH * SCALE);

            validate();
            repaint();
        }
    }
}