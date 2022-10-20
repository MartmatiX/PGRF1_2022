import model.Line;
import model.Point;
import model.Triangle;
import rasterize.DashedLine;
import rasterize.FilledLineRasterizer;
import rasterize.LineRasterizer;
import rasterize.RasterBufferImage;

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
    private final LineRasterizer dashedLineRasterizer;

    private int x_start;
    private int y_start;

    private int flag = 1;

    Triangle triangle = new Triangle();

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

        panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics graphics) {
                super.paintComponent(graphics);
                raster.present(graphics);
            }
        };
        panel.setPreferredSize(new Dimension(width, height));

        frame.add(panel, BorderLayout.CENTER);

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

                // ukoncit pri zmacknuti ESC
                if (keyEvent.getKeyCode() == KeyEvent.VK_ESCAPE) {
                    System.out.println("Goodbye.\n");
                    System.exit(1);
                }

                // vycistit vse pri C
                if (keyEvent.getKeyCode() == KeyEvent.VK_C) {
                    raster.clear();
                    panel.repaint();
                    triangle = new Triangle();
                    System.out.println("Everything cleared and repainted.\n");
                }

                // swap mezi rezimy
                if (keyEvent.getKeyCode() == KeyEvent.VK_L) {
                    flag = 1;
                    raster.clear();
                    panel.repaint();
                    triangle = new Triangle();
                    System.out.println("You entered line mode.\n");
                }
                if (keyEvent.getKeyCode() == KeyEvent.VK_T) {
                    flag = 2;
                    raster.clear();
                    panel.repaint();
                    triangle = new Triangle();
                    System.out.println("You entered triangle mode.\n");
                }
                if (keyEvent.getKeyCode() == KeyEvent.VK_D) {
                    flag = 3;
                    raster.clear();
                    panel.repaint();
                    triangle = new Triangle();
                    System.out.println("You entered dashed line mode.\n");
                }
            }
        });

        panel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);

                switch (flag) {
                    case 1 -> {
                        raster.clear();
                        x_start = e.getX();
                        y_start = e.getY();
                        raster.setPixel(e.getX(), e.getY(), 0xfffff);
                        panel.repaint();
                        System.out.println("You selected new starting point.\n");
                    }
                    case 2 -> {
                        if (triangle.getSize() < 2) {
                            Point point = new Point(e.getX(), e.getY());
                            triangle.addPoint(point);
                            raster.setPixel(point.getX(), point.getY(), 0xfffff);
                            panel.repaint();
                            System.out.println("Point: " + triangle.getSize() + " added.\n");
                        }
                    }
                    case 3 -> {
                        raster.clear();
                        x_start = e.getX();
                        y_start = e.getY();
                        raster.setPixel(e.getX(), e.getY(), 0xfffff);
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
                        Line line = new Line(x_start, y_start, e.getX(), e.getY());
                        dashedLineRasterizer.rasterize(line);
                        panel.repaint();
                    }
                }
            }
        });

    }

}
