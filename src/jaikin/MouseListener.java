package jaikin;

import java.awt.Point;
import java.awt.event.MouseEvent;

public class MouseListener implements java.awt.event.MouseListener {
    private final App app;

    MouseListener(App app) {
        this.app = app;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (e.getButton() == MouseEvent.BUTTON1) {
            Point p = e.getPoint();
            app.drawPoint(p.x, p.y);
        }
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }

    @Override
    public void mousePressed(MouseEvent e) {
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

}
