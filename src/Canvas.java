import fill.Filler;
import fill.ScanLineFiller;
import fill.SeedFiller;
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
    private final LineRasterizer lineRasterizerBright;
    private final RasterBufferImage raster;
    private final DashedLine dashedLineRasterizer;
    private final PolygonDrawer polygonDrawer;

    private int x_start;
    private int y_start;

    private int flag = 1;
    private int lineFlag = 1;
    private int polygonFlag = 1;

    Triangle triangle = new Triangle();
    Polygon polygon = new Polygon();
    Polygon polygon2 = new Polygon();

    private final int startPointColor = 0xfffff;

    public Canvas(int width, int height) {
        JFrame frame = new JFrame();

        frame.setLayout(new BorderLayout());
        frame.setResizable(false);
        frame.setTitle("Task1 Malir");
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setSize(width, height);

        raster = new RasterBufferImage(width, height);
        lineRasterizer = new FilledLineRasterizer(raster, 0xff0000);
        lineRasterizerBright = new FilledLineRasterizer(raster, 0xfffff);
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
                "polygon mode: P<br/>" +
                "clear canvas: C<br/>" +
                "paint triangle/polygon: RMB<br/>" +
                "close application: ESC" +
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
                        cleaner();
                        triangle = new Triangle();
                        polygon = new Polygon();
                        System.out.println("Everything cleared and repainted.\n");
                    }

                    // swap mezi rezimy
                    case KeyEvent.VK_L -> {
                        flag = 1;
                        cleaner();
                        triangle = new Triangle();
                        System.out.println("You entered line mode.\n");
                    }
                    case KeyEvent.VK_T -> {
                        flag = 2;
                        cleaner();
                        triangle = new Triangle();
                        System.out.println("You entered triangle mode.\n");
                    }
                    case KeyEvent.VK_D -> {
                        flag = 3;
                        cleaner();
                        System.out.println("You entered dashed line mode.\n");
                    }
                    case KeyEvent.VK_P -> {
                        flag = 4;
                        cleaner();
                        polygon = new Polygon();
                        System.out.println("You entered polygon mode.\n");
                    }
                }

                // swap typu car
                if (flag == 3) {
                    switch (keyEvent.getKeyCode()) {
                        case KeyEvent.VK_1, KeyEvent.VK_NUMPAD1 -> {
                            lineFlag = 1;
                            System.out.println("You now draw dashed line.\n");
                        }
                        case KeyEvent.VK_2, KeyEvent.VK_NUMPAD2 -> {
                            lineFlag = 2;
                            System.out.println("You now draw dotted line.\n");
                        }
                    }
                }

                if (flag == 4) {
                    switch (keyEvent.getKeyCode()) {
                        case KeyEvent.VK_2, KeyEvent.VK_NUMPAD2 -> {
                            polygonFlag = 2;
                            System.out.println("You can draw second polygon.\n");
                        }
                        case KeyEvent.VK_1, KeyEvent.VK_NUMPAD1 -> {
                            polygonFlag = 1;
                            System.out.println("You can draw first polygon.\n");
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
                switch (e.getButton()) {
                    case MouseEvent.BUTTON1 -> {
                        if (flag == 4 && polygonFlag == 1) {
                            polygon.addPoint(new Point(e.getX(), e.getY()));
                            System.out.println("You added point.\n");
                            if (polygon.getCount() == 1) {
                                x_start = e.getX();
                                y_start = e.getY();
                            }
                            if (polygon.getCount() > 1) {
                                raster.clear();
                                polygonDrawer.drawPolygon(lineRasterizer, polygon);
                                if (polygon2.getCount() > 1)
                                    polygonDrawer.drawPolygon(lineRasterizerBright, polygon2);
                                panel.repaint();
                            }
                        }
                        if (flag == 4 && polygonFlag == 2) {
                            polygon2.addPoint(new Point(e.getX(), e.getY()));
                            System.out.println("You added point.\n");
                            if (polygon2.getCount() == 1) {
                                x_start = e.getX();
                                y_start = e.getY();
                            }
                            if (polygon2.getCount() > 1) {
                                raster.clear();
                                polygonDrawer.drawPolygon(lineRasterizerBright, polygon2);
                                if (polygon.getCount() > 1)
                                    polygonDrawer.drawPolygon(lineRasterizer, polygon);
                                panel.repaint();
                            }
                        }
                    }
                    case MouseEvent.BUTTON3 -> {
                        if (flag == 4 || flag == 2) {
                            Filler seedFiller = new SeedFiller(raster, e.getX(), e.getY(), 0xfffff, raster.getPixel(0, 0));
                            System.out.println("You added seed to location: [" + e.getX() + ", " + e.getY() + "].\n");
                            seedFiller.fill();
                            panel.repaint();
                        }
                    }
                    case MouseEvent.BUTTON2 -> {
                        if (flag == 4) {
                            if (polygon.getCount() > 2) {
                                Filler scanLineFiller = new ScanLineFiller(lineRasterizer, polygonDrawer, polygon, raster);
                                scanLineFiller.fill();
                                panel.repaint();
                            }
                            if (polygon2.getCount() > 2) {
                                Filler scanLineFiller = new ScanLineFiller(lineRasterizerBright, polygonDrawer, polygon2, raster);
                                scanLineFiller.fill();
                                panel.repaint();
                            }
                        }

                    }
                }

            }
        });

        panel.addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                super.mouseDragged(e);
                if (SwingUtilities.isLeftMouseButton(e)) {
                    switch (flag) {
                        case 1 -> {
                            raster.clear();
                            Line line = new Line(x_start, y_start, e.getX(), e.getY());
                            lineRasterizer.rasterize(line);
                            panel.repaint();
                        }
                        case 2 -> {
                            if (triangle.getSize() >= 2) {
                                Point p = new Point(e.getX(), e.getY());
                                triangle.addPoint(p);
                                raster.clear();
                                if (triangle.getSize() >= 3) {
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
                            Line line = new Line(x_start, y_start, e.getX(), e.getY());
                            dashedLineRasterizer.rasterize(line);
                            panel.repaint();
                        }
                        case 4 -> {
                            switch (polygonFlag) {
                                case 1 -> {
                                    raster.clear();
                                    Line line = new Line(x_start, y_start, e.getX(), e.getY());
                                    Line line2 = new Line(polygon.getPoints().get(polygon.getCount() - 1).getX(), polygon.getPoints().get(polygon.getCount() - 1).getY(), e.getX(), e.getY());
                                    lineRasterizer.rasterize(line2);
                                    lineRasterizer.rasterize(line);
                                    polygonDrawer.drawPolygon(lineRasterizer, polygon);
                                    panel.repaint();
                                }
                                case 2 -> {
                                    raster.clear();
                                    Line line = new Line(x_start, y_start, e.getX(), e.getY());
                                    Line line2 = new Line(polygon2.getPoints().get(polygon2.getCount() - 1).getX(), polygon2.getPoints().get(polygon2.getCount() - 1).getY(), e.getX(), e.getY());
                                    lineRasterizerBright.rasterize(line2);
                                    lineRasterizerBright.rasterize(line);
                                    polygonDrawer.drawPolygon(lineRasterizerBright, polygon2);
                                    panel.repaint();
                                }
                            }

                        }

                    }
                }
            }
        });
    }

    public void cleaner() {
        panel.repaint();
        raster.clear();
    }

}
