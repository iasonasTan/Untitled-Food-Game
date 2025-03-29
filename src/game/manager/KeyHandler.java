package game.manager;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyHandler implements KeyListener {
    public boolean jump, s_key_blank, left, right;
    public boolean space, shift;

    @Override public void keyTyped(KeyEvent e) {}

    @Override
    public void keyPressed(KeyEvent e) {
        final int CODE = e.getKeyCode();
        switch (CODE) {
            case KeyEvent.VK_W -> jump =true;
            case KeyEvent.VK_S -> s_key_blank =true;
            case KeyEvent.VK_A -> left=true;
            case KeyEvent.VK_D -> right=true;
            case KeyEvent.VK_SPACE -> space=true;
            case KeyEvent.VK_SHIFT -> shift=true;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        final int CODE = e.getKeyCode();
        switch (CODE) {
            case KeyEvent.VK_W -> jump =false;
            case KeyEvent.VK_S -> s_key_blank =false;
            case KeyEvent.VK_A -> left=false;
            case KeyEvent.VK_D -> right=false;
            case KeyEvent.VK_SPACE -> space=false;
            case KeyEvent.VK_SHIFT -> shift=false;
        }
    }
}
