package shapes;

import java.awt.Color;
import java.util.Random;

public class Colored {
    public Color color = Colored.getRandomColor();

    static Color getRandomColor() {
        Random rnd = new Random();
        return new Color(rnd.nextFloat(), rnd.nextFloat(), rnd.nextFloat());
    }
}
