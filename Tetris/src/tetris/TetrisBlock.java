package tetris;

import java.awt.*;

public class TetrisBlock {
    private int[][] shape;
    private Color color;
    private int x;
    private int y;
    private int[][][] shapes;
    private int currentRotation;


    public TetrisBlock(int[][] shape, Color color){
        this.shape = shape;
        this.color = color;
        initShapes();
    }

    private void initShapes(){
        shapes = new int[4][][];
        for (int i = 0; i < 4; i++){
            int row = shape[0].length;
            int col = shape.length;
            shapes[i] = new int[row][col];
            for (int y = 0; y < row; y++) {
                for (int x = 0; x < col; x++) {
                    shapes[i][y][x] = shape[col - x - 1][y];
                }
            }
            shape = shapes[i];
        }
    }

    public void spawn(int gridWidth) {
        currentRotation = 0;
        shape = shapes[currentRotation];
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
        return y + getHeight();
    }

    public int getLeftEdge() {
        return x;
    }

    public int getRightEdge() {
        return x + getWidth();
    }

    public void rotate(){
        currentRotation++;
        if (currentRotation > 3) currentRotation = 0;
        shape = shapes[currentRotation];
    }
}
