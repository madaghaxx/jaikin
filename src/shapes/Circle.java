package shapes;

import java.awt.Color;

public class Circle extends Colored implements Drawable {
    Point center;
    int radius;
    static double radian = Math.PI / 180;

    public Circle(Point center, int radius) {
        this.center = center;
        this.radius = radius;
    }

    public static Circle random(int maxWidth, int maxHeight) {
        double x = Math.random() * maxWidth;
        double y = Math.random() * maxHeight;
        double radius = Math.random() * maxHeight;

        Point randomPoint = new Point((int) x, (int) y);
        return new Circle(randomPoint, (int) radius);
    }

    public void draw(Displayable displayable) {
        for (double i = 0; i < 360; i += 1.0 / radius) {
            double angle = radian * i;
            double x = center.x + radius * Math.cos(angle);
            double y = center.y + radius * Math.sin(angle);
            displayable.display((int) Math.round(x), (int) Math.round(y), getColor());
        }
    }

    public Color getColor() {
        return this.color;
    }
}