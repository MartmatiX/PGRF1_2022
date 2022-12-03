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
    private final Mat4 proj;

    public WireRenderer(FilledLineRasterizer lineRasterizer, BufferedImage img, Mat4 view, Mat4 proj){
        this.lineRasterizer = lineRasterizer;
        this.img = img;
        this.view = view;
        this.proj = proj;
    }

    private Boolean cut(Point3D point){
        return -point.getW() <= point.getY() && -point.getW() <= point.getX() && point.getY() <= point.getW() && point.getX() <= point.getW() && point.getZ() >= 0 && point.getZ() <= point.getW();
    }

    public void renderSolid(Solid solid, Mat4 model) {
        Mat4 model1;
        if (solid.isTransferable()) {
            model1 = solid.getTransMat().mul(model);
        } else {
            model1 = new Mat4Identity();
        }

        final Mat4 finalTransform = model1.mul(view).mul(proj);

        for (int i = 0; i < solid.getIb().size(); i = i + 2) {
            int index1 = solid.getIb().get(i);
            int index2 = solid.getIb().get(i + 1);

            Point3D point1 = solid.getVb().get(index1);
            Point3D point2 = solid.getVb().get(index2);

            point1 = point1.mul(finalTransform);
            point2 = point2.mul(finalTransform);

            Vec3D vectorA = null;
            Vec3D vectorB = null;

            if (cut(point1) && cut(point2)){
                if (point1.dehomog().isPresent()) {
                    vectorA = point1.dehomog().get();
                }

                if (point1.dehomog().isPresent()) {
                    if (point2.dehomog().isPresent())
                        vectorB = point2.dehomog().get();
                }

                assert vectorA != null;
                int x1 = (int) ((1 + vectorA.getX()) * (img.getWidth() - 1) / 2);
                int y1 = (int) ((1 - vectorA.getY()) * (img.getHeight() - 1) / 2);

                assert vectorB != null;
                int x2 = (int) ((1 + vectorB.getX()) * (img.getWidth() - 1) / 2);
                int y2 = (int) ((1 - vectorB.getY()) * (img.getHeight() - 1) / 2);

                lineRasterizer.drawLine(x1, y1, x2, y2);
            }
        }
    }

    public void renderScene(List<Solid> solids, Mat4 model) {
        for (Solid solid : solids) {
            renderSolid(solid, model);
        }
    }

    public void setView(Mat4 view){
        this.view = view;
    }

}
