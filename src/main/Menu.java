package main;

import javax.swing.*;
import java.awt.*;
import java.lang.annotation.Inherited;

public final class Menu extends JPanel {
    // logic
    private final Main main;
    private final int MAPS=2;

    // gui
    private final JButton startGame_button=new JButton("Play"),
            quitApplication_button =new JButton("Exit Game"),
            restartGame_button=new JButton("Restart");
    private final Slider map_slider =new Slider("Map", 1, MAPS);

    public Menu (Main main) {
        this.main=main;
        initUI();
    }

    private void initUI () {
        setLayout(new FlowLayout());
        setBackground(new Color(44, 44, 44, 190));

        add(startGame_button);
        add(quitApplication_button);
        add(restartGame_button);
        add(map_slider);

        for (Component c: getComponents()) {
            c.setFocusable(false);
        }
        for (Component c: getComponents()) {
            c.setBackground(Color.LIGHT_GRAY);
        }

        startGame_button.addActionListener(e -> main.startGame(map_slider.getValue()));
        quitApplication_button.addActionListener(e -> System.exit(0));
        restartGame_button.addActionListener(e -> main.startGame(map_slider.getValue()));
    }

    private static class Slider extends JPanel {
        private final JSlider input_slider=new JSlider();
        private final JLabel text_label=new JLabel(),
                count_label=new JLabel();
        private final JPanel labels_panel=new JPanel(new FlowLayout());

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

        public int getValue () {
            return input_slider.getValue();
        }
    }

}
