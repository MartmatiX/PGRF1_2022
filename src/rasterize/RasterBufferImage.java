package rasterize;

import java.awt.*;
import java.awt.image.BufferedImage;

public class RasterBufferImage implements Raster {

    private final BufferedImage img;

    public RasterBufferImage(int width, int height) {
        img = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
    }

    @Override
    public void setPixel(int x, int y, int color) {
        if (x > img.getWidth() - 1 || x < 0 || y > img.getHeight() - 1 || y < 0)
            return;
        img.setRGB(x, y, color);
    }

    @Override
    public int getPixel(int x, int y) {
        return img.getRGB(x, y);
    }

    public void present(Graphics g) {
        g.drawImage(img, 0, 0, null);
    }

    public void clear() {
        Graphics gr = img.getGraphics();
        gr.setColor(Color.black);
        gr.fillRect(0, 0, img.getWidth(), img.getHeight());
    }

    public BufferedImage getImg() {
        return img;
    }
}
