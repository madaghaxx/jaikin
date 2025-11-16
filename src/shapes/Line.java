package shapes;

import java.awt.Color;

public class Line extends Colored implements Drawable {
    Point a, b;

    public Line(Point a, Point b) {
        this.a = a;
        this.b = b;
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
    }

    public Color getColor() {
        return this.color;
    }

}
