package fill;

import rasterize.Raster;

public class SeedFiller implements Filler {

    private final Raster raster;
    private final int x, y;
    private final int backgroundColor;

    public SeedFiller(Raster raster, int x, int y, int backgroundColor) {
        this.raster = raster;
        this.x = x;
        this.y = y;
        this.backgroundColor = backgroundColor;
    }

    @Override
    public void fill() {
        seedFill(x, y);
    }

    private void seedFill(int x, int y) {
        int[] colors = new int[]{0xff0000, 0xfffff};
        int colorSetter;

        int pixelColor = raster.getPixel(x, y);

        if (pixelColor != backgroundColor)
            return;

        if (x % 8 == 0)
            colorSetter = 0;
        else
            colorSetter = 1;

        raster.setPixel(x, y, colors[colorSetter]);

        seedFill(x, y - 1);
        seedFill(x, y + 1);
        seedFill(x + 1, y);
        seedFill(x - 1, y);
    }


}
