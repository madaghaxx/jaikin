package jaikin;

import java.awt.event.KeyEvent;

public class KeyListener implements java.awt.event.KeyListener {
    private final App app;

    KeyListener(App app) {
        this.app = app;
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ENTER) {
            app.startAnimation();
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

}
