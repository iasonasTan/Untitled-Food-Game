package main;

import game.handler.MapHandler;

import javax.swing.*;
import java.awt.*;
import java.io.*;

public final class Main extends Game.GameController {
    private MainWindow window;
    public Game game;
    public Menu menu;
    public static final Dimension FRAME_SIZE=new Dimension(1000,800);
    private static final String winName="Untitled Food Game";
    private static Main instance;
    private int currentLevel;

    public static Main getInstance() { return instance; }
    public MainWindow getWindow() { return window; }

    public Main () {
        currentLevel=Storage.readFromFile();
    }

    public static class Storage {
        public static final String appData_str=System.getProperty("user.home")+"/.config/Untitled_Food_Game_Data";
        public static final String dataFile_str=appData_str+"/gameData.d";

        static {
            try {
                File file=new File(appData_str);
                if (!file.exists()) {
                    file.mkdir();
                }
                file=new File(dataFile_str);
                if (!file.exists()) {
                    file.createNewFile();
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        public static int readFromFile () {
            int out=1; // default level
            try (FileInputStream fis=new FileInputStream(dataFile_str)) {
                byte[] bytes=fis.readAllBytes();
                String outStr=new String(bytes);
                if (!outStr.isEmpty()) {
                    out = Integer.parseInt(outStr.replaceAll("[^0-9]", ""));
                }

            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            return out;
        }

        public static boolean saveLevelToFile (int val) {
            try (FileOutputStream fos=new FileOutputStream(dataFile_str)) {
                fos.write((""+val).getBytes());
                return true;
            } catch (FileNotFoundException e) {
                System.out.println("File not found!");
                System.out.println("Error: "+e);
                return false;
            } catch (IOException e) {
                System.out.println("Error: "+e);
                return false;
            }
        }
    }

    public void nextLevel () {
        currentLevel++;
        if (currentLevel>MapHandler.MAPS) {
            JOptionPane.showMessageDialog(game, "Game is complete");
            currentLevel=1;
        }
        Storage.saveLevelToFile(currentLevel);
        startGame();
    }

    void initComponents(){
        menu=new Menu();
        window=new MainWindow();
    }

    public final class MainWindow extends JFrame {
        public MainWindow() {
            setTitle(winName);
            setContentPane(menu);
            setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
            setLocationRelativeTo(null);
            setResizable(false);
            setSize(FRAME_SIZE);
            setVisible(true);
        }

        void gameOver () {
            menu.gameOver(true);
            pause();
        }

        void pause() {
            setTitle(winName);
            setLayout(null);
            add(menu);
            revalidate();
            repaint();
        }

        void game() {
            menu.gameOver(false);
            remove(menu);
            setLevel(currentLevel);
            setContentPane(game);
            revalidate();
            repaint();
        }

        void setLevel(int lev) {
            setTitle(winName+" stage: "+lev);
        }
    }

    public void gameOver () {
        menu.big();
        window.gameOver();
        stopGame(game);
        game=null;
    }

    public void pauseGame() {
        stopGame(game);
        menu.small();
        window.pause();
    }

    public void resumeGame () {
        window.game();
        resumeGame(game);
    }

    public void startGame() {
        game=new Game();
        game.initGame(currentLevel);
        window.game();
        startGame(game);
    }

    public static void main(String[] args) {
        instance=new Main();
        instance.initComponents();
    }
}

