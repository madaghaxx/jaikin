import java.awt.Dimension;
import java.awt.Toolkit;

import jaikin.App;

public class Main {
    private static final Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

    public static void main(String[] args) {
        App app = new App("Jaikin");
        app.setSize(screenSize);
        app.setVisible(true);
    }
}
