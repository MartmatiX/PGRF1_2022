package solids;

import transforms.Point3D;

public class Cube extends Solid {

    public Cube() {

        // Geometrie
        vb.add(new Point3D(-2, -2, -2));
        vb.add(new Point3D(-2, 2, -2));
        vb.add(new Point3D(2, 2, -2));
        vb.add(new Point3D(2, -2, -2));
        vb.add(new Point3D(-2, -2, 2));
        vb.add(new Point3D(-2, 2, 2));
        vb.add(new Point3D(2, 2, 2));
        vb.add(new Point3D(2, -2, 2));

        // Topologie
        addIndices(0, 1, 0, 4, 1, 2, 1, 5, 2, 3, 2, 6, 3, 0, 3, 7, 4, 5, 5, 6, 6, 7, 7, 4);
        addColors(0x0000ff);
    }

}
