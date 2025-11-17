
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;

public class Canvas extends JPanel {
    private static final int POINT_RADIUS = 6;
    private final List<Point> controlPoints;      
    private List<Point> displayedPoints;          // will change them in animations 7 step 
    private final int maxSteps = 7;
    private int currentStep = 0;
    private Timer animationTimer;
    private boolean animating = false;
    private boolean showHelp = false;
    private boolean closedShape = false;
    private Point draggedPoint = null;
    private static final int DRAG_THRESHOLD = 10;

    public Canvas() {
        this.controlPoints = new ArrayList<>();
        this.displayedPoints = new ArrayList<>();
        setPreferredSize(new Dimension(800, 600));
        setBackground(Color.BLACK);

        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON1) {
                    // Check if clicking near an existing point for dragging
                    draggedPoint = findNearestPoint(e.getX(), e.getY(), DRAG_THRESHOLD);
                    if (draggedPoint == null && !animating) {
                        // Add new point if not dragging and not animating
                        controlPoints.add(new Point(e.getX(), e.getY()));
                        displayedPoints.add(new Point(e.getX(), e.getY()));
                        repaint();
                    }
                    requestFocusInWindow();
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                draggedPoint = null;
            }
        });

        addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                if (draggedPoint != null) {
                    draggedPoint.x = e.getX();
                    draggedPoint.y = e.getY();
                    
                    // Update in real-time during drag
                    displayedPoints = new ArrayList<>(controlPoints);
                    if (animating) {
                        // Recalculate the current animation step with new positions
                        for (int i = 0; i < currentStep; i++) {
                            displayedPoints = chaikinIteration(displayedPoints);
                        }
                    }
                    
                    repaint();
                }
            }
        });
        
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

        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_H, 0), "toggleHelp");
        am.put("toggleHelp", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showHelp = !showHelp;
                repaint();
            }
        });

        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_C, 0), "clearScreen");
        am.put("clearScreen", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                controlPoints.clear();
                displayedPoints.clear();
                currentStep = 0;
                closedShape = false;  // Reset closed shape when clearing
                if (animating) {
                    animationTimer.stop();
                    animating = false;
                }
                repaint();
            }
        });

        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_S, 0), "toggleClosed");
        am.put("toggleClosed", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (controlPoints.size() >= 3) {
                    closedShape = !closedShape;
                    if (animating) {
                        restartAnimation();
                    } else {
                        displayedPoints = new ArrayList<>(controlPoints);
                        repaint();
                    }
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

        if (!closedShape) {
            // Open curve: keep first point
            next.add(new Point(pts.get(0).x , pts.get(0).y));
        }

        int limit = closedShape ? n : n - 1;
        for (int i = 0; i < limit; i++) {
            Point p0 = pts.get(i);
            Point p1 = pts.get((i + 1) % n);

            double qx = 0.75 * p0.x + 0.25 * p1.x;
            double qy = 0.75 * p0.y + 0.25 * p1.y;
            double rx = 0.25 * p0.x + 0.75 * p1.x;
            double ry = 0.25 * p0.y + 0.75 * p1.y;

            next.add(new Point((int) Math.round(qx), (int) Math.round(qy)));
            next.add(new Point((int) Math.round(rx), (int) Math.round(ry)));
        }

        if (!closedShape) {
            // Open curve: keep last point
            next.add(new Point(pts.get(n-1).x , pts.get(n-1).y));
        }

        return next;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        if (displayedPoints.size() >= 2) {
            g2d.setColor(Color.GREEN);
            for (int i = 0; i < displayedPoints.size() - 1; i++) {
                Point a = displayedPoints.get(i);
                Point b = displayedPoints.get(i + 1);
                g2d.drawLine((int)a.x, (int)a.y, (int)b.x, (int)b.y);
            }
            // Draw closing line only if closed shape and we have 3+ control points
            if (closedShape && controlPoints.size() >= 3) {
                Point first = displayedPoints.get(0);
                Point last = displayedPoints.get(displayedPoints.size() - 1);
                g2d.drawLine((int)last.x, (int)last.y, (int)first.x, (int)first.y);
            }
        }

        // draw smoothed curve points (smaller, green)
        if (animating) {
            g2d.setColor(new Color(100, 255, 100, 150));
            for (Point p : displayedPoints) {
                g2d.fillOval((int)p.x - 2, (int)p.y - 2, 4, 4);
            }
        } else {
            // When not animating, draw the curve points
            g2d.setColor(Color.WHITE);
            for (Point p : displayedPoints) {
                g2d.fillOval((int)p.x - 2, (int)p.y - 2, 4, 4);
            }
        }

        // Always draw control points (larger, white with red outline)
        g2d.setColor(Color.RED);
        for (Point p : controlPoints) {
            g2d.fillOval((int)p.x - POINT_RADIUS, (int)p.y - POINT_RADIUS,
                    POINT_RADIUS * 2, POINT_RADIUS * 2);
        }
        g2d.setColor(Color.WHITE);
        for (Point p : controlPoints) {
            g2d.fillOval((int)p.x - POINT_RADIUS + 1, (int)p.y - POINT_RADIUS + 1,
                    POINT_RADIUS * 2 - 2, POINT_RADIUS * 2 - 2);
        }

        g2d.setFont(g2d.getFont().deriveFont(12f));
        g2d.setColor(Color.LIGHT_GRAY);
        String status;
        if (animating) {
            status = String.format("Animating - step %d/%d (press Enter to restart, Esc to quit)", currentStep+1, maxSteps);
        } else {
            String closedStatus = controlPoints.size() < 3 ? "N/A" : (closedShape ? "YES" : "NO");
            status = String.format("Points: %d | Closed: %s (H for help)", 
                controlPoints.size(), closedStatus);
        }
        g2d.drawString(status, 8, 16);

        // Draw help text if enabled
        if (showHelp) {
            g2d.setColor(new Color(0, 0, 0, 200));
            g2d.fillRect(10, 30, 280, 130);
            g2d.setColor(Color.WHITE);
            int y = 50;
            g2d.drawString("HELP - Keyboard Shortcuts:", 20, y);
            y += 20;
            g2d.drawString("H - Toggle this help", 20, y);
            y += 20;
            g2d.drawString("C - Clear screen", 20, y);
            y += 20;
            g2d.drawString("S - Toggle closed shape (3+ pts)", 20, y);
            y += 20;
            g2d.drawString("Enter - Start animation", 20, y);
            y += 20;
            g2d.drawString("Esc - Quit", 20, y);
            y += 20;
            g2d.drawString("Left-click - Add point", 20, y);
            y += 20;
            g2d.drawString("Drag - Move control points (anytime)", 20, y);
        }
    }

    private Point findNearestPoint(int x, int y, int threshold) {
        for (Point p : controlPoints) {
            double dist = Math.sqrt(Math.pow(p.x - x, 2) + Math.pow(p.y - y, 2));
            if (dist <= threshold) {
                return p;
            }
        }
        return null;
    }

    public List<Point> getControlPoints() {
        return new ArrayList<>(controlPoints);
    }
}
