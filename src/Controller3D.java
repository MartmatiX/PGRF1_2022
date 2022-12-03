import rasterize.FilledLineRasterizer;
import rasterize.RasterBufferImage;
import render.WireRenderer;
import solids.Cube;
import solids.Solid;
import transforms.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

public class Controller3D {
    private final JPanel panel;
    private final RasterBufferImage raster;
    private final WireRenderer wireRenderer;
    private Mat4 model = new Mat4Identity();

    private final List<Solid> solids = new ArrayList<>();

    // Kamera
    Camera camera = new Camera(new Vec3D(0, 0, 0), 0.1, -0.2, 1, true);
    private final double CAMERA_SPEED = 1;

    public Controller3D(int width, int height) {

        // Frame + Panel
        JFrame frame = new JFrame();

        frame.setLayout(new BorderLayout());
        frame.setTitle("PGRF1 Malir");
        frame.setResizable(false);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        raster = new RasterBufferImage(800, 600);

        panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                raster.present(g);
            }
        };

        panel.setPreferredSize(new Dimension(width, height));

        frame.add(panel, BorderLayout.CENTER);
        frame.pack();
        frame.setVisible(true);

        panel.requestFocus();
        panel.requestFocusInWindow();

        // Ovladani
        panel.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                super.keyPressed(e);
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_W -> camera = camera.up(CAMERA_SPEED);
                    case KeyEvent.VK_S -> camera = camera.down(CAMERA_SPEED);
                    case KeyEvent.VK_A -> camera = camera.left(CAMERA_SPEED);
                    case KeyEvent.VK_D -> camera = camera.right(CAMERA_SPEED);
                    case KeyEvent.VK_ESCAPE -> {
                        System.out.println("Goodbye!\n");
                        System.exit(0);
                    }
                }
                render();
            }
        });

        final double UNZOOM_MODIFIER = 0.8;
        final double ZOOM_MODIFIER = 1.2;

        panel.addMouseWheelListener(e -> {
            Mat4 scale;
            if (e.getWheelRotation() < 0) {
                scale = new Mat4Scale(ZOOM_MODIFIER, ZOOM_MODIFIER, ZOOM_MODIFIER);
            } else {
                scale = new Mat4Scale(UNZOOM_MODIFIER, UNZOOM_MODIFIER, UNZOOM_MODIFIER);
            }
            model = model.mul(scale);
            render();
        });

        // CV 3 ops
        FilledLineRasterizer filledLineRasterizer = new FilledLineRasterizer(raster, 0xffff);
        wireRenderer = new WireRenderer(filledLineRasterizer, raster.getImg(), camera.getViewMatrix(), new Mat4OrthoRH(20, 20, 0.1, 200));

        Cube c1 = new Cube();
        solids.add(c1);
    }

    public void render() {
        clearCanvas();
        wireRenderer.setView(camera.getViewMatrix());
        wireRenderer.renderScene(solids, model);
        present();
    }

    public void present() {
        if (panel.getGraphics() != null)
            panel.getGraphics().drawImage(raster.getImg(), 0, 0, null);
    }

    public void start() {
        raster.clear();
        render();
        panel.repaint();
    }

    public void clearCanvas() {
        raster.clear();
    }
}
