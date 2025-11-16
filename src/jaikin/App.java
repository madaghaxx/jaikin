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

    private List<Point> points = new ArrayList<>();
    private boolean isAnimating = false;
    private int step = 0;

    public App(String title) {
        super();
        setBackground(new Color(0));
        setTitle(title);
        addMouseListener(mouseListener);
        addKeyListener(keyListener);
    }

    public void drawPoint(int x, int y) {
        if (!isAnimating) {
            new Circle(new Point(x, y), 4).draw(this);
            repaint();
        }
    }

    public void startAnimation() {
        if (!isAnimating) {
            this.isAnimating = true;
            repaint();
        }
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);

        g.setColor(Color.WHITE);
        if (points.size() == 1) {
            Point p = points.get(0);
            g.drawLine(p.x, p.y, p.x, p.y);
            return;
        }

        if (isAnimating) {
            int size = points.size();
            for (int i = 0; i < size - 1; i++) {
                Point p1 = points.get(i);
                Point p2 = points.get(i + 1);
                new Line(p1, p2).draw(this);
            }
            step = (step + 1) % MAX_STEPS;
        }

        for (Point p : points)
            g.drawLine(p.x, p.y, p.x, p.y);

    }

    @Override
    public void display(int x, int y, Color color) {
        points.add(new Point(x, y));
    }

    @Override
    public void save(String string) {
        repaint();
    }

}
