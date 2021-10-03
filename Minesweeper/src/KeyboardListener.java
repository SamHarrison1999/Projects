import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyboardListener implements KeyListener {

    private Game game; // game passed through to allow for game manipulation

    public KeyboardListener(Game game) {
        this.game = game;
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        // Resets the game when press the spacebar after the game is finished
        if(e.getKeyCode() == KeyEvent.VK_SPACE) {
            game.reset();
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
}