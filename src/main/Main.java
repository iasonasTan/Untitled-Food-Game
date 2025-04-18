package main;

import javax.swing.*;
import java.awt.*;

public final class Main extends JFrame {
    public Game game;
    public Menu menu;
    public static Dimension FRAME_SIZE=new Dimension(1000,800);
    public static Main instance;

    public Main () {

    }

    void initComponents(){
        menu=new Menu(this);
        initFrame();
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

    private void initFrame () {
        setContentPane(menu);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        setSize(FRAME_SIZE);
        setVisible(true);
    }

    public void startGame(int map) {
        game=new Game(this);
        game.initGame(map);
        remove(menu);
        setContentPane(game);
        revalidate();
        repaint();
        game.start();
    }

    public static void main(String[] args) {
        instance=new Main();
        instance.initComponents();
    }
}

