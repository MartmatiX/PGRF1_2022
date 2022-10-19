import model.Line;
import model.Point;
import model.Triangle;
import rasterize.LineRasterizer;
import rasterize.FilledLineRasterizer;
import rasterize.RasterBufferImage;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Canvas {

    private final JPanel panel;

    private final LineRasterizer lineRasterizer;
    private final RasterBufferImage raster;

    private int x_start;
    private int y_start;

    private boolean tPressed = false;

    Triangle t = new Triangle();

    public Canvas(int width, int height) {

        JFrame frame = new JFrame();

        frame.setLayout(new BorderLayout());
        frame.setResizable(false);
        frame.setTitle("Task1 Malir");
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setSize(width, height);

        raster = new RasterBufferImage(width, height);
        lineRasterizer = new FilledLineRasterizer(raster);

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

        panel.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent keyEvent) {
                super.keyPressed(keyEvent);
                if (keyEvent.getKeyCode() == KeyEvent.VK_T) {
                    if (tPressed) {
                        System.out.println("You exited triangle mode: Triangle was cleared.\nYou can start drawing lines.\n");
                    } else {
                        raster.clear();
                        panel.repaint();
                        t = new Triangle();
                        System.out.println("You entered triangle mode.\n");
                    }
                    tPressed = !tPressed;
                }
            }
        });

        // usecka, trojuhelnik

        panel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                if (!tPressed) {
                    raster.clear();
                    x_start = e.getX();
                    y_start = e.getY();
                    raster.setPixel(x_start, y_start, 0xfffff);
                    panel.repaint();
                } else {
                    if (t.getSize() < 2) {
                        t.addPoint(new Point(e.getX(), e.getY()));
                        System.out.println("Point: " + t.getSize() + " added.\n");
                    }
                }
            }
        });

        panel.addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                super.mouseDragged(e);
                if (!tPressed) {
                    raster.clear();
                    Line line = new Line(x_start, y_start, e.getX(), e.getY());
                    lineRasterizer.rasterize(line);
                    panel.repaint();
                } else {
                    if (t.getSize() >= 2) {
                        Point p = new Point(e.getX(), e.getY());
                        t.addPoint(p);
                        raster.clear();
                        if (t.getSize() >= 3) {
                            t.drawTriangle(lineRasterizer);
                            panel.repaint();
                        }
                    }
                }
            }
        });

    }

}
