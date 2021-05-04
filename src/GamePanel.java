import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Random;

public class GamePanel extends JPanel implements ActionListener {
    final BufferedImage apple = ImageIO.read(new File("D:\\Images\\apple.png"));
    final BufferedImage head = ImageIO.read(new File("D:\\Images\\snakeHead.png"));
    final BufferedImage body = ImageIO.read(new File("D:\\Images\\snakeBody.png"));

    static final int SCREEN_WIDTH = 1200;
    static final int SCREEN_HEIGHT = 600;

    static final int UNIT_SIZE = 1;
    static final int GAME_UNITS = (SCREEN_WIDTH * SCREEN_HEIGHT)/UNIT_SIZE;
    static final int DELAY = 1;


    int ballMoveX = -3;
    int ballMoveY = -4;

    int ballX = SCREEN_WIDTH/2;
    int ballY = SCREEN_HEIGHT/2;
    int redPongY;
    int bluePongY;

    boolean wPressed = false;
    boolean sPressed = false;

    boolean redWPressed = false;
    boolean redSPressed = false;

    boolean BlueUpPressed = false;
    boolean BlueDownPressed = false;

    char controlling = 'R';
    char controllerType = 'D';


    int pongHeight = 200;
    int pongWidth = 20;

    int redPongs = 0;
    int bluePongs = 0;

    boolean running = false;
    boolean justDied = false;

    int timesLooped = 0;

    Timer timer;
    Random random;

    GamePanel() throws IOException {
        random = new Random();
        this.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
        this.setBackground(Color.BLACK);
        this.setFocusable(true);
        this.addKeyListener(new MyKeyAdapter());

        startGame();
    }

    public void startGame() {
//        newApple();
        running = true;
        timer = new Timer(DELAY, this);
        timer.start();

        ballMoveX = random.nextInt(9) - 5;
        ballMoveY = random.nextInt(9) - 5;
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }

    public void draw(Graphics g) {
        if (running) {
//            for (int i = 0; i < SCREEN_HEIGHT / UNIT_SIZE; i++) {
//                g.drawLine(i * UNIT_SIZE, 0, i * UNIT_SIZE, SCREEN_HEIGHT);
//                g.drawLine(0, i * UNIT_SIZE, SCREEN_WIDTH, i * UNIT_SIZE);
//            }
            g.setColor(Color.WHITE);
            g.drawLine(SCREEN_WIDTH/2, 0, SCREEN_WIDTH/2, SCREEN_HEIGHT);
            g.fillOval(ballX, ballY, 25, 25);

            g.setColor(Color.RED);
            g.fillRect(0, redPongY, pongWidth, pongHeight);

            g.setColor(Color.BLUE);
            g.fillRect(SCREEN_WIDTH - pongWidth, bluePongY, pongWidth, pongHeight);

            justDied = false;
            drawScore(g);
        } else {
            gameOver(g);
        }
    }

    public void regenerateBall() {
        ballX = SCREEN_WIDTH/2;
        ballY = SCREEN_HEIGHT/2;

        ballMoveX = random.nextInt(9) - 5;
        ballMoveY = random.nextInt(9) - 5;


    }

    public void move() {
        ballX = ballX + ballMoveX;
        ballY = ballY + ballMoveY;
    }

//    public void checkApple() {
//        if (x[0] == appleX && y[0] == appleY) {
//            bodyParts++;
//            applesEaten++;
//            newApple();
//        }
//    }

    public void checkCollisions() {
        if (ballX < pongWidth && ballY > redPongY && ballY < redPongY + pongHeight) {
            ballMoveY = random.nextInt(6) - 3;
            ballMoveX = random.nextInt(3) - +6;
            System.out.println(ballMoveY);
            ballMoveX = 7;

        }
        if (ballX > SCREEN_WIDTH - pongWidth && ballY > bluePongY && ballY < bluePongY + pongHeight) {
            ballMoveY = random.nextInt(6) - 3;
            ballMoveX = -random.nextInt(3) - +6;
            System.out.println(ballMoveY);

        }

        if (ballX < 0) {
            bluePongs++;
            regenerateBall();
        }
        if (ballX > SCREEN_WIDTH) {
            redPongs++;
            regenerateBall();
        }
        if (ballY < 0) ballMoveY = -ballMoveY;
        if (ballY > SCREEN_HEIGHT) ballMoveY = -ballMoveY;

        if (!running) timer.stop();
    }

    public void drawScore(Graphics g) {
        g.setColor(Color.WHITE);
        g.setFont(new Font("Courier-New", Font.PLAIN, 35));
        FontMetrics metrics = getFontMetrics(g.getFont());
        g.drawString("" + redPongs, SCREEN_WIDTH/2-20, 55);
        g.setColor(Color.WHITE);
        g.setFont(new Font("Courier-New", Font.PLAIN, 35));
        FontMetrics metrics2 = getFontMetrics(g.getFont());
        g.drawString("" + bluePongs, SCREEN_WIDTH/2+20, 55);
    }

    public void gameOver(Graphics g) {
        g.setColor(Color.RED);
        g.setFont(new Font("Minecrafter", Font.BOLD, 75));
        FontMetrics metrics = getFontMetrics(g.getFont());
        g.drawString("GAme Over", (SCREEN_WIDTH - metrics.stringWidth("Game Over"))/2, SCREEN_HEIGHT/2);
        g.setColor(Color.WHITE);
        g.setFont(new Font("Helvetica", Font.PLAIN, 45));
        FontMetrics metrics2 = getFontMetrics(g.getFont());
        g.drawString("" + redPongs, (SCREEN_WIDTH - metrics2.stringWidth("" + redPongs))/2, SCREEN_HEIGHT/2);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (running) {
            move();
//            checkApple();
            checkCollisions();

            timesLooped++;

            if (timesLooped == 25) {
                timer.stop();

                timer = new Timer(DELAY-15, this);
                timer.start();
            }

            if (timesLooped == 50) {
                timer.stop();

                timer = new Timer(DELAY-30, this);
                timer.start();
            }

            if (controllerType == 'S') {
                if (wPressed) {
                    movePong(-10, 'W');
                }
                if (sPressed) {
                    movePong(10, 'W');
                }
            }
            if (controllerType == 'D') {
                if (redWPressed) movePong(-10, 'R');
                if (redSPressed) movePong(10, 'R');
                if (BlueUpPressed) movePong(-10, 'B');
                if (BlueDownPressed) movePong(10, 'B');
            }

        }
        repaint();
    }

    public void movePong(int movement, Character pong) {
        if (controllerType == 'S') {
            if (controlling == 'R') {
                if (redPongY + movement > -10 && redPongY + movement < SCREEN_HEIGHT + 10 - pongHeight)
                    redPongY += movement;
            } else {
                if (bluePongY + movement > -10 && bluePongY + movement < SCREEN_HEIGHT + 10 - pongHeight)
                    bluePongY += movement;
            }
        } else {
            if (pong == 'R') {
                if (redPongY + movement > -10 && redPongY + movement < SCREEN_HEIGHT + 10 - pongHeight)
                    redPongY += movement;
            }
            if (pong == 'B') {
                if (bluePongY + movement > -10 && bluePongY + movement < SCREEN_HEIGHT + 10 - pongHeight)
                    bluePongY += movement;
            }
        }
    }

    public class MyKeyAdapter extends KeyAdapter {

        @Override
        public void keyPressed(KeyEvent e) {
            switch (e.getKeyCode()) {
                case KeyEvent.VK_W:
                    wPressed = true;
                    redWPressed = true;
                    break;
                case KeyEvent.VK_S:
                    sPressed = true;
                    redSPressed = true;
                    break;
                case KeyEvent.VK_R:
                    regenerateBall();
                    break;
                case KeyEvent.VK_UP:
                    if (controllerType == 'D') {
                        BlueUpPressed = true;
                    }
                    break;
                case KeyEvent.VK_DOWN:
                    if (controllerType == 'D') {
                        BlueDownPressed = true;
                    }
                    break;
                case KeyEvent.VK_SPACE:
                    if (controlling == 'R') {
                        controlling = 'B';
                    } else {
                        controlling = 'R';
                    }
                    break;
                case KeyEvent.VK_ENTER:
                    if (controllerType == 'S') {
                        controllerType = 'D';
                    } else {
                        controllerType = 'S';
                    }
                    break;
                case KeyEvent.VK_1:
                    controllerType = 'S';
                    break;
                case KeyEvent.VK_2:
                    controllerType = 'D';
                    break;

            }
        }

        @Override
        public void keyReleased(KeyEvent e) {
            switch (e.getKeyCode()) {
                case KeyEvent.VK_W:
                    wPressed = false;
                    redWPressed = false;
                    break;
                case KeyEvent.VK_S:
                    sPressed = false;
                    redSPressed = false;
                    break;
                case KeyEvent.VK_UP:
                    BlueUpPressed = false;
                    break;
                case KeyEvent.VK_DOWN:
                    BlueDownPressed = false;
                    break;

            }
        }
    }
}
