import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

public class Canvas extends JPanel {
    private static final int POINT_RADIUS = 3;
    private List<Point> controlPoints;
    public Canvas() {
        this.controlPoints = new ArrayList<>();
        setPreferredSize(new Dimension(800, 600));
        setBackground(Color.BLACK);

        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON1) {
                    controlPoints.add(new Point(e.getX(), e.getY()));
                    repaint();
                }
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g2d.setColor(Color.WHITE);
        for (Point p : controlPoints) {
            g2d.fillOval((int) (p.x - POINT_RADIUS), (int) (p.y - POINT_RADIUS),
                    POINT_RADIUS * 2, POINT_RADIUS * 2);
        }
    }

    public List<Point> getControlPoints() {
        return controlPoints;
    }
}
