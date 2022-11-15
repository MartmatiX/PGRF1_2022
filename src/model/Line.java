package model;

public class Line {

    private final int x1, y1;
    private final int x2, y2;

    public Line(int x1, int y1, int x2, int y2) {
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;
    }

    public int getX1() {
        return x1;
    }

    public int getY1() {
        return y1;
    }

    public int getX2() {
        return x2;
    }

    public int getY2() {
        return y2;
    }

    public boolean isInside(Point v) {
        int side = ((x2 - x1) * (v.getY() - y1) - (y2 - y1) * (v.getX() - x1));
        return side < 0;
    }

    public Point intersection(Point v1, Point v2) {
        // metoda pro vypocet pruseciku pomoci primky a dvou bodu
        double x0, y0;
        double x3 = v2.getX();
        double y3 = v2.getY();
        double x4 = v1.getX();
        double y4 = v1.getY();

        double v = (x1 - x2) * (y3 - y4) - (y1 - y2) * (x3 - x4);
        x0 = ((x1 * y2 - x2 * y1) * (x3 - x4) - (x3 * y4 - x4 * y3) * (x1 - x2)) / v;
        y0 = ((x1 * y2 - x2 * y1) * (y3 - y4) - (x3 * y4 - x4 * y3) * (y1 - y2)) / v;
        return new Point((int) Math.round(x0), (int) Math.round(y0));
    }

}
