import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;
import java.util.List;

public class Game extends JFrame implements Runnable {

    // initialising variables

    private JPanel panelOne;
    Rectangle background;
    Rectangle smile1;
    Rectangle smile2;
    Rectangle smile3;
    Rectangle frown1;
    Rectangle frown2;
    Rectangle frown3;
    Rectangle flagger1;
    Rectangle flagger2;
    Rectangle flagger3;
    Rectangle flagger4;
    Rectangle flagger5;
    Rectangle flagger6;
    Oval face1;
    Oval face2;
    Oval face3;

    List<Integer> scoreList = new ArrayList<Integer>();
    List<Integer> allScores = new ArrayList<Integer>();

    public boolean reset = false;

    public boolean flag = false;

    Date startDate = new Date();
    Date endDate;

    int gridSpacing = 2;

    int neighbouringMines = 0;

    String victoryMessage = "Nothing yet!";

    public int mouseXPosition = -100;
    public int mouseYPosition = -50;

    public int smileyFaceXPosition = 230;
    public int smileyFaceYPosition = 5;

    public int smileyFaceCenterXPosition = smileyFaceXPosition + 30;
    public int smileyFaceCenterYPosition = smileyFaceYPosition + 40;

    public int flagXPosition = 5;
    public int flagYPosition = 5;

    public int flagCenterXPosition = flagXPosition + 35;
    public int flagCenterYPosition = flagYPosition + 35;

    public int timeXPosition = 425;
    public int timeYPosition = 5;

    public int victoryMessageXPosition = 170;
    public int victoryMessageYPosition = -25;

    public int time = 0;

    public boolean smile = true;

    public boolean win = false;

    public boolean lose = false;

    Random random = new Random();

    int[][] mines = new int[10][10]; // 0 if box don't contain mine or 1 if box contains mine
    int[][] neighbouringMinesArray = new int[10][10]; // 0 to 8 depending on how many of neighbouring boxes contains mines CHANGE TO NEIGHBOURING MINES
    boolean[][] revealed = new boolean[10][10]; // 1 if square has been  revealed 0 if it hasn't
    boolean[][] flagged = new boolean[10][10]; // 1 if square has been flagged 0 if it hasn't

    public void initialise(){

        // creates menu

        JMenuBar menu = new JMenuBar();

        JMenu gameMenu = new JMenu("Game");
        JMenuItem newGame = new JMenuItem("New");
        JMenuItem exitGame = new JMenuItem("Exit");
        gameMenu.add(newGame);
        gameMenu.addSeparator();
        gameMenu.add(exitGame);
        menu.add(gameMenu);

        newGame.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                reset();
            }
        });

        exitGame.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

        setJMenuBar(menu);


        // Sets squares containing mines
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                if (random.nextInt(100) < 15) {
                    mines[i][j] = 1; // if box contains mine
                } else {
                    mines[i][j] = 0; // if box doesn't contain mine
                }
                revealed[i][j] = false;
            }
        }

        // reveals number of neighbouring mines
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                neighbouringMines = 0;
                for (int m = 0; m < 10; m++) {
                    for (int n = 0; n < 10; n++) {
                        if (!(m == i && n == j)) {
                            if (isNeighbouring(i, j, m, n))
                                neighbouringMines++;
                        }
                    }
                }
                neighbouringMinesArray[i][j] = neighbouringMines;
            }
        }


        // add mouse motion event
        MouseMotionListener moveMouse = new MouseMotionListener(this);
        addMouseMotionListener(moveMouse);

        // add mouse click event
        MouseClickListener clickMouse = new MouseClickListener(this);
        addMouseListener(clickMouse);

    }

    public Game(){

        // set up game environment

        initialise();

        // standard configuration
        setTitle("Minesweeper - 1801367");
        setSize(new Dimension(515, 610)); //+6 + 29 because page borders
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.background = new Rectangle(0, 0, 500, 569, Color.orange);
        this.face1 = new Oval(smileyFaceXPosition, smileyFaceYPosition, 40, 40, Color.yellow);
        this.face2 = new Oval(smileyFaceXPosition + 10, smileyFaceYPosition + 10, 5, 5, Color.black);
        this.face3 = new Oval(smileyFaceXPosition + 25, smileyFaceYPosition + 10, 5, 5, Color.black);
        this.smile1 = new Rectangle(smileyFaceXPosition + 13, smileyFaceYPosition + 25, 15, 2, Color.black);
        this.smile2 = new Rectangle(smileyFaceXPosition + 12, smileyFaceYPosition + 22, 2, 2, Color.black);
        this.smile3 = new Rectangle(smileyFaceXPosition + 27, smileyFaceYPosition + 22, 2, 2, Color.black);
        this.frown1 = new Rectangle(smileyFaceXPosition + 13, smileyFaceYPosition + 25, 15, 2, Color.black);
        this.frown2 = new Rectangle(smileyFaceXPosition + 12, smileyFaceYPosition + 28, 2, 2, Color.black);
        this.frown3 = new Rectangle(smileyFaceXPosition + 27, smileyFaceYPosition + 28, 2, 2, Color.black);
        this.flagger1 = new Rectangle(flagXPosition + 14, flagYPosition + 10, 5, 20, Color.black);
        this.flagger2 = new Rectangle(flagXPosition + 10, flagYPosition + 30, 20, 5, Color.black);
        this.flagger3 = new Rectangle(flagXPosition + 14, flagYPosition + 5, 15, 15, Color.red);
        this.flagger4 = new Rectangle(flagXPosition + 14, flagYPosition + 5, 15, 15, Color.black);
        this.flagger5 = new Rectangle(flagXPosition + 15, flagYPosition + 6, 13, 13, Color.black);
        this.flagger6 = new Rectangle(flagXPosition + 16, flagYPosition + 7, 11, 11, Color.black);
        this.panelOne = new JPanel() {

            public void paintComponent(Graphics g) {
                Game.this.background.fill(g);
                for (int i = 0; i < 10; i++) {
                    for (int j = 0; j < 10; j++) {
                        // grid
                        g.setColor(Color.yellow);
                        if (revealed[i][j]) {
                            // revealed square
                            g.setColor(Color.white);
                            if (mines[i][j] == 1) {
                                // mines
                                g.setColor(Color.black);
                            }
                        }

                        // if mouse hovered over square
                        if (mouseXPosition >= gridSpacing + i * 50 && mouseXPosition < gridSpacing + i * 50 + 50 - 2 * gridSpacing && mouseYPosition >= gridSpacing + j * 50 + 50 + 26 && mouseYPosition < gridSpacing + j * 50 + 26 + 50 + 50 - 2 * gridSpacing) {
                            g.setColor(Color.blue);
                        }
                        g.fillRect(gridSpacing + i * 50, gridSpacing + j * 50 + 50, 50 - 2 * gridSpacing, 50 - 2 * gridSpacing);
                        // writes number of neighbouring mines on squares after revealing square
                        if (revealed[i][j]) {
                            g.setColor(Color.black);
                            if (mines[i][j] == 0 && neighbouringMinesArray[i][j] != 0) {
                                if (neighbouringMinesArray[i][j] == 1) {
                                    g.setColor(Color.blue);
                                } else if (neighbouringMinesArray[i][j] == 2) {
                                    g.setColor(Color.green);
                                } else if (neighbouringMinesArray[i][j] == 3) {
                                    g.setColor(Color.red);
                                } else if (neighbouringMinesArray[i][j] == 4) {
                                    g.setColor(new Color(0, 0, 128));
                                } else if (neighbouringMinesArray[i][j] == 5) {
                                    g.setColor(new Color(178, 34, 34));
                                } else if (neighbouringMinesArray[i][j] == 6) {
                                    g.setColor(new Color(72, 209, 204));
                                } else if (neighbouringMinesArray[i][j] == 8) {
                                    g.setColor(Color.darkGray);
                                }
                                g.setFont(new Font("Ariel", Font.BOLD, 40));
                                g.drawString(Integer.toString(neighbouringMinesArray[i][j]), i * 50 + 15, j * 50 + 50 + 40);
                            }
                            /* draws mines */
                            else if (mines[i][j] == 1) {
                                g.setColor(Color.red);
                                g.fillRect(i * 50 + 5 + 15, j * 50 + 50 + 15, 10, 20);
                                g.fillRect(i * 50 + 15, j * 50 + 50 + 5 + 15, 20, 10);
                                g.fillRect(i * 50 + 2 + 15, j * 50 + 50 + 2 + 15, 15, 15);
                                g.fillRect(i * 50 + 23, j * 50 + 50 + 10, 4, 30);
                                g.fillRect(i * 50 + 10, j * 50 + 50 + 23, 30, 4);
                            }
                        }

                        // draws flags

                        if (flagged[i][j]) {
                            g.setColor(Color.black);
                            g.fillRect(i * 50 + 20 + 3, j * 50 + 50 + 15, 5, 20);
                            g.fillRect(i * 50 + 15, j * 50 + 50 + 35, 20, 5);
                            g.setColor(Color.red);
                            g.fillRect(i * 50 + 23, j * 50 + 50 + 15, 10, 10);
                            g.setColor(Color.black);
                            g.drawRect(i * 50 + 23, j * 50 + 50 + 15, 10, 10);
                            g.drawRect(i * 50 + 24, j * 50 + 50 + 15, 10, 10);
                            g.drawRect(i * 50 + 25, j * 50 + 50 + 15, 10, 10);
                        }
                    }
                }
                // draws smiley face

                Game.this.face1.fill(g);
                Game.this.face2.fill(g);
                Game.this.face3.fill(g);
                if (smile) {
                    Game.this.smile1.fill(g);
                    Game.this.smile2.fill(g);
                    Game.this.smile3.fill(g);
                } else {
                    Game.this.frown1.fill(g);
                    Game.this.frown2.fill(g);
                    Game.this.frown3.fill(g);
                }

                // draws flagger

                Game.this.flagger1.fill(g);
                Game.this.flagger2.fill(g);
                Game.this.flagger3.fill(g);
                Game.this.flagger4.draw(g);
                Game.this.flagger5.draw(g);
                Game.this.flagger6.draw(g);

                if (flag) {
                    g.setColor(Color.red);
                }

                g.drawOval(flagXPosition, flagYPosition, 40, 40);
                g.drawOval(flagXPosition + 1, flagYPosition + 1, 39, 39);
                g.drawOval(flagXPosition + 2, flagYPosition + 2, 38, 38);

                // draws time counter

                if (!lose && !win) {
                    time = (int) ((new Date().getTime() - startDate.getTime()) / 1000);
                }
                if (time > 999) {
                    time = 999;
                }
                g.setColor(Color.white);
                if (win) {
                    g.setColor(Color.green);
                } else if (lose) {
                    g.setColor(Color.red);
                }
                g.setFont(new Font("Arial", Font.PLAIN, 40));
                if (time < 10) {
                    g.drawString("00" + Integer.toString(time), timeXPosition + 1, timeYPosition + 35);
                } else if (time < 100) {
                    g.drawString("0" + Integer.toString(time), timeXPosition + 1, timeYPosition + 35);
                } else {
                    g.drawString(Integer.toString(time), timeXPosition + 1, timeYPosition + 35);
                }

                // when game over

                if (win) {
                    g.setColor(Color.green);
                    victoryMessage = "YOU WIN";
                    int yCoordinate = 140;
                    for (int a = 0; a < scoreList.size(); a++) {
                        if (a < 5) {
                            Integer score = scoreList.get(a);
                            g.drawString(a + 1 + ". " + score.toString(), 5, yCoordinate);
                            yCoordinate += 50;
                        }
                    }
                    g.drawString(allScores.indexOf(time) + 1 + ".  " + time, 5, yCoordinate);
                } else if (lose) {
                    g.setColor(Color.red);
                    victoryMessage = "YOU LOSE";
                    g.setColor(Color.lightGray);
                    g.drawString("Top 5 Leaderboard", 80, 90);


                    int yCoordinate = 140;
                    for (int a = 0; a < scoreList.size(); a++) {
                        if (a < 5) {
                            Integer score = scoreList.get(a);
                            g.drawString(a + 1 + ". " + score.toString(), 5, yCoordinate);
                            yCoordinate += 50;
                        }
                    }
                }
                if (win || lose) {
                    g.setColor(Color.lightGray);
                    g.setFont(new Font("Arial", Font.PLAIN, 35));
                    g.drawString("PRESS SPACE TO RESTART", 15, 540);
                }

                if (win || lose) {
                    victoryMessageYPosition = -25 + (int) (new Date().getTime() - endDate.getTime()) / 10;
                    if (victoryMessageYPosition > 42) {
                        victoryMessageYPosition = 42;
                    }
                    g.setColor(Color.red);
                    g.setFont(new Font("Arial", Font.PLAIN, 35));
                    g.drawString(victoryMessage, victoryMessageXPosition, victoryMessageYPosition);
                }
            }
        };
        this.add(this.panelOne);
        setVisible(true);
        setResizable(false);

    }

    // works out the total number of mines
    public int totalMines() {
        int totalMines = 0;
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                if (mines[i][j] == 1) {
                    totalMines++;
                }
            }
        }
        return totalMines;
    }

    // works out the total number of boxes revealed
    public int totalBoxesRevealed() {
        int totalBoxesRevealed = 0;
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                if (revealed[i][j]) {
                    totalBoxesRevealed++;
                }
            }
        }
        return totalBoxesRevealed;
    }

    // resets the game
    public void reset() {

        reset = true;

        flag = false;

        startDate = new Date();

        victoryMessageYPosition = -150;

        victoryMessage = "Nothing yet!";

        smile = true;
        win = false;
        lose = false;

        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                if (random.nextInt(100) < 15) {
                    mines[i][j] = 1; // if box contains mine
                } else {
                    mines[i][j] = 0; // if box doesn't contain mine
                }
                revealed[i][j] = false;
                flagged[i][j] = false;
            }
        }

        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                neighbouringMines = 0;
                for (int m = 0; m < 10; m++) {
                    for (int n = 0; n < 10; n++) {
                        if (!(m == i && n == j)) {
                            if (isNeighbouring(i, j, m, n))
                                neighbouringMines++;
                        }
                    }
                }
                neighbouringMinesArray[i][j] = neighbouringMines;
            }
        }

        reset = false;
    }

    // checks the mouse is on the face
    public boolean inSmiley() {
        int dif = (int) Math.sqrt(Math.abs(mouseXPosition - smileyFaceCenterXPosition) * Math.abs(mouseXPosition - smileyFaceCenterXPosition) + Math.abs(mouseYPosition - smileyFaceCenterYPosition) * Math.abs(mouseYPosition - smileyFaceCenterYPosition));
        if (dif < 35) {
            return true;
        }
        return false;
    }

    // checks the mouse is on the flag
    public boolean inFlagger() {
        int dif = (int) Math.sqrt(Math.abs(mouseXPosition - flagCenterXPosition) * Math.abs(mouseXPosition - flagCenterXPosition) + Math.abs(mouseYPosition - flagCenterYPosition) * Math.abs(mouseYPosition - flagCenterYPosition));
        if (dif < 35) {
            return true;
        }
        return false;
    }

    // checks the mouse is in a square on the grid
    public int inBoxX() {
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                if (mouseXPosition >= gridSpacing + i * 50 && mouseXPosition < gridSpacing + i * 50 + 50 - 2 * gridSpacing && mouseYPosition >= gridSpacing + j * 50 + 50 + 26 && mouseYPosition < gridSpacing + j * 50 + 26 + 50 + 50 - 2 * gridSpacing) {
                    return i;
                }
            }
        }
        return -1;
    }

    // checks the mouse is in a square on the grid
    public int inBoxY() {
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                if (mouseXPosition >= gridSpacing + i * 50 && mouseXPosition < gridSpacing + i * 50 + 50 - 2 * gridSpacing && mouseYPosition >= gridSpacing + j * 50 + 50 + 26 && mouseYPosition < gridSpacing + j * 50 + 26 + 50 + 50 - 2 * gridSpacing) {
                    return j;
                }
            }
        }
        return -1;
    }

    // checks for neighbouring mines
    public boolean isNeighbouring(int mouseXPosition, int mouseYPosition, int currentXPosition, int currentYPosition) {
        if (mouseXPosition - currentXPosition < 2 && mouseXPosition - currentXPosition > -2 && mouseYPosition - currentYPosition < 2 && mouseYPosition - currentYPosition > -2 && mines[currentXPosition][currentYPosition] == 1) {
            return true;
        }
        return false;
    }

    // checks if you have won or lost
    public void victoryStatus() {
        // if you haven't lost and reveal a mine you lose
        if (!lose) {
            for (int i = 0; i < 10; i++) {
                for (int j = 0; j < 10; j++) {
                    if (revealed[i][j] && mines[i][j] == 1) {
                        lose = true;
                        smile = false;
                        endDate = new Date();
                        // if you lose
                        if (lose) {
                            // key listener to respond to key event
                            addKeyListener(new KeyboardListener(this));
                        }
                    }
                }
            }
        }

        // if you win
        if (totalBoxesRevealed() >= 100 - totalMines() && (!win && !lose)) {
            // calls score method if you win
            Score scoreObject = new Score(this);
            scoreObject.score();

            // key listener to respond to key event
            addKeyListener(new KeyboardListener(this));
        }
        // reveals the grid after you lose
        if(lose) {
            for (int x = 0; x < 10; x++) {
                for (int y = 0; y < 10; y++){
                    revealed[x][y] = true;
                }
            }
        }

    }


    public  static  void main(String[] args) {
        new Thread(new Game()).start();
    }
    @Override
    public void run() {
        while (true) {
            repaint();
            if (!reset) {
                victoryStatus(); // checks if the user has won
            }
        }
    }
}
