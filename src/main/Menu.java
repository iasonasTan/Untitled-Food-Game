package main;

import javax.swing.*;
import java.awt.*;

public final class Menu extends JPanel {
    // logic
    private final Main main;

    // gui
    private final JButton startGame_button=new JButton("play"),
            quitApplication_button =new JButton("exit app");

    public Menu (Main main) {
        this.main=main;
        initUI();
    }

    private void initUI () {
        setLayout(new FlowLayout());
        setBackground(new Color(44, 44, 44, 190));

        add(startGame_button);
        add(quitApplication_button);

        for (Component c: getComponents()) {
            c.setFocusable(false);
        }

        startGame_button.addActionListener(e -> main.startGame());
        quitApplication_button.addActionListener(e -> System.exit(0));
    }

}
