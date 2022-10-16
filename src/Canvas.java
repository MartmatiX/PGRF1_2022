import model.Line;
import rasterize.FilledLineRasterizer;
import rasterize.LineRasterizer;
import rasterize.NaiveLineAlgorithm;
import rasterize.RasterBufferImage;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Canvas {

    private int width;
    private int height;

    private JFrame frame;
    private JPanel panel;

    private LineRasterizer lineRasterizer;
    private RasterBufferImage raster;

    public Canvas(int width, int height) {
        this.width = width;
        this.height = height;

        frame = new JFrame();

        frame.setLayout(new BorderLayout());
        frame.setResizable(false);
        frame.setTitle("Task1 Malir");
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setSize(this.width, this.height);

        raster = new RasterBufferImage(this.width, this.height);
        //lineRasterizer = new FilledLineRasterizer(raster);
        lineRasterizer = new NaiveLineAlgorithm(raster);

        panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics graphics) {
                super.paintComponent(graphics);
                raster.present(graphics);
            }
        };
        panel.setPreferredSize(new Dimension(this.width, this.height));

        frame.add(panel, BorderLayout.CENTER);

        frame.pack();
        frame.setVisible(true);

        panel.requestFocus();
        panel.requestFocusInWindow();

        final int[] x_start = new int[1];
        final int[] y_start = new int[1];

        panel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent mouseEvent) {
                super.mouseClicked(mouseEvent);
                raster.clear();
                x_start[0] = mouseEvent.getX();
                y_start[0] = mouseEvent.getY();
                raster.setPixel(x_start[0], y_start[0], 0xfffff);
                panel.repaint();
            }
        });

        panel.addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                super.mouseDragged(e);

                raster.clear();

                Line line = new Line(x_start[0], y_start[0], e.getX(), e.getY());
                lineRasterizer.rasterize(line);

                panel.repaint();
            }
        });

    }

}
