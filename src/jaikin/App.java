package jaikin;

import java.awt.Color;
import java.awt.Frame;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;

import shapes.Circle;
import shapes.Displayable;
import shapes.Line;
import shapes.Point;

public class App extends Frame implements Displayable {
    private final MouseListener mouseListener = new MouseListener(this);
    private final KeyListener keyListener = new KeyListener(this);
    private final int MAX_STEPS = 7;
    private final int POINT_RADIUS = 4;
    private final int LINE_THICKNESS = POINT_RADIUS / 2;

    private List<Circle> points = new ArrayList<>();
    private List<Point> pixels = new ArrayList<>();
    private boolean isAnimating = false;
    private int step = 0;

    public App(String title) {
        super(title);
        setBackground(Color.BLACK);
        addMouseListener(mouseListener);
        addKeyListener(keyListener);
    }

    public void drawPoint(int x, int y) {
        if (isAnimating)
            return;

        points.add(circledPoint(x, y));
        repaint();
    }

    public Circle circledPoint(int x, int y) {
        return new Circle(new Point(x, y), POINT_RADIUS);
    }

    public void startAnimation() {
        if (points.size() < 2 || isAnimating)
            return;

        isAnimating = true;
        new Thread(() -> {
            while (true) {
                List<Circle> chaikinPoints = new ArrayList<>(points);
                for (int i = 0; i < chaikinPoints.size() - 1; i++) {
                    Circle p1 = chaikinPoints.get(i);
                    Circle p2 = chaikinPoints.get(i + 1);
                    new Line(p1.getCenter(), p2.getCenter(), LINE_THICKNESS).draw(this);
                }

                repaint();
                try {
                    Thread.sleep(1000);
                } catch (Exception e) {
                    System.out.println(e);
                }

                for (int step = 1; step < MAX_STEPS; step++) {
                    // CHAIKIN’S ALGORITHMS
                    List<Circle> copy = new ArrayList<>(chaikinPoints);
                    chaikinPoints.clear();
                    chaikinPoints.add(copy.get(0));
                    for (int i = 0; i < copy.size() - 1; i++) {
                        Circle c1 = copy.get(i);
                        Circle c2 = copy.get(i + 1);
                        Point p1 = c1.getCenter();
                        Point p2 = c2.getCenter();

                        double dx = p2.x - p1.x;
                        double dy = p2.y - p1.y;

                        double xQuarter = p1.x + dx / 4.0; // 1/4 from p1
                        double yQuarter = p1.y + dy / 4.0;
                        double xThreeQ = p1.x + 3.0 * dx / 4.0; // 3/4 from p1
                        double yThreeQ = p1.y + 3.0 * dy / 4.0;

                        chaikinPoints.add(circledPoint((int) Math.round(xQuarter), (int) Math.round(yQuarter)));
                        chaikinPoints.add(circledPoint((int) Math.round(xThreeQ), (int) Math.round(yThreeQ)));
                    }
                    chaikinPoints.add(copy.get(copy.size() - 1));

                    // draw new shape
                    for (int i = 0; i < chaikinPoints.size() - 1; i++) {
                        Circle p1 = chaikinPoints.get(i);
                        Circle p2 = chaikinPoints.get(i + 1);
                        new Line(p1.getCenter(), p2.getCenter(), LINE_THICKNESS).draw(this);
                    }
                    repaint();

                    try {
                        Thread.sleep(1000);
                    } catch (Exception e) {
                        System.out.println(e);
                    }
                }
            }
        }).start();
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        for (Circle point : points)
            point.draw(this);

        g.setColor(Color.WHITE);
        for (Point p : pixels) {
            g.drawLine(p.x, p.y, p.x, p.y);
        }
        pixels.clear();
    }

    @Override
    public void display(int x, int y, Color color) {
        pixels.add(new Point(x, y));
    }
}
