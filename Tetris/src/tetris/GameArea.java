package tetris;

import java.awt.*;
import javax.swing.*;

public class GameArea extends JPanel {
    
    private int gridRows;
    private int gridColumns;
    private int gridCellSize;
    private Color[][] background;

    private TetrisBlock block;

    
    public GameArea(JPanel placeholder, int columns){
        placeholder.setVisible(false);
        this.setBounds(placeholder.getBounds());
        this.setBackground(placeholder.getBackground());
        this.setBorder(placeholder.getBorder());
        gridColumns = columns;
        gridCellSize = this.getBounds().width / gridColumns;
        gridRows = this.getBounds().height / gridCellSize;
        background = new Color[gridRows][gridColumns];
    }

    public void spawnBlock(){
        block = new TetrisBlock( new int[][]{{1,0},{1,0},{1,1}}, Color.blue);
        this.block.spawn(this.gridColumns);
    }

    public boolean moveBlockDown() {
        if (!this.checkBottom()) {
            moveBlockToBackground();
            return false;
        }
        this.block.moveDown();
        repaint();
        return true;
    }

    public void moveBlockLeft(){
        if (!checkLeft()) return;
        block.moveLeft();
        repaint();
    }

    public void moveBlockRight(){
        if (!checkRight()) return;
        block.moveRight();
        repaint();
    }

    public void dropBlock(){
        while (checkBottom()) {
            block.moveDown();
        }
        repaint();
    }

    public void rotateBlock(){
        block.rotate();
        repaint();
    }

    private boolean checkBottom() {
        if (block.getBottomEdge() == gridRows) return false;
        int[][] shape = block.getShape();
        int width = block.getWidth();
        int height = block.getHeight();
        for (int col = 0; col < width; col++) {
            for (int row = height - 1; row >= 0; row--) {
                if (shape[row][col] != 0) {
                    int x = col + block.getX();
                    int y = row + block.getY() + 1;
                    if (y < 0) break;
                    if (background[y][x] != null) return false;
                    break;
                }
            }
        }
        return true;
    }

    private boolean checkLeft() {
        if (block.getLeftEdge() == 0) return false;
        int[][] shape = block.getShape();
        int width = block.getWidth();
        int height = block.getHeight();
        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                if (shape[row][col] != 0) {
                    int x = col + block.getX() - 1;
                    int y = row + block.getY();
                    if (y < 0) break;
                    if (background[y][x] != null) return false;
                    break;
                }
            }
        }
        return true;
    }

    private boolean checkRight() {
        if (block.getRightEdge() == gridColumns) return false;
        int[][] shape = block.getShape();
        int width = block.getWidth();
        int height = block.getHeight();
        for (int row = 0; row < height; row++) {
            for (int col = width - 1; col >= 0; col--) {
                if (shape[row][col] != 0) {
                    int x = col + block.getX() + 1;
                    int y = row + block.getY();
                    if (y < 0) break;
                    if (background[y][x] != null) return false;
                    break;
                }
            }
        }
        return true;
    }

    private void moveBlockToBackground(){
        int[][] shape = block.getShape();
        int height = block.getHeight();
        int width = block.getWidth();
        int xPos = block.getX();
        int yPos = block.getY();
        Color color = block.getColor();
        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                if (shape[row][col] == 1) {
                    background[row + yPos][col + xPos] = color;
                }
            }
        }
    }

    private void drawBlock(Graphics g){
        int height = block.getHeight();
        int width = block.getWidth();
        int[][] shape = block.getShape();
        Color color = block.getColor();
        for (int row = 0; row < height; row ++) {
            for (int col = 0; col < width; col++) {
                if (shape[row][col] == 1) {
                    int x = (block.getX() + col) * gridCellSize;
                    int y = (block.getY() + row) * gridCellSize;
                    drawGridSquare(g, color, x, y);
                }
            }
        }
    }

    private void drawBackground(Graphics g) {
        Color color;
        for (int row = 0; row < gridRows; row++) {
            for (int col = 0; col < gridColumns; col++) {
                color = background[row][col];
                if (color != null) {
                    int x = col * gridCellSize;
                    int y = row * gridCellSize;
                    drawGridSquare(g, color, x, y);
                }
            }
        }
    }

    private void drawGridSquare(Graphics g, Color color, int x, int y){
        g.setColor(color);
        g.fillRect(x, y, gridCellSize, gridCellSize);
        g.setColor(Color.black);
        g.drawRect(x, y, gridCellSize, gridCellSize);
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        drawBackground(g);
        drawBlock(g);
    }
    
}
