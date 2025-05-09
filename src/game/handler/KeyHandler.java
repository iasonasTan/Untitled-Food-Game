package game.handler;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyHandler implements KeyListener {
    public volatile boolean s_key_blank, left, right;
    public volatile boolean shift, jump;

    @Override public void keyTyped(KeyEvent e) {}

    @Override
    public void keyPressed(KeyEvent e) {
        final int CODE = e.getKeyCode();
        switch (CODE) {
            case KeyEvent.VK_S,
                 KeyEvent.VK_DOWN -> s_key_blank =true;
            case KeyEvent.VK_A,
                 KeyEvent.VK_LEFT-> left=true;
            case KeyEvent.VK_D,
                 KeyEvent.VK_RIGHT-> right=true;
            case KeyEvent.VK_SPACE,
                 KeyEvent.VK_W,
                 KeyEvent.VK_UP  -> jump =true;
            case KeyEvent.VK_SHIFT -> shift=true;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        final int CODE = e.getKeyCode();
        switch (CODE) {
            case KeyEvent.VK_S,
                 KeyEvent.VK_DOWN-> s_key_blank =false;
            case KeyEvent.VK_A,
                 KeyEvent.VK_LEFT -> left=false;
            case KeyEvent.VK_D,
                 KeyEvent.VK_RIGHT -> right=false;
            case KeyEvent.VK_SPACE,
                 KeyEvent.VK_W,
                 KeyEvent.VK_UP -> jump =false;
            case KeyEvent.VK_SHIFT -> shift=false;
        }
    }
}
