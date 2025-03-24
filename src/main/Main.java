package main;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public final class Main extends JFrame {
    private final Game game;
    private final Menu menu;
    public static Dimension FRAME_SIZE=new Dimension(1000,800);

    public Main () {
        menu=new Menu(this);
        game=new Game(this);
        try {
            initFrame();
        } catch (IOException e) {
            System.out.println();
            throw new RuntimeException(e);
        }
    }

    public void pauseGame() {
        game.stop();
        setLayout(null);
        final int PAD=50;
        menu.setBounds(PAD, PAD, FRAME_SIZE.width-PAD*2,
                FRAME_SIZE.height-PAD*2);
        this.add(menu);
        revalidate();
        repaint();
    }

    private void initFrame () throws IOException {
        setContentPane(menu);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setSize(FRAME_SIZE);
        setVisible(true);
    }

    public void startGame() {
        remove(menu);
        setContentPane(game);
        revalidate();
        repaint();
        game.start();
    }

    public static void main(String[] args) {
        new Main();
    }
}

