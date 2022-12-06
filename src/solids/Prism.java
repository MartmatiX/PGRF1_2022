package solids;

import transforms.Point3D;

public class Prism extends Solid {

    public Prism() {
        vb.add(new Point3D(0, 0, 4));
        vb.add(new Point3D(-2, -2, -1));
        vb.add(new Point3D(-2, 2, -1));
        vb.add(new Point3D(2, 2, -1));
        vb.add(new Point3D(2, -2, -1));

        addIndices(1, 2, 2, 3, 3, 4, 1, 4, 0, 1, 0, 2, 0, 3, 0, 4);

        addColors(0x00ff00);
    }

}
