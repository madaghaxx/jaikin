package shapes;

import java.awt.Color;

public class Line extends Colored implements Drawable {
    Point a, b;
    private int thickness = 0;

    public Line(Point a, Point b) {
        this.a = a;
        this.b = b;
    }

    public Line(Point a, Point b, int thickness) {
        this.a = a;
        this.b = b;
        this.thickness = thickness;
    }

    public Line(Point a, Point b, Color color) {
        this.a = a;
        this.b = b;
        this.color = color;
    }

    public void draw(Displayable displayable) {
        float distX = b.x - a.x;
        float distY = b.y - a.y;

        float maxSteps = Math.max(Math.abs(distX), Math.abs(distY));

        float stepX = distX / maxSteps;
        float stepY = distY / maxSteps;

        for (int i = 0; i < maxSteps; i++) {
            int nextX = Math.round(a.x + stepX * i);
            int nextY = Math.round(a.y + stepY * i);
            displayable.display(nextX, nextY, getColor());
        }

        this.drawThickness(displayable);
    }

    private void drawThickness(Displayable displayable) {
        int factor = -1;
        while (factor < 2) {
            for (int i = 0; i < this.thickness; i++) {
                Point p1 = new Point(a.x + i * factor, a.y + i * factor);
                Point p2 = new Point(b.x + i * factor, b.y + i * factor);
                new Line(p1, p2).draw(displayable);
            }
            factor += 2;
        }
    }

    public Color getColor() {
        return this.color;
    }

}
