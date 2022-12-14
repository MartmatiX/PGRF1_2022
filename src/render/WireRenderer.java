package render;

import rasterize.FilledLineRasterizer;
import solids.Solid;
import transforms.Mat4;
import transforms.Mat4Identity;
import transforms.Point3D;
import transforms.Vec3D;

import java.awt.image.BufferedImage;
import java.util.List;

public class WireRenderer {

    private final FilledLineRasterizer lineRasterizer;
    private final BufferedImage img;
    private Mat4 view;
    private Mat4 projection;

    public WireRenderer(FilledLineRasterizer lineRasterizer, BufferedImage img, Mat4 projection) {
        this.lineRasterizer = lineRasterizer;
        this.img = img;
        this.projection = projection;
    }

    public void setView(Mat4 view) {
        this.view = view;
    }

    public void setProjection(Mat4 projection) {
        this.projection = projection;
    }


    public void renderSolid(Solid solid, Mat4 model) {
        if (solid.isTransferable()) {
            model = solid.getTransMat().mul(model);
        } else {
            model = new Mat4Identity();
        }

        final Mat4 finalTransform = model.mul(view).mul(projection);

        for (int i = 0; i < solid.getIb().size(); i += 2) {
            int index1 = solid.getIb().get(i);
            int index2 = solid.getIb().get(i + 1);

            Point3D point1 = solid.getVb().get(index1);
            Point3D point2 = solid.getVb().get(index2);

            point1 = point1.mul(finalTransform);
            point2 = point2.mul(finalTransform);

            if (point1.getW() < 0) break;
            if (point2.getW() < 0) break;

            Vec3D vectorA = null;
            Vec3D vectorB = null;

            if (point1.dehomog().isPresent()) {
                vectorA = point1.dehomog().get();
            }
            if (point2.dehomog().isPresent()) {
                vectorB = point2.dehomog().get();
            }

            assert vectorA != null;
            int x1 = (int) ((1 + vectorA.getX()) * (img.getWidth() - 1) / 2);
            int y1 = (int) ((1 - vectorA.getY()) * (img.getHeight() - 1) / 2);
            assert vectorB != null;
            int x2 = (int) ((1 + vectorB.getX()) * (img.getWidth() - 1) / 2);
            int y2 = (int) ((1 - vectorB.getY()) * (img.getHeight() - 1) / 2);

            if (x1 >= 0 && x1 < img.getWidth() && y1 >= 0 && y1 < img.getHeight() && x2 >= 0 && x2 < img.getWidth() && y2 >= 0 && y2 < img.getHeight()) {
                lineRasterizer.setColor(solid.getColor(i % solid.getColorSize()));
                lineRasterizer.drawLine(x1, y1, x2, y2);
            }
        }
    }

    public void renderScene(List<Solid> solids, Mat4 model) {
        for (Solid solid : solids) {
            renderSolid(solid, model);
        }
    }

}
