package solids;

import transforms.Point3D;

public class AxisRGB extends Solid {

    public AxisRGB() {
        transferable = false;

        vb.add(new Point3D(0, 0, 0));
        vb.add(new Point3D(1, 0, 0));
        vb.add(new Point3D(0, 1, 0));
        vb.add(new Point3D(0, 0, 1));

        addIndices(0, 1, 0, 2, 0, 3);

        addColors(0xff0000, 0x00ff00, 0x0000ff);
    }

}
