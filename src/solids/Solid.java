package solids;

import transforms.Mat4;
import transforms.Mat4Identity;
import transforms.Point3D;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class Solid {
    protected List<Point3D> vb = new ArrayList<>();
    protected List<Integer> ib = new ArrayList<>();
    protected List<Integer> colors = new ArrayList<>();
    protected Mat4 transMat = new Mat4Identity();
    protected boolean transferable = true;

    private Mat4 model = new Mat4Identity();

    public List<Point3D> getVb() {
        return vb;
    }

    public List<Integer> getIb() {
        return ib;
    }

    public Mat4 getModel() {
        return model;
    }

    public void setModel(Mat4 model) {
        this.model = model;
    }

    protected void addIndices(Integer... indices) {
        ib.addAll(Arrays.asList(indices));
    }

    public int getColor(int index) {
        if (this.colors != null) {
            return this.colors.get(index);
        }
        return 0x000000;
    }

    public int getColorSize() {
        return this.colors.size();
    }

    public void addColors(Integer... colors) {
        this.colors.addAll(Arrays.asList(colors));
    }

    public Mat4 getTransMat() {
        return transMat;
    }

    public boolean isTransferable() {
        return transferable;
    }

}
