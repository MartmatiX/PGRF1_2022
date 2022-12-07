import rasterize.FilledLineRasterizer;
import rasterize.RasterBufferImage;
import render.WireRenderer;
import solids.*;
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

    private int flag = 1;

    private Point2D mousePos;

    private Mat4 projection = new Mat4OrthoRH(20, 20, 0.1, 200);

    private final List<Solid> solids = new ArrayList<>();

    Cube c1 = new Cube();
    AxisRGB aRGB = new AxisRGB();
    Prism p1 = new Prism();
    Octahedron o1 = new Octahedron();
    Solid ferguson = new CubicTranslation(Cubic.FERGUSON, 0xff0000);
    Solid coons = new CubicTranslation(Cubic.COONS, 0x00ff00);
    Solid bezier = new CubicTranslation(Cubic.BEZIER, 0x0000ff);

    // Kamera
    Camera camera = new Camera(new Vec3D(0, 0, 0), 0.1, -0.2, 1, true);
    private final double CAMERA_SPEED = 1;

    public Controller3D(int width, int height) {
        solids.add(aRGB);

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
                    case KeyEvent.VK_1 -> flag = 1;
                    case KeyEvent.VK_2 -> flag = 2;
                    case KeyEvent.VK_M -> {
                        solids.add(c1);
                        solids.add(p1);
                        solids.add(o1);
                        solids.add(ferguson);
                        solids.add(coons);
                        solids.add(bezier);
                        new Mat4Identity();

                    }
                    case KeyEvent.VK_C -> resetAll();
                    case KeyEvent.VK_O -> {
                        solids.clear();
                        solids.add(o1);
                    }
                    case KeyEvent.VK_P -> {
                        solids.clear();
                        solids.add(p1);
                    }
                    case KeyEvent.VK_K -> {
                        solids.clear();
                        solids.add(c1);
                    }
                    case KeyEvent.VK_F -> {
                        solids.clear();
                        solids.add(ferguson);
                    }
                    case KeyEvent.VK_B -> {
                        solids.clear();
                        solids.add(bezier);
                    }
                    case KeyEvent.VK_H -> {
                        solids.clear();
                        solids.add(coons);
                    }
                }
                solids.add(aRGB);
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

        panel.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                super.mousePressed(e);
                mousePos = new Point2D(e.getX(), e.getY());
                panel.addMouseMotionListener(new MouseAdapter() {
                    @Override
                    public void mouseDragged(MouseEvent f) {
                        super.mouseDragged(f);
                        double dx = f.getX() - mousePos.getX();
                        double dy = f.getY() - mousePos.getY();

                        switch (flag) {
                            case 1 -> {
                                camera = camera.addAzimuth(-(dx) * Math.PI / 360);
                                camera = camera.addZenith(-(dy) * Math.PI / 360);

                                mousePos = new Point2D(f.getX(), f.getY());
                            }
                            case 2 -> {
                                Mat4 rot = new Mat4RotXYZ(0, (-(dy) * 0.0002), (-(dx) * 0.0002));
                                model = model.mul(rot);
                            }
                        }
                        render();
                    }
                });
            }
        });

        FilledLineRasterizer filledLineRasterizer = new FilledLineRasterizer(raster);
        wireRenderer = new WireRenderer(filledLineRasterizer, raster.getImg(), new Mat4OrthoRH(20, 20, 0.1, 200));
    }

    public void render() {
        clearCanvas();
        wireRenderer.setView(camera.getViewMatrix());
        wireRenderer.setProjection(projection);
        wireRenderer.renderScene(solids, this.model);
        present();
    }

    public void present() {
        if (panel.getGraphics() != null) panel.getGraphics().drawImage(raster.getImg(), 0, 0, null);
    }

    public void start() {
        raster.clear();
        render();
        panel.repaint();
    }

    public void setCameraView(Mat4 projection) {
        this.projection = projection;
        render();
    }

    public void clearCanvas() {
        raster.clear();
    }

    public void resetAll() {
        solids.clear();
        camera = new Camera(new Vec3D(0, 0, 0), 0.1, -0.2, 1, true);
        solids.add(aRGB);
    }
}
