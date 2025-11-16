package shapes;

import java.awt.Color;

public class Point extends Colored {
    public int x;
    public int y;

    public Point(int xx, int yy, Color color) {
        this.x = xx;
        this.y = yy;
        this.color = color;
    }

    public Point(int xx, int yy) {
        this.x = xx;
        this.y = yy;
    }
}
