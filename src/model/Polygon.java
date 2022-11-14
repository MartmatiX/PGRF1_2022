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

    public List<Edge> getEdges(){
        List<Edge> edges = new ArrayList<>();
        for (int i = 0; i < points.size(); i++) {
            int nextIndex = (i + 1) % points.size();
            Point p1 = points.get(i);
            Point p2 = points.get(nextIndex);
            Edge edge = new Edge(p1.getX(), p1.getY(), p2.getX(), p2.getY());
            if (edge.isHorizontal())
                continue;

            // pridam hranu do seznamu
            edge.orientate();
            edges.add(edge);
        }
        return edges;
    }

    public int getCount() {
        return points.size();
    }

    public Point getPoint(int index){
        return points.get(index);
    }

}
