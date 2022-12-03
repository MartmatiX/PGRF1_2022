package solids;

import transforms.Point3D;

public class Cube extends Solid {

    public Cube() {
        // Geometrie
        vb.add(new Point3D(3, 1, 1));
        vb.add(new Point3D(3, 3, 1));
        vb.add(new Point3D(1, 3, 1));
        vb.add(new Point3D(1, 1, 1));
        vb.add(new Point3D(3, 1, 3));
        vb.add(new Point3D(3, 3, 3));
        vb.add(new Point3D(1, 3, 3));
        vb.add(new Point3D(1, 1, 3));

        // Topologie
        addIndices(0, 1, 0, 4, 1, 2, 1, 5, 2, 3, 2, 6, 3, 0, 3, 7, 4, 5, 5, 6, 6, 7, 7, 4);
    }

}
