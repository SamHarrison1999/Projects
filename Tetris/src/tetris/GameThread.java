package tetris;

import java.util.logging.Level;
import java.util.logging.Logger;

public class GameThread extends Thread{
    private GameArea gameArea;
    private GameForm gameForm;
    private int score;
    private int level = 1;
    private int scorePerLevel = 3;
    private int pause = 1000;
    private int speedupPerLevel = 100;

    public GameThread(GameArea gameArea, GameForm gameForm) {
        this.gameArea = gameArea;
        this.gameForm = gameForm;
    }

    @Override
    public void run() {
        while (true) {
            gameArea.spawnBlock();

            while (gameArea.moveBlockDown()) {
                try {
                    Thread.sleep(pause);
                } catch (InterruptedException exception) {
                    Logger.getLogger(GameThread.class.getName()).log(Level.SEVERE, null, exception);
                }
            }
            if (gameArea.isBlockOutOfBounds()) {
                System.out.println("Game Over");
                break;
            }
            gameArea.moveBlockToBackground();
            score += gameArea.clearLines();
            gameForm.updateScore(score);
            int lvl = score / scorePerLevel + 1;
            if (lvl > level) {
                level = lvl;
                gameForm.updateLevel(level);
                pause -= speedupPerLevel;
            }
        }
    }
}
