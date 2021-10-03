import java.awt.event.MouseEvent;

public class MouseMotionListener implements java.awt.event.MouseMotionListener {

    private Game game; // game passed through to allow for game manipulation

    public MouseMotionListener(Game game) {
        this.game = game;
    }

    @Override
    public void mouseDragged(MouseEvent event) {

    }

    @Override
    public void mouseMoved(MouseEvent event) {
        game.mouseXPosition = event.getX(); // gets the current x position of the mouse when the mouse is moved
        game.mouseYPosition = event.getY(); // gets the current y position of the mouse when the mouse is moved
    }

}

