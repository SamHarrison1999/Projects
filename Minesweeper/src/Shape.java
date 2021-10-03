import java.awt.*;

public abstract class Shape {
    Integer posX;
    Integer posY;

    public Shape(Integer posX, Integer posY) {
        this.posX = posX;
        this.posY = posY;
    }

    public abstract void draw(Graphics var1);
}
