package solids;

import transforms.Bicubic;
import transforms.Mat4;
import transforms.Point3D;

public class CubicTranslation extends Solid {

    public CubicTranslation(Mat4 type, int color) {
        Point3D[] points;
        points = new Point3D[]{new Point3D(1, 1, 0), new Point3D(1, 2, 0), new Point3D(1, 3, 2), new Point3D(1, 4, 0), new Point3D(2, 1, 0), new Point3D(2, 2, 3), new Point3D(2, 3, 0), new Point3D(2, 4, 0), new Point3D(3, 1, 4), new Point3D(3, 2, 0), new Point3D(3, 3, 0), new Point3D(3, 4, 0), new Point3D(4, 1, -5), new Point3D(4, 2, 0), new Point3D(4, 3, -7), new Point3D(4, 4, 0),};

        Bicubic bc = new Bicubic(type, points);
        int counter = 0;

        for (float i = 0; i < 1.0; i += 0.05) {
            counter++;
            for (float j = 0; j < 1.0; j += 0.05) {
                vb.add(bc.compute(i, j));
            }
        }

        for (int i = 0; i < counter; i++) {
            for (int j = 0; j < counter; j++) {
                if (j < counter - 1) {
                    ib.add((i * counter) + j);
                    ib.add((i * counter) + j + 1);
                }
                if (i < counter - 1) {
                    ib.add((i * counter) + j);
                    ib.add(((i + 1) * counter) + j);
                }
            }
        }
        addColors(color);
    }

}
