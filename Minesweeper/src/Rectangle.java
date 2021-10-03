import java.awt.*;

public class Rectangle extends Shape {
    Integer width;
    Integer height;
    Color color;

    public Rectangle(Integer posX, Integer posY, Integer width, Integer height, Color color) {
        super(posX, posY);
        this.width = width;
        this.height = height;
        this.color = color;
    }


    public void fill(Graphics g) {
        g.setColor(this.color);
        g.fillRect(this.posX, this.posY, this.width, this.height);
    }

    public void draw(Graphics g) {
        g.setColor(this.color);
        g.drawRect(this.posX, this.posY, this.width, this.height);
    }
}
