package model;

import java.util.ArrayList;
import java.util.List;

public class Polygon {

    private final List<Point> points = new ArrayList<>();

    public void addPoint(Point p) {
        points.add(p);
    }

    public List<Point> getPoints() {
        return points;
    }

    public int getCount() {
        return points.size();
    }

    public Point getPoint(int index){
        return points.get(index);
    }

}
