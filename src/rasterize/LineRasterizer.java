package rasterize;

import model.Line;

public abstract class LineRasterizer {

    protected Raster raster;

    public LineRasterizer(Raster raster) {
        this.raster = raster;
    }

    public void rasterize(Line line) {
        drawLine(line.getX1(), line.getY1(), line.getX2(), line.getY2());
    }

    public void drawLine(int x1, int y1, int x2, int y2) {
    }

    public void setColor(int i) {

    }
}
