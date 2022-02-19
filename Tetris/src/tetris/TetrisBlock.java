package tetris;

import java.awt.*;

public class TetrisBlock {
    private int[][] shape;
    private Color color;
    private int x;
    private int y;

    public TetrisBlock(int[][] shape, Color color){
        this.shape = shape;
        this.color = color;
    }

    public void spawn(int gridWidth) {
        this.y = -this.getHeight();
        this.x = (gridWidth - this.getWidth()) / 2;
    }

    public int[][] getShape() {
        return shape;
    }


    public Color getColor() {
        return color;
    }

    public int getHeight() {
        return shape.length;
    }

    public int getWidth() {
        return shape[0].length;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void moveDown(){
        y++;
    }

    public void moveLeft(){
        x--;
    }

    public void moveRight(){
        x++;
    }

    public int getBottomEdge() {
        return this.y + this.getHeight();
    }
}
