package main;

import game.handler.EntityManager;
import game.handler.KeyHandler;
import game.handler.MapHandler;
import game.model.Enemy;
import game.model.Player;
import game.model.Projectile;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;

public final class Game extends JPanel {
    // rules
    public static final int DEFAULT_SIZE =50;
    public static final Rectangle SCREEN=new Rectangle(Main.FRAME_SIZE);

    // logic
    private Thread gameThread;
    public KeyHandler keyHandler;
    private boolean running =false;

    // components
    public MapHandler map;
    public Player player;
    public EntityManager<Enemy> enemiesMan;
    public EntityManager<Projectile> projectilesMan;

    // gui
    private BufferedImage background_image;
    private final JButton pause_button=new JButton("pause");

    public Game () {
        try {
            initUI();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void initGame(int mapID) {
        keyHandler=new KeyHandler();
        Main.getInstance().getWindow().addKeyListener(keyHandler);
        player=new Player(this);
        map =new MapHandler(this);
        enemiesMan = new EntityManager<>(this, new ArrayList<>());
        projectilesMan =new EntityManager<>(this, new LinkedList<>()){
            public void update () {
                super.update();
                entities.removeIf(projectile ->
                        !projectile.getRect().intersects(Game.SCREEN)
                        || map.collider(projectile).isPresent()||
                        projectile.collides(context.player));
            }
        };
        try {
            map.loadMap(mapID);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @SuppressWarnings("all")
    void initUI () throws IOException {
        background_image= ImageIO.read(getClass().getResource("/menu/background.png"));
        add(pause_button);
        pause_button.setFocusable(false);
        pause_button.addActionListener(ae->Main.getInstance().pauseGame());
    }

    @Override
    public void paintComponent (Graphics g) {
        super.paintComponent(g);
        g.drawImage(background_image, 0, 0, Main.FRAME_SIZE.width, Main.FRAME_SIZE.height, null);
        map.render(g);
        player.render(g);
        enemiesMan.render(g);
        projectilesMan.render(g);
    }

    public void update () {
        map.update();
        player.update();
        enemiesMan.update();
        projectilesMan.update();

        if (player.getWorldY()>Game.SCREEN.height) {
            gameThread=null; // STOP GAME THREAD
            final int MAP_LEFT_X= map.getLeftTopMapTile().getWorldX();
            final int MAP_RIGHT_X= map.getRightTopMapTile().getWorldX();
            final int PLAYER_X=player.getWorldX();
            String message="GameOver, ";
            if (PLAYER_X<MAP_LEFT_X||PLAYER_X>MAP_RIGHT_X) {
                message+="you lose";
                Main.getInstance().startGame();
            }
            if (PLAYER_X>MAP_LEFT_X&&PLAYER_X<MAP_RIGHT_X) {
                message+="you won";
                Main.getInstance().nextLevel();
            }
            //Main.getInstance().gameOver();
            Main.getInstance().pauseGame();
            JOptionPane.showMessageDialog(this, message);
        }
    }

    public static abstract sealed class GameController permits Main {
        protected void stopGame(Game g) {
            g.stop();
        }
        protected void startGame(Game g) {
            g.start();
        }
        protected void resumeGame(Game g) {
            g.resume();
        }

    }

    private void stop() {
        gameThread=null;
        remove(pause_button);
    }

    private void start() {
        running =true;
        gameThread=new Thread(this::loop);
        gameThread.start();
        add(pause_button);
    }

    private void resume() { start(); }

    @SuppressWarnings("all")
    public void loop () {
        long wait, diff, prev, curr;
        final long TARGET_FPS=60;

        while (gameThread!=null) {
            prev=System.currentTimeMillis();

            update();
            repaint();
            paintImmediately(0, 0, getWidth(), getHeight());

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

    public boolean isRunning() {
        return running;
    }
}
