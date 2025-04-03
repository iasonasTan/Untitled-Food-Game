package main;

import game.entity.Enemy;
import game.entity.Player;
import game.handler.EntityManager;
import game.handler.KeyHandler;
import game.handler.MapHandler;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;

public final class Game extends JPanel {
    // rules
    public static final int TILE_SIZE=50;
    public static final Rectangle SCREEN=new Rectangle(Main.FRAME_SIZE);

    // logic
    private Thread gameThread;
    private final Main main;
    public KeyHandler keyHandler;

    // components
    public MapHandler tileM;
    public Player player;
    public EntityManager<Enemy> enemyM;

    // gui
    private BufferedImage background_image;
    private final JButton pause_button=new JButton("pause");

    public Game (Main main) {
        this.main=main;
        try {
            initUI();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    void initGame () {
        keyHandler=new KeyHandler();
        Main.instance.addKeyListener(keyHandler);
        player=new Player(this);
        tileM=new MapHandler(this);
        enemyM= new EntityManager<>(this, new ArrayList<>()){
            @Override
            public void update() {
                if (keyHandler.left) {
                    enemyM.move(player.getCurrentSpeed(), 0);
                } else if (keyHandler.right) {
                    enemyM.move(-player.getCurrentSpeed(), 0);
                }
                super.update();
            }
        };
        try {
            tileM.loadMap(1);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    void initUI () throws IOException {
        background_image= ImageIO.read(getClass().getResource("/menu/background.png"));
        add(pause_button);
        pause_button.setFocusable(false);
        pause_button.addActionListener(ae->main.pauseGame());
    }

    @Override
    public void paintComponent (Graphics g) {
        super.paintComponent(g);
        //g.drawImage(background_image, 0, 0, Main.FRAME_SIZE.width, Main.FRAME_SIZE.height, null);
        tileM.render(g);
        player.render(g);
        enemyM.render(g);
    }

    public void update () {
        tileM.update();
        player.update();
        enemyM.update();
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
