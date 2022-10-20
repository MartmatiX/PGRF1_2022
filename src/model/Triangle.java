package model;

import rasterize.LineRasterizer;

import java.util.ArrayList;
import java.util.List;

public class Triangle {

    private final List<Point> points = new ArrayList<>();

    public void addPoint(Point p) {
        points.add(p);
    }

    public int getSize() {
        return points.size();
    }

    public void drawTriangle(LineRasterizer lineRasterizer) {
        lineRasterizer.drawLine(points.get(0).getX(), points.get(0).getY(), points.get(1).getX(), points.get(1).getY());

        double dx = points.get(0).getX() - points.get(1).getX();
        double dy = points.get(0).getY() - points.get(1).getY();

        double cx = points.get(0).getX() + dx * -0.5;
        double cy = points.get(0).getY() + dy * -0.5;

        double bs = (double) (points.get(1).getY() - points.get(0).getY()) / (points.get(1).getX() - points.get(0).getX());
        double par = points.get(points.size() - 1).getY() - bs * points.get(points.size() - 1).getX();
        double k = -1.0 / bs;
        double q = cy - k * cx;

        double x = (q - par) / (bs - k);
        double y = bs * x + par;

        lineRasterizer.drawLine(points.get(0).getX(), points.get(0).getY(), (int) Math.round(x), (int) Math.round(y));
        lineRasterizer.drawLine(points.get(1).getX(), points.get(1).getY(), (int) Math.round(x), (int) Math.round(y));
    }

}
