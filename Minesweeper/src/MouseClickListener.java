import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class MouseClickListener implements MouseListener {

    private Game game; // game passed through to allow for game manipulation

    public MouseClickListener(Game game) {
        this.game = game;
    }

    @Override
    public void mouseClicked(MouseEvent event) {
        game.mouseXPosition = event.getX(); // gets the current x position of the mouse when the mouse is clicked
        game.mouseYPosition = event.getY(); // gets the current y position of the mouse when the mouse is clicked

        if (game.inBoxX() != -1 && game.inBoxY() != -1) {
            /* allows you to add and remove a flag to a square in the grid
            doesn't allow you to reveal a square in the grid with a flag on
             */
            if (game.flag == true && game.revealed[game.inBoxX()][game.inBoxY()] == false) {
                if (game.flagged[game.inBoxX()][game.inBoxY()] == false) {
                    game.flagged[game.inBoxX()][game.inBoxY()] = true;
                } else {
                    game.flagged[game.inBoxX()][game.inBoxY()] = false;
                }
            } else {
                if (game.flagged[game.inBoxX()][game.inBoxY()] == false) {
                    game.revealed[game.inBoxX()][game.inBoxY()] = true;
                }
            }
        }
        // click smiley face to reset the game
        if(game.inSmiley() == true){
            game.reset();
        }
        // click  flag
        if(game.inFlagger() == true) {
            if (game.flag == false) {
                game.flag = true;
            } else {
                game.flag = false;
            }
        }

    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }
}

