import java.awt.*;

public class Grain {

    public enum Type {
        RED, GREEN, BLUE, YELLOW
    }

    public enum Moves {
        RIGHT, LEFT, UP, DOWN
    }


    int x, y;
    Color color;
    Type type;
    boolean right;
    boolean left;
    int doDelete = 0;
    boolean falling = true;
    int packId;

    public Grain(Type type, Color color) {
        this.type = type;
        this.color = color;
        x = 0;
        y = 0;
    }

    public void move(Moves move) {
        switch (move) {
            case RIGHT:
                x++;
                break;
            case LEFT:
                x--;
                break;
            case UP:
                y--;
                break;
            case DOWN:
                y++;
                break;
        }
    }


}
