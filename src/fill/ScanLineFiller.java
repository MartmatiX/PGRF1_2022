package fill;

import model.Edge;
import model.Point;
import model.Polygon;
import rasterize.LineRasterizer;
import rasterize.PolygonDrawer;
import rasterize.Raster;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ScanLineFiller implements Filler{

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

    private void scanLine(){
        // init seznamu hran
        List<Edge> edges = new ArrayList<>();

        // projdu pointy a vytvorim z nich hrany
        for (int i = 0; i < polygon.getCount(); i++){
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
        for (Point p : polygon.getPoints()){
            if (p.getY() < yMin)
                yMin = p.getY();
            if (p.getY() > yMax)
                yMax = p.getY();
        }

        // pro Y od yMin do yMax
        for (int y = yMin; y <= yMax; y++){
            // seznam pruseciku
            List<Integer> intersections = new ArrayList<>();

            for (Edge e : edges){
                if (e.isIntersection(y))
                    intersections.add(e.getIntersection(y));
            }

            Collections.sort(intersections);
            //bubbleSort(intersections);

            for (int i = 0; i < intersections.size() - 1;){
                for(int j = intersections.get(i++); j < intersections.get(i++ % intersections.size()); j++){
                    raster.setPixel(j, y, 0x125ff);
                }
            }
        }
        polygonDrawer.drawPolygon(lineRasterizer, polygon);
    }

    public void bubbleSort(List<Integer> list){
        int temp;
        for (int i = 0; i < list.size(); i++){
            for (int j = 1; j < list.size() - 1; j++){
                if (list.get(j - 1) > list.get(j)){
                    temp = list.get(j - 1);
                    list.set(j - 1, list.get(j));
                    list.set(j, temp);
                }
            }
        }
    }

}
