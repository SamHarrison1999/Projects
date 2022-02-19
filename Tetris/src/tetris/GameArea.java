package tetris;

import java.awt.*;
import javax.swing.*;

public class GameArea extends JPanel {
    
    private int gridRows;
    private int gridColumns;
    private int gridCellSize;

    private TetrisBlock block;

    
    public GameArea(JPanel placeholder, int columns){
        placeholder.setVisible(false);
        this.setBounds(placeholder.getBounds());
        this.setBackground(placeholder.getBackground());
        this.setBorder(placeholder.getBorder());
        gridColumns = columns;
        gridCellSize = this.getBounds().width / gridColumns;
        gridRows = this.getBounds().height / gridCellSize;
        spawnBlock();
    }

    public void spawnBlock(){
        block = new TetrisBlock( new int[][]{{1,0},{1,0},{1,1}}, Color.blue);
        this.block.spawn(this.gridColumns);
    }

    public boolean moveBlockDown() {
        if (!this.checkBottom()) return false;
        this.block.moveDown();
        this.repaint();
        return true;
    }

    private boolean checkBottom() {
        return this.block.getBottomEdge() != this.gridRows;
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
                    g.setColor(color);
                    g.fillRect(x, y, gridCellSize, gridCellSize);
                    g.setColor(Color.black);
                    g.drawRect(x, y, gridCellSize, gridCellSize);

                }
            }
        }
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        drawBlock(g);
    }
    
}
