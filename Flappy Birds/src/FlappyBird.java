import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;

public class FlappyBird implements ActionListener, MouseListener, KeyListener {

    public static FlappyBird flappyBird;
    public Renderer renderer;
    public Rectangle bird;
    public ArrayList<Rectangle> columns;
    public Random random;
    public int ticks, yMotion, score;
    public boolean gameOver, started;

    public FlappyBird() {
        JFrame jFrame = new JFrame("Flappy Bird");
        Timer timer = new Timer(20, this);
        renderer = new Renderer();
        random = new Random();
        jFrame.add(renderer);
        jFrame.setSize(new Dimension(800, 800));
        jFrame.addMouseListener(this);
        jFrame.addKeyListener(this);
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jFrame.setResizable(false);
        jFrame.setVisible(true);
        bird = new Rectangle(390, 390, 20, 20);
        columns = new ArrayList<>();
        addColumn(true);
        addColumn(true);
        addColumn(true);
        addColumn(true);
        timer.start();
    }

    public void addColumn(boolean start){
        int space = 300;
        int width = 100;
        int height = 50 + random.nextInt(300);
        if (start) {
            columns.add(new Rectangle(800 + width + columns.size() * 300, 800 - height - 120, width, height));
            columns.add(new Rectangle(800 + width + (columns.size() - 1) * 300, 0, width, 800 - height - space));
        } else {
            columns.add(new Rectangle(columns.get(columns.size() - 1).x + 600, 800 - height - 120, width, height));
            columns.add(new Rectangle(columns.get(columns.size() - 1).x, 0, width, 800 - height - space));
        }
    }

    public void paintColumn(Graphics g, Rectangle column) {
        g.setColor(Color.GREEN.darker());
        g.fillRect(column.x, column.y, column.width, column.height);
    }

    public void jump(){
        if (gameOver) {
            bird = new Rectangle(390, 390, 20, 20);
            columns.clear();
            yMotion = 0;
            score = 0;
            addColumn(true);
            addColumn(true);
            addColumn(true);
            addColumn(true);
            gameOver = false;
        }
        if (!started) {
            started = true;
        } else if (!gameOver){
            if (yMotion > 0) {
                yMotion = 0;
            }
            yMotion -= 10;
        }
    }

    /**
     * Invoked when an action occurs.
     *
     * @param e the event to be processed
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        ticks++;
        if (started && !gameOver) {
            for (Rectangle column : columns) {
                column.x -= 10;
            }
            if (ticks % 2 == 0 && yMotion < 15) {
                yMotion += 2;
            }
            for (int i = 0; i < columns.size(); i++) {
                Rectangle column = columns.get(i);
                if (column.x + column.width < 0) {
                    columns.remove(column);
                    if (column.y == 0) {
                        addColumn(false);
                    }
                }
            }
            bird.y += yMotion;
            for (Rectangle column : columns) {
                if (column.y == 0 && bird.x + bird.width / 2 > column.x + column.width / 2 - 5 && bird.x + bird.width / 2 < column.x + column.width / 2 + 5) {
                    score++;
                }
                if (column.intersects(bird)) {
                    gameOver = true;
                    if (bird.x <= column.x) {
                        bird.x = column.x - bird.width;
                    } else {
                        if (column.y != 0) {
                            bird.y = column.y - bird.height;
                        } else if (bird.y < column.height) {
                            bird.y = column.height;
                        }
                    }
                }
            }
            if (bird.y > 680 || bird.y < 0) {
                gameOver = true;
            }
            if (bird.y + yMotion >= 680) {
                bird.y = 680 - bird.height;
            }
        }
        renderer.repaint();
    }

    public void repaint(Graphics g) {
        g.setColor(Color.CYAN);
        g.fillRect(0, 0, 800, 800);
        g.setColor(Color.RED);
        g.fillRect(bird.x, bird.y, bird.width, bird.height);
        g.setColor(Color.ORANGE);
        g.fillRect(0, 680, 800 , 120);
        g.setColor(Color.GREEN);
        g.fillRect(0, 680, 800 , 20);
        for (Rectangle column : columns) {
            paintColumn(g, column);
        }
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 100));
        if (!started) {
            g.drawString("Click to start!", 75, 350);
        }
        if (gameOver) {
            g.drawString("Game Over!", 100, 350);
        }
        if (!gameOver && started) {
            g.drawString(String.valueOf(score), 375, 100);
        }
    }

    public static void main(String[] args) {
        flappyBird = new FlappyBird();
    }

    /**
     * Invoked when the mouse button has been clicked (pressed
     * and released) on a component.
     *
     * @param e the event to be processed
     */
    @Override
    public void mouseClicked(MouseEvent e) {
        jump();
    }

    /**
     * Invoked when a mouse button has been pressed on a component.
     *
     * @param e the event to be processed
     */
    @Override
    public void mousePressed(MouseEvent e) {

    }

    /**
     * Invoked when a mouse button has been released on a component.
     *
     * @param e the event to be processed
     */
    @Override
    public void mouseReleased(MouseEvent e) {

    }

    /**
     * Invoked when the mouse enters a component.
     *
     * @param e the event to be processed
     */
    @Override
    public void mouseEntered(MouseEvent e) {

    }

    /**
     * Invoked when the mouse exits a component.
     *
     * @param e the event to be processed
     */
    @Override
    public void mouseExited(MouseEvent e) {

    }

    /**
     * Invoked when a key has been typed.
     * See the class description for {@link KeyEvent} for a definition of
     * a key typed event.
     *
     * @param e the event to be processed
     */
    @Override
    public void keyTyped(KeyEvent e) {

    }

    /**
     * Invoked when a key has been pressed.
     * See the class description for {@link KeyEvent} for a definition of
     * a key pressed event.
     *
     * @param e the event to be processed
     */
    @Override
    public void keyPressed(KeyEvent e) {

    }

    /**
     * Invoked when a key has been released.
     * See the class description for {@link KeyEvent} for a definition of
     * a key released event.
     *
     * @param e the event to be processed
     */
    @Override
    public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_SPACE) {
            jump();
        }
    }
}
