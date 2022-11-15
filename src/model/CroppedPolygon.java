package model;

public class CroppedPolygon {

    Polygon cropper;

    public CroppedPolygon(Polygon polygon) {
        this.cropper = polygon;
    }

    public Polygon cropPolygon(Polygon polygon) {
        Polygon croppedPolygon = new Polygon(polygon);
        for (int i = 0; i < cropper.getCount(); i++) {
            Line cutter = new Line(cropper.getPoint(i).getX(), cropper.getPoint(i).getY(), cropper.getPoint((i + 1) % cropper.getCount()).getX(), cropper.getPoint((i + 1) % cropper.getCount()).getY());
            polygon = new Polygon(croppedPolygon);
            croppedPolygon.clearPolygon();
            Point v1 = new Point(polygon.getPoint(polygon.getCount() - 1).getX(), polygon.getPoint(polygon.getCount() - 1).getY());
            for (int j = 0; j < polygon.getCount(); j++) {
                Point v2 = new Point(polygon.getPoint(j).getX(), polygon.getPoint(j).getY());
                if (cutter.isInside(v2)) {
                    if (!cutter.isInside(v1))
                        croppedPolygon.addPoint(cutter.intersection(v1, v2));

                    croppedPolygon.addPoint(v2);
                } else {
                    if (cutter.isInside(v1))
                        croppedPolygon.addPoint(cutter.intersection(v1, v2));
                }
                v1 = v2;
            }

        }
        return croppedPolygon;
    }
}
