package rasterize;

public class FilledLineRasterizer extends LineRasterizer {

    /*
        nazev: trivialni algoritmus
        vyhody: pracuje rychle, pokud plati predpoklad nezavislosti funkci, muze fungovat lepe nez jine algoritmy
        nevyhody: prirazuje nulovou hodnotu do promenne a proto se usecka prestane vykreslovat, pokud x1 == x2,
                  zaroven cim mensi je uhel, tim mene presneji se usecka vykresluje
     */


    public FilledLineRasterizer(Raster raster) {
        super(raster);
    }

    @Override
    public void drawLine(int x1, int y1, int x2, int y2) {
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
                float y = k * x + q;
                raster.setPixel(x, Math.round(y), 0xff0000);
            }
        } else { // vykresleni pomoci y
            if (y1 > y2) {
                int tmp = y1;
                y1 = y2;
                y2 = tmp;
            }
            for (int y = y1; y <= y2; y++) {
                int x = Math.round((y - q) / k);
                raster.setPixel(x, y, 0xff0000);
            }
        }
    }
}
