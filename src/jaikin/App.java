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
    private int step = 0;
    private boolean isAnimating = false;

    public App(String title) {
        super(title);
        setBackground(Color.BLACK);
        addMouseListener(mouseListener);
        addKeyListener(keyListener);
    }

    public void drawPoint(int x, int y) {
        if (isAnimating)
            return;

        Circle point = new Circle(new Point(x, y), POINT_RADIUS);
        points.add(point);
        repaint();
    }

    public void startAnimation() {
        if (points.size() < 2 || isAnimating)
            return;
        isAnimating = true;

        new Thread(() -> {
            while (true) {
                for (int i = 0; i < points.size() - 1; i++) {
                    Circle p1 = points.get(i);
                    Circle p2 = points.get(i + 1);
                    System.out.println("1pixels: " + pixels.size());
                    new Line(p1.getCenter(), p2.getCenter(), LINE_THICKNESS).draw(this);
                }
                step = (step + 1) % MAX_STEPS;
                repaint();
                return;
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
