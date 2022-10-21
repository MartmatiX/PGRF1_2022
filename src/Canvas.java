import model.Line;
import model.Point;
import model.Triangle;
import model.Polygon;
import rasterize.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class Canvas {

    private final JPanel panel;

    private final LineRasterizer lineRasterizer;
    private final RasterBufferImage raster;
    private final DashedLine dashedLineRasterizer;
    private final PolygonDrawer polygonDrawer;

    private int x_start;
    private int y_start;

    private int flag = 1;
    private int lineFlag = 1;

    Triangle triangle = new Triangle();
    Polygon polygon = new Polygon();

    private final int startPointColor = 0xfffff;

    public Canvas(int width, int height) {
        JFrame frame = new JFrame();

        frame.setLayout(new BorderLayout());
        frame.setResizable(false);
        frame.setTitle("Task1 Malir");
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setSize(width, height);

        raster = new RasterBufferImage(width, height);
        lineRasterizer = new FilledLineRasterizer(raster);
        dashedLineRasterizer = new DashedLine(raster);
        polygonDrawer = new PolygonDrawer();

        panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics graphics) {
                super.paintComponent(graphics);
                raster.present(graphics);
            }
        };
        panel.setPreferredSize(new Dimension(width, height));

        frame.add(panel, BorderLayout.WEST);

        panel.setLayout(new FlowLayout(FlowLayout.LEFT, 15, 15));

        JLabel controls = new JLabel("<html>" +
                "line mode: L<br/>" +
                "triangle mode: T<br/>" +
                "dashed line mode: D<br/>" +
                "polygon mode: P.<br/>" +
                "clear canvas: C" +
                "</html>");
        controls.setForeground(new Color(255, 255, 255));
        panel.add(controls, BorderLayout.WEST);

        JLabel dashedLineSelect = new JLabel("<html>" +
                "dotted line: 1<br/>" +
                "dashed line: 2<br/>" +
                "</html>");
        dashedLineSelect.setForeground(new Color(255, 255, 255));
        panel.add(dashedLineSelect, BorderLayout.EAST);

        frame.pack();
        frame.setVisible(true);

        panel.requestFocus();
        panel.requestFocusInWindow();

        raster.clear();
        panel.repaint();

        panel.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent keyEvent) {
                super.keyPressed(keyEvent);

                switch (keyEvent.getKeyCode()) {
                    // ukonceni aplikace
                    case KeyEvent.VK_ESCAPE -> {
                        System.out.println("Goodbye.\n");
                        System.exit(1);
                    }

                    // vycisteni platna
                    case KeyEvent.VK_C -> {
                        raster.clear();
                        panel.repaint();
                        triangle = new Triangle();
                        polygon = new Polygon();
                        System.out.println("Everything cleared and repainted.\n");
                    }

                    // swap mezi rezimy
                    case KeyEvent.VK_L -> {
                        flag = 1;
                        raster.clear();
                        panel.repaint();
                        triangle = new Triangle();
                        System.out.println("You entered line mode.\n");
                    }
                    case KeyEvent.VK_T -> {
                        flag = 2;
                        raster.clear();
                        panel.repaint();
                        triangle = new Triangle();
                        System.out.println("You entered triangle mode.\n");
                    }
                    case KeyEvent.VK_D -> {
                        flag = 3;
                        raster.clear();
                        panel.repaint();
                        System.out.println("You entered dashed line mode.\n");
                    }
                    case KeyEvent.VK_P -> {
                        flag = 4;
                        raster.clear();
                        panel.repaint();
                        polygon = new Polygon();
                        System.out.println("You entered polygon mode.\n");
                    }
                }

                // swap typu car
                if (flag == 3) {
                    switch (keyEvent.getKeyCode()) {
                        case KeyEvent.VK_1, KeyEvent.VK_NUMPAD1 -> {
                            lineFlag = 1;
                            System.out.println("You now draw dashed line");
                        }
                        case KeyEvent.VK_2, KeyEvent.VK_NUMPAD2 -> {
                            lineFlag = 2;
                            System.out.println("You now draw dotted line");
                        }
                    }
                }
            }
        });

        panel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);

                switch (flag) {
                    case 1, 3 -> {
                        raster.clear();
                        x_start = e.getX();
                        y_start = e.getY();
                        raster.setPixel(e.getX(), e.getY(), startPointColor);
                        panel.repaint();
                        System.out.println("You selected new starting point.\n");
                    }
                    case 2 -> {
                        if (triangle.getSize() < 2) {
                            Point point = new Point(e.getX(), e.getY());
                            triangle.addPoint(point);
                            raster.setPixel(point.getX(), point.getY(), startPointColor);
                            panel.repaint();
                            System.out.println("Point: " + triangle.getSize() + " added.\n");
                        }
                    }
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                super.mouseReleased(e);
                if (flag == 4) {
                    polygon.addPoint(new Point(e.getX(), e.getY()));
                    System.out.println("You added point.\n");
                    if (polygon.getCount() == 1) {
                        x_start = e.getX();
                        y_start = e.getY();
                    }
                    if (polygon.getCount() > 1) {
                        raster.clear();
                        polygonDrawer.drawPolygon(lineRasterizer, polygon);
                        panel.repaint();
                    }
                }
            }
        });

        panel.addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                super.mouseDragged(e);

                switch (flag) {
                    case 1 -> {
                        raster.clear();
                        if (e.getX() >= width || e.getY() >=height || e.getX() < 0 || e.getY() < 0)
                            return;
                        Line line = new Line(x_start, y_start, e.getX(), e.getY());
                        lineRasterizer.rasterize(line);
                        panel.repaint();
                    }
                    case 2 -> {
                        if (triangle.getSize() >= 2) {
                            if (e.getX() >= width || e.getY() >=height || e.getX() < 0 || e.getY() < 0)
                                return;
                            Point p = new Point(e.getX(), e.getY());
                            triangle.addPoint(p);
                            raster.clear();
                            if (triangle.getSize() >= 3) {
                                if (e.getX() >= width || e.getY() >=height || e.getX() < 0 || e.getY() < 0)
                                    return;
                                triangle.drawTriangle(lineRasterizer);
                                panel.repaint();
                            }
                        }
                    }
                    case 3 -> {
                        raster.clear();
                        switch (lineFlag) {
                            case 1 -> {
                                dashedLineRasterizer.setDashLength(5);
                                dashedLineRasterizer.setSpaceLength(5);
                            }
                            case 2 -> {
                                dashedLineRasterizer.setDashLength(1);
                                dashedLineRasterizer.setSpaceLength(1);
                            }
                        }
                        if (e.getX() >= width || e.getY() >=height || e.getX() < 0 || e.getY() < 0)
                            return;
                        Line line = new Line(x_start, y_start, e.getX(), e.getY());
                        dashedLineRasterizer.rasterize(line);
                        panel.repaint();
                    }
                    case 4 -> {
                        raster.clear();
                        if (e.getX() >= width || e.getY() >=height || e.getX() < 0 || e.getY() < 0)
                            return;
                        Line line = new Line(x_start, y_start, e.getX(), e.getY());
                        Line line2 = new Line(polygon.getPoints().get(polygon.getCount() - 1).getX(), polygon.getPoints().get(polygon.getCount() - 1).getY(), e.getX(), e.getY());
                        lineRasterizer.rasterize(line2);
                        polygonDrawer.drawPolygon(lineRasterizer, polygon);
                        lineRasterizer.rasterize(line);
                        panel.repaint();
                    }
                }
            }
        });

    }

}
