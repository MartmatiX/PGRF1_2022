package solids;

import transforms.Point3D;

public class Octahedron extends Solid {

    public Octahedron() {
        vb.add(new Point3D(1, 0, 0));
        vb.add(new Point3D(-1, 0, 0));
        vb.add(new Point3D(0, 1, 0));
        vb.add(new Point3D(0, -1, 0));
        vb.add(new Point3D(0, 0, 1));
        vb.add(new Point3D(0, 0, -1));

        addIndices(0, 2, 2, 1, 1, 3, 3, 0, 0, 4, 1, 4, 2, 4, 3, 4, 0, 5, 1, 5, 2, 5, 3, 5);
        addColors(0xff0000);
    }

}
