import rasterize.RasterBufferImage;
import render.WireRenderer;
import solids.Cube;
import transforms.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class Controller3D {
    private final JPanel panel;
    private final RasterBufferImage raster;
    private final WireRenderer wireRenderer;
    private final Mat4 model = new Mat4Identity();

    // Kamera
    Camera camera = new Camera(new Vec3D(-12, 1, 1), 0.1, -0.2, 1, true);
    private final double CAMERA_SPEED = 0.3;

    public Controller3D(int width, int height) {
        JFrame frame = new JFrame();

        frame.setLayout(new BorderLayout());
        frame.setTitle("PGRF1 Malir");
        frame.setResizable(false);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        raster = new RasterBufferImage(800, 600);
        wireRenderer = new WireRenderer();
        wireRenderer.setBufferedImage(raster.getImg());

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

        panel.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                super.keyPressed(e);
                switch (e.getKeyCode()){
                    case KeyEvent.VK_W -> camera = camera.up(CAMERA_SPEED);
                    case KeyEvent.VK_S -> camera = camera.down(CAMERA_SPEED);
                    case KeyEvent.VK_A -> camera = camera.left(CAMERA_SPEED);
                    case KeyEvent.VK_D -> camera = camera.right(CAMERA_SPEED);
                }
            }
        });
    }

    public void render(){
        wireRenderer.setView(camera.getViewMatrix());
        wireRenderer.setProj(new Mat4OrthoRH(20, 20, 0.1, 200));
        wireRenderer.renderSolid(new Cube(), this.model);
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
}
