package main;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public final class Menu extends JPanel {
    // logic
    private final int PAD=50;

    // gui
    private final JButton playGame_button =new JButton("Play"),
            quitApplication_button =new JButton("Quit Game"),
            restartGame_button=new JButton("Restart");
    //private final Slider map_slider =new Slider("Map", 1, MapHandler.MAPS);
    private BufferedImage backgroundImage;

    public Menu () {
        try {
            initUI();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void paintComponent (Graphics g) {
        g.drawImage(backgroundImage, 0, 0, Main.FRAME_SIZE.width, Main.FRAME_SIZE.height, null);
        super.paintComponent(g);
    }

    @SuppressWarnings("all")
    private void initUI () throws IOException {
        setLayout(new FlowLayout());
        setBackground(new Color(44, 44, 44, 190));
        backgroundImage= ImageIO.read(getClass().getResource("/menu/background.png"));
        setBorder(BorderFactory.createLineBorder(Color.GREEN, 5));

        add(playGame_button);
        add(quitApplication_button);
        add(restartGame_button);
        //add(map_slider);

        for (Component c: getComponents()) {
            c.setFocusable(false);
        }
        for (Component c: getComponents()) {
            c.setBackground(Color.LIGHT_GRAY);
        }

        playGame_button.addActionListener(e -> {
            final Main main=Main.getInstance();
            if (main.game!=null&&main.game.isRunning()) {
                main.resumeGame();
            } else {
                //main.setCurrentLevel(map_slider.getValue());
                main.startGame();
            }
        });
        quitApplication_button.addActionListener(e -> System.exit(0));
        restartGame_button.addActionListener(e -> Main.getInstance().startGame());
    }

    public void small() {
        setBounds(PAD, PAD, Main.FRAME_SIZE.width-PAD*2,
                Main.FRAME_SIZE.height-PAD*2);
    }

    public void big() {
        setBounds(0, 0, 0, 0); // temp vals
        setPreferredSize(Main.FRAME_SIZE);
        setSize(Main.FRAME_SIZE);
    }

    public void gameOver(boolean over) {
        playGame_button.setVisible(!over);
    }

    @SuppressWarnings("all")
    private static class Slider extends JPanel {
        private final JSlider input_slider=new JSlider();
        private final JLabel text_label=new JLabel(),
                count_label=new JLabel();
        @SuppressWarnings("all")
        private final JPanel labels_panel=new JPanel(new FlowLayout());

        @SuppressWarnings("all")
        private Slider () {
            labels_panel.add(text_label);
            labels_panel.add(count_label);
            this.add(labels_panel);
            this.add(input_slider);
            input_slider.addChangeListener(e -> {
                count_label.setText(""+input_slider.getValue());
            });
            setLayout(new GridLayout(2, 1, 1, 1));
        }

        @SuppressWarnings("unused")
        public Slider (String text, int min, int max) {
            this();
            config(min, max);
            text_label.setText(text+" :");
        }

        @Override
        public void setBackground (Color clr) {
            super.setBackground(clr);
            for (Component c: getComponents()) {
                c.setBackground(clr);
            }
        }

        @Override
        public void setFocusable (boolean f) {
            super.setFocusable(f);
            for (Component c: getComponents()) {
                c.setFocusable(f);
            }
        }

        public void config (int min, int max) {
            if (min>max)
                throw new IllegalArgumentException();

            input_slider.setMinimum(min);
            input_slider.setMaximum(max);
            input_slider.setValue((min+max)/2);
        }

        public int getValue () { return input_slider.getValue(); }
    }

}
