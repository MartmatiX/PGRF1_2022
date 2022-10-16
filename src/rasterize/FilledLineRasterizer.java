package rasterize;

public class FilledLineRasterizer extends LineRasterizer {

    public FilledLineRasterizer(Raster raster) {
        super(raster);
    }

    @Override
    protected void drawLine(int x1, int y1, int x2, int y2) {
        int dx = x2 - x1;
        int dy = y2 - y1;

        int d = dy - (dx / 2);
        int x = x1, y = y1;

        while (x < x2) {
            x++;

            if (d < 0)
                d = d + dy;

            else {
                d += (dy - dx);
                y++;
            }

            raster.setPixel(x, y, 0xfffff);
        }
    }
}
