import java.awt.*;
import java.util.ArrayList;

public class Pack {


    // On représente les packs par la position du coin haut gauche, et la listes des grains par rapport à ce coin, 0 0, 1 2, 0 6 etcc.
    // 0,0 0,1 0,2 0,3 0,4
    // 1,0 1,1 1,2 1,3 1,4
    // 2,0 2,1 2,2 2,3 2,4
    // 3,0 3,1 3,2 3,3 3,4

    int width;
    int height;
    int type;
    Grain[][] positions;

    public Pack() {
    }

    public void addGrain(Grain grain) {
    }

    public void update() {
    }

    public static Pack create4x4Pack(int x, int y, Grain.Type type) {
        Pack pack = new Pack();
        pack.width = 16;
        pack.height = 16;
        pack.positions = new Grain[16][16];
        for (int i = 0 ; i < 16 ; i++) {
            for (int j = 0 ; j < 16 ; j++) {
                pack.positions[i][j] = new Grain(type, Color.blue);
            }
        }
        return pack;
    }









}
