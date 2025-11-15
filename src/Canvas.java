
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

public class Canvas extends JPanel {
    private static final int POINT_RADIUS = 3;
    private final List<Point> controlPoints;      
    private List<Point> displayedPoints;          // will change them in animations 7 step 
    private final int maxSteps = 7;
    private int currentStep = 0;
    private Timer animationTimer;
    private boolean animating = false;

    public Canvas() {
        this.controlPoints = new ArrayList<>();
        this.displayedPoints = new ArrayList<>();
        setPreferredSize(new Dimension(800, 600));
        setBackground(Color.BLACK);

        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON1 && !animating) {
                    controlPoints.add(new Point(e.getX(), e.getY()));
                    displayedPoints.add(new Point(e.getX(), e.getY()));
                    repaint();
                    requestFocusInWindow();
                }
            }
        });

        // setFocusable(true);
        // requestFocusInWindow();

        // maps for acrions 
        InputMap im = getInputMap(WHEN_IN_FOCUSED_WINDOW);
        ActionMap am = getActionMap();

        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), "startAnimation");
        am.put("startAnimation", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleEnterPressed();
            }
        });

        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), "closeWindow");
        am.put("closeWindow", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Window w = SwingUtilities.getWindowAncestor(Canvas.this);
                if (w != null) {
                    w.dispose();
                } else {
                    System.exit(0);
                }
            }
        });

        int delayMs = 700;
        animationTimer = new Timer(delayMs, evt -> {
            stepChaikinAnimation();
        });
    }

    private void handleEnterPressed() {
        int n = controlPoints.size();

        if (animating) {
            restartAnimation();
            return;
        }
        if (n == 2) {
            displayedPoints = new ArrayList<>(controlPoints);
            repaint();
            return;
        }

        // n >= 3 -> start animation
        startAnimation();
    }

    private void startAnimation() {
        animating = true;
        currentStep = 0;
        displayedPoints = new ArrayList<>(controlPoints);
        animationTimer.start();
    }

    private void restartAnimation() {
        animationTimer.stop();
        animating = true;
        currentStep = 0;
        displayedPoints = new ArrayList<>(controlPoints);
        animationTimer.start();
    }

    private void stepChaikinAnimation() {
        if (controlPoints.size() <= 2) {
            animationTimer.stop();
            animating = false;
            displayedPoints = new ArrayList<>(controlPoints);
            repaint();
            return;
        }

        // each time 
        displayedPoints = chaikinIteration(displayedPoints);
        currentStep++;
        repaint();

        if (currentStep >= maxSteps) {
            displayedPoints = new ArrayList<>(controlPoints);
            currentStep = 0;
        }
    }


    private List<Point> chaikinIteration(List<Point> pts) {
        List<Point> next = new ArrayList<>();
        int n = pts.size();
        if (n == 0) return next;
        if (n == 1) {
            next.add(new Point(pts.get(0).x , pts.get(0).y));
            return next;
        }

        next.add(new Point(pts.get(0).x , pts.get(0).y));

        for (int i = 0; i < n - 1; i++) {
            Point p0 = pts.get(i);
            Point p1 = pts.get(i + 1);

            double qx = 0.75 * p0.x + 0.25 * p1.x;
            double qy = 0.75 * p0.y + 0.25 * p1.y;
            double rx = 0.25 * p0.x + 0.75 * p1.x;
            double ry = 0.25 * p0.y + 0.75 * p1.y;

            next.add(new Point((int) Math.round(qx), (int) Math.round(qy)));
            next.add(new Point((int) Math.round(rx), (int) Math.round(ry)));
        }

        next.add(new Point(pts.get(n-1).x , pts.get(n-1).y));

        return next;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // draw lines if >=2 points
        if (displayedPoints.size() >= 2) {
            g2d.setColor(Color.GREEN);
            for (int i = 0; i < displayedPoints.size() - 1; i++) {
                Point a = displayedPoints.get(i);
                Point b = displayedPoints.get(i + 1);
                g2d.drawLine((int)a.x, (int)a.y, (int)b.x, (int)b.y);
            }
        }

        // draw control points 
        g2d.setColor(Color.WHITE);
        for (Point p : displayedPoints) {
            g2d.fillOval((int)p.x - POINT_RADIUS, (int)p.y - POINT_RADIUS,
                    POINT_RADIUS, POINT_RADIUS );
        }

        g2d.setFont(g2d.getFont().deriveFont(12f));
        g2d.setColor(Color.LIGHT_GRAY);
        String status;
        if (animating) {
            status = String.format("Animating - step %d/%d (press Enter to restart, Esc to quit)", currentStep+1, maxSteps);
        } else {
            status = String.format("Points: %d (Left-click to add). Press Enter to animate (>=3 points). Esc to quit.", controlPoints.size());
        }
        g2d.drawString(status, 8, 16);
    }

    public List<Point> getControlPoints() {
        return new ArrayList<>(controlPoints);
    }
}
