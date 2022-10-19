package model;

import java.util.ArrayList;
import java.util.List;

public class Polygon {

    private final List<Point> points;

    public Polygon() {
        this.points = new ArrayList<>();
    }

    public void addPoint(Point p) {
        points.add(p);
    }

    public Point getPoint(int index) {
        return points.get(index);
    }

    public int getCount() {
        return points.size();
    }

}
