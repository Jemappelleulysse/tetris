import java.util.ArrayList;

public class Pack {

    private boolean right;
    private boolean left;
    private ArrayList<Grain> grains;

    public Pack() {
        grains = new ArrayList<>();
    }

    public void addGrain(Grain grain) {
        grains.add(grain);
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
