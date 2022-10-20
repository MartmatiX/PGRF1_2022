package rasterize;

import model.Line;

public class DashedLine extends LineRasterizer {

    public DashedLine(Raster raster) {
        super(raster);
    }

    @Override
    public void rasterize(Line line) {
        super.rasterize(line);
    }

    @Override
    public void drawLine(int x1, int y1, int x2, int y2) {
        int space = 0;
        int length = 0;

        int spaceLength = 5;
        int dashLength = 5;

        // trivialni algoritmus
        float k;

        // uhel
        if (x1 != x2) {
            k = (y2 - y1) / (float) (x2 - x1);
        } else {
            k = Integer.MAX_VALUE; // nerozbije se, pokud x1 == x2, ztraci se presnost
        }

        // y offset
        final float q = y1 - k * x1;

        if (Math.abs(y2 - y1) < Math.abs(x2 - x1)) { // vykresleni pomoci x
            if (x1 > x2) {
                int tmp = x1;
                x1 = x2;
                x2 = tmp;
            }

            for (int x = x1; x <= x2; x++) {
                if (space > spaceLength) {
                    length = 0;
                    space = 0;
                }
                if (length <= dashLength) { // urazena vzdalenost <= zadane maximalni vzdalenosti
                    float y = k * x + q;
                    raster.setPixel(x, Math.round(y), 0xff0000);
                    length++; // urazena vzdalenost
                } else {
                    space++;
                }
            }
        } else { // vykresleni pomoci y
            if (y1 > y2) {
                int tmp = y1;
                y1 = y2;
                y2 = tmp;
            }
            for (int y = y1; y <= y2; y++) {
                if (space > spaceLength) {
                    length = 0;
                    space = 0;
                }
                if (length <= dashLength) { // urazena vzdalenost <= zadane maximalni vzdalenosti
                    int x = Math.round((y - q) / k);
                    raster.setPixel(x, y, 0xff0000);
                    length++; // urazena vzdalenost
                } else {
                    space++; // pixely ktere se nevykresli
                }
            }
        }

    }
}
