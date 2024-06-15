package snakegame;

import java.awt.*;
import java.awt.event.*;
import java.io.InputStream;
import java.util.Random;
import javax.swing.*;

public class GamePanel extends JPanel implements ActionListener, KeyListener {
    private ImageIcon snakeBoard = new ImageIcon(getClass().getResource("snakeBoard.jpg"));
    private ImageIcon snakeRightHead = new ImageIcon(getClass().getResource("snakeRightHead.png"));
    private ImageIcon snakeLeftHead = new ImageIcon(getClass().getResource("snakeLeftHead.png"));
    private ImageIcon snakeUpHead = new ImageIcon(getClass().getResource("snakeUpHead.png"));
    private ImageIcon snakeDownHead = new ImageIcon(getClass().getResource("snakeDownHead.png"));
    private ImageIcon snakeBack = new ImageIcon(getClass().getResource("snakeBody.png"));
    private ImageIcon snakeApple = new ImageIcon(getClass().getResource("snakeApple.png"));
    private Image modifiedSnakeRightHead = snakeRightHead.getImage().getScaledInstance(25, 25, Image.SCALE_DEFAULT);
    private Image modifiedSnakeLeftHead = snakeLeftHead.getImage().getScaledInstance(25, 25, Image.SCALE_DEFAULT);
    private Image modifiedSnakeUpHead = snakeUpHead.getImage().getScaledInstance(25, 25, Image.SCALE_DEFAULT);
    private Image modifiedSnakeDownHead = snakeDownHead.getImage().getScaledInstance(25, 25, Image.SCALE_DEFAULT);
    private Image modifiedSnakeBack = snakeBack.getImage().getScaledInstance(25, 25, Image.SCALE_DEFAULT);
    private Image modifiedSnakeApple = snakeApple.getImage().getScaledInstance(27, 25, Image.SCALE_DEFAULT);
    private InputStream sound;
    private boolean left = false;
    private boolean right = true;
    private boolean up = false;
    private boolean down = false;
    private boolean out = false;
    private boolean eaten = false;
    private int snakeXlen[] = new int[700];
    private int snakeYlen[] = new int[700];
    private int moves = 0, snakeLen = 3;
    private int snakeAppleX, snakeAppleY;
    private int posX[] = new int[25];
    private int posY[] = new int[25];
    private Timer time;
    private Random random;
    int n = 0;
    int score = 0;
    static int highScore = 0;
    public GameModes modes = new GameModes();
    int button;
    GamePanel(int button) {
        this.button=button;
        time = new Timer(100, this);
        time.start();
        addKeyListener(this);
        setFocusable(true);
        setFocusTraversalKeysEnabled(true);
        random = new Random();
        for (int i = 25; i < 625; i++) {
            if (i % 25 == 0) {
                posX[n] = i;
                posY[n] = i;
                n++;
            }
        }
        newApple();
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        g.drawRect(25, 25, 725, 600);
        g.drawRect(25, 645, 725, 100);
        g.setColor(Color.RED);
        g.fillRect(25, 645, 725, 100);
        g.setColor(Color.BLACK);
        g.setFont(new Font("Arial", Font.BOLD, 30));
        g.drawString("Score : " + score, 150, 675);
        g.drawString("High Score : " + highScore, 400, 675);
        snakeBoard.paintIcon(this, g, 25, 25);
        snakeBoard.paintIcon(this, g, 330, 25);
        snakeBoard.paintIcon(this, g, 25, 253);
        snakeBoard.paintIcon(this, g, 330, 253);

        snakeRightHead = new ImageIcon(modifiedSnakeRightHead);
        snakeLeftHead = new ImageIcon(modifiedSnakeLeftHead);
        snakeUpHead = new ImageIcon(modifiedSnakeUpHead);
        snakeDownHead = new ImageIcon(modifiedSnakeDownHead);
        snakeBack = new ImageIcon(modifiedSnakeBack);
        snakeApple = new ImageIcon(modifiedSnakeApple);

        if (moves == 0) {
            snakeRightHead.paintIcon(this, g, 150, 100);
            for (int i = 1; i < snakeLen; i++) {
                snakeBack.paintIcon(this, g, 100, 100);
            }
            snakeXlen[0] = 150;
            snakeXlen[1] = 125;
            snakeXlen[2] = 100;
            snakeYlen[0] = 100;
            snakeYlen[1] = 100;
            snakeYlen[2] = 100;
        }

        if (left) {
            snakeLeftHead.paintIcon(this, g, snakeXlen[0], snakeYlen[0]);
        }
        if (right) {
            snakeRightHead.paintIcon(this, g, snakeXlen[0], snakeYlen[0]);
        }
        if (up) {
            snakeUpHead.paintIcon(this, g, snakeXlen[0], snakeYlen[0]);
        }
        if (down) {
            snakeDownHead.paintIcon(this, g, snakeXlen[0], snakeYlen[0]);
        }

        for (int i = 1; i < snakeLen; i++) {
            snakeBack.paintIcon(this, g, snakeXlen[i], snakeYlen[i]);
        }
        snakeApple.paintIcon(this, g, snakeAppleX, snakeAppleY);

        if (out) {
            g.setColor(Color.BLACK);
            g.setFont(new Font("Arial", Font.BOLD, 60));
            g.drawString("Game Over ...", 210, 250);
            g.setFont(new Font("Arial", Font.BOLD, 20));
            g.drawString("Press 'Space' to restart ", 250, 300);
        }

        g.dispose();

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        
        for (int i = snakeLen; i > 0; i--) {
            snakeXlen[i] = snakeXlen[i - 1];
            snakeYlen[i] = snakeYlen[i - 1];
        }

        if (left) {
            snakeXlen[0] -= 25;
        }
        if (right) {
            snakeXlen[0] += 25;
        }
        if (up) {
            snakeYlen[0] -= 25;
        }
        if (down) {
            snakeYlen[0] += 25;
        }

        repaint();
        
        if (button == 0) {
            crossesWalls();
        }
        if (button == 1) {
            collideWithWall();
        }
        appleEaten();
        collideWithBody();
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_SPACE && out) {
            out = false;
            restartGame();
        }
        if (e.getKeyCode() == KeyEvent.VK_LEFT && (!right)) {
            left = true;
            right = false;
            up = false;
            down = false;
            moves++;
        }
        if (e.getKeyCode() == KeyEvent.VK_RIGHT && (!left)) {
            left = false;
            right = true;
            up = false;
            down = false;
            moves++;
        }
        if (e.getKeyCode() == KeyEvent.VK_UP && (!down)) {
            left = false;
            right = false;
            up = true;
            down = false;
            moves++;
        }
        if (e.getKeyCode() == KeyEvent.VK_DOWN && (!up)) {
            left = false;
            right = false;
            up = false;
            down = true;
            moves++;
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }

    private void newApple() {
        snakeAppleX = posX[random.nextInt(24)];
        snakeAppleY = posY[random.nextInt(24)];

        for (int i = snakeLen; i >= 0; i--) {
            if (snakeAppleX == snakeXlen[i] && snakeAppleY == snakeYlen[i]) {
                newApple();
            }
        }
    }

    private void appleEaten() {
        if (snakeAppleX == snakeXlen[0] && snakeAppleY == snakeYlen[0]) {
            newApple();
            eaten = true;
            snakeLen++;
            score++;
        }
    }

    private void collideWithBody() {
        for (int i = snakeLen - 1; i > 0; i--) {
            if (snakeXlen[0] == snakeXlen[i] && snakeYlen[0] == snakeYlen[i]) {
                time.stop();
                out = true;
            }
        }
    }

    private void collideWithWall() {
        if (snakeXlen[0] == 750 || snakeXlen[0] == 0 || snakeYlen[0] == 0 || snakeYlen[0] == 625) {
            time.stop();
            out = true;

        }
    }

    private void crossesWalls() {
        if (snakeXlen[0] == 750) {
            snakeXlen[0] = 25;
        } else if (snakeXlen[0] == 0) {
            snakeXlen[0] = 725;
        } else if (snakeYlen[0] == 625) {
            snakeYlen[0] = 25;
        } else if (snakeYlen[0] == 0) {
            snakeYlen[0] = 600;
        }

    }

    private void restartGame() {
        time.start();
        moves = 0;
        snakeLen = 3;
        right = true;
        left = false;
        down = false;
        up = false;
        if (score > highScore) {
            highScore = score;
        }
        score = 0;
        newApple();
    }
}
