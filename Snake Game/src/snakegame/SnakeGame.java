package snakegame;

import java.awt.*;
import javax.swing.*;

public class SnakeGame {
    
    public SnakeGame(int button){
        frame(button);
    }
    public void frame(int button){
        JFrame frame = new JFrame();
        frame.setSize(800,750);
        frame.setTitle("SNAKE GAME 🐍");
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        GamePanel frame2 = new GamePanel(button);
        frame2.setBackground(new Color(45,45,45 ));
        frame.add(frame2);
        frame.setVisible(true);
    }
}
