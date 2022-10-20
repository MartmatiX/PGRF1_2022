package rasterize;

import model.Point;
import model.Polygon;

import java.util.List;

public class PolygonDrawer {

    public void drawPolygon(LineRasterizer lineRasterizer, Polygon polygon) {
        List<Point> points = polygon.getPoints();
        for (int i = 1; i < points.size(); i++) {
            lineRasterizer.drawLine(points.get(i - 1).getX(), points.get(i - 1).getY(), points.get(i).getX(), points.get(i).getY());
        }
        lineRasterizer.drawLine(points.get(0).getX(), points.get(0).getY(), points.get(points.size() - 1).getX(), points.get(points.size() - 1).getY());
    }
}
