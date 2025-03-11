import java.util.ArrayList;

public class Pack {


    // On représente les packs par la position du coin haut gauche, et la listes des grains par rapport à ce coin, 0 0, 1 2, 0 6 etcc.
    // 0,0 0,1 0,2 0,3 0,4
    // 1,0 1,1 1,2 1,3 1,4
    // 2,0 2,1 2,2 2,3 2,4
    // 3,0 3,1 3,2 3,3 3,4

    private boolean right;
    private boolean left;
    int x;
    int y;
    int width;
    int height;
    int type;
    private int[][] positions;

    public Pack() {
        //grains = new ArrayList<>();
    }

    public void addGrain(Grain grain) {
        //grains.add(new Pgrain);
        if (grain.right) {
            right = true;
        }
        if (grain.left) {
            left = true;
        }
    }

    public void update() {
    }






}
