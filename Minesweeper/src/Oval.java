import java.awt.*;

public class Oval extends Shape {
    Integer width;
    Integer height;
    Color color;


    public Oval(Integer posX, Integer posY, Integer width, Integer height, Color color) {
        super(posX, posY);
        this.width = width;
        this.height = height;
        this.color = color;
    }

    public void fill(Graphics g) {
        g.setColor(this.color);
        g.fillOval(this.posX, this.posY, this.width, this.height);
    }

    public void draw(Graphics g) {
        g.setColor(this.color);
        g.drawOval(this.posX, this.posY, this.width, this.height);
    }

}
