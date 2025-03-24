package main;

import entity.Player;

import javax.swing.*;
import java.awt.*;

public final class Game extends JPanel {
    // rules
    public static final int TILE_SIZE=25;

    // logic
    private Thread gameThread;
    private final Main main;

    // components
    public TileManager tileM;
    public Player player;

    // gui
    private final JButton pause_button=new JButton("pause");

    public Game (Main main) {
        this.main=main;
        initUI();
        initGame();
        loadMap();
    }

    private void initGame () {
        player=new Player(this);
        tileM=new TileManager(this);
        try {
            tileM.loadMap(1);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void loadMap () {
        try {

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void initUI () {
        add(pause_button);
        pause_button.addActionListener(ae->main.pauseGame());
    }

    @Override
    public void paintComponent (Graphics g) {
        g.fillRect(10,10,100,100);
        tileM.render(g);
    }

    public void update () {
        
    }

    public void start() {
        gameThread=new Thread(this::loop);
        gameThread.start();
        add(pause_button);
    }

    public void stop() {
        gameThread=null;
        remove(pause_button);
    }

    public void loop () {
        long wait, diff, prev, curr;
        final long TARGET_FPS=60;

        while (gameThread!=null) {
            prev=System.currentTimeMillis();

            update();
            repaint();

            curr=System.currentTimeMillis();
            diff=curr-prev;
            wait=TARGET_FPS-diff;

            try {
                Thread.sleep(wait>=0?wait:0);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

}
