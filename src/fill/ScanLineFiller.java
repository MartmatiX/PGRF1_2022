package fill;

import model.Edge;
import model.Point;
import model.Polygon;
import rasterize.LineRasterizer;
import rasterize.PolygonDrawer;
import rasterize.Raster;

import java.util.ArrayList;
import java.util.List;

public class ScanLineFiller implements Filler {

    private final LineRasterizer lineRasterizer;
    private final PolygonDrawer polygonDrawer;
    private final Polygon polygon;
    private final Raster raster;

    public ScanLineFiller(LineRasterizer lineRasterizer, PolygonDrawer polygonDrawer, Polygon polygon, Raster raster) {
        this.lineRasterizer = lineRasterizer;
        this.polygonDrawer = polygonDrawer;
        this.polygon = polygon;
        this.raster = raster;
    }

    @Override
    public void fill() {
        scanLine();
    }

    private void scanLine() {
        int[] colors = new int[]{0xff0000, 0xfffff};
        int colorSetter;
        // init seznamu hran
        List<Edge> edges = new ArrayList<>();

        // projdu pointy a vytvorim z nich hrany
        for (int i = 0; i < polygon.getCount(); i++) {
            int nextIndex = (i + 1) % polygon.getCount();
            Point p1 = polygon.getPoint(i);
            Point p2 = polygon.getPoint(nextIndex);
            Edge edge = new Edge(p1.getX(), p1.getY(), p2.getX(), p2.getY());
            if (edge.isHorizontal())
                continue;

            // pridam hranu do seznamu
            edge.orientate();
            edges.add(edge);
        }

        // najit yMin, yMax
        int yMin = polygon.getPoint(0).getY();
        int yMax = yMin;
        for (Point p : polygon.getPoints()) {
            if (p.getY() < yMin)
                yMin = p.getY();
            if (p.getY() > yMax)
                yMax = p.getY();
        }

        // pro Y od yMin do yMax
        for (int y = yMin; y <= yMax; y++) {
            // seznam pruseciku
            List<Integer> intersections = new ArrayList<>();

            for (Edge e : edges) {
                if (e.isIntersection(y))
                    intersections.add(e.getIntersection(y));
            }

            sort(intersections);

            for (int i = 0; i < intersections.size() - 1; ) {
                int start = intersections.get(i++);
                int end = intersections.get(i++ % intersections.size());
                for (int j = start; j < end; j++) {
                    int color = j % 8;
                    if (color == 0)
                        colorSetter = 0;
                    else
                        colorSetter = 1;
                    raster.setPixel(j, y, colors[colorSetter]);
                }
            }
        }
        polygonDrawer.drawPolygon(lineRasterizer, polygon);
    }

    public void sort(List<Integer> list) { // Selection sort
        for (int i = 0; i < list.size(); i++){
            int min = list.get(i);
            int minId = i;
            for (int j = i + 1; j < list.size(); j++){
                if (list.get(j) < min){
                    min = list.get(j);
                    minId = j;
                }
            }
            int temp = list.get(i);
            list.set(i, min);
            list.set(minId, temp);
        }
    }

}
