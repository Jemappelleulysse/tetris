import java.awt.*;
import java.util.Random;

public class Main {


    public static void main(String[] args) {
        startFrame();
        for (int i = 0 ; i < 100 ; i++) {
            Random random = new Random();
            int x = random.nextInt(80);
            int y = random.nextInt(160);
            Grain grain = new Grain(Grain.Type.RED, Color.red);
            //Board.board[x][y] = grain;
        }

        long time = System.currentTimeMillis();
        while (true) {
            time = System.currentTimeMillis() - time;
            Board.update(time);
        }

    }

    private static void startFrame() {
        Window window = new Window(800, 1000, "Hello World");
        window.show();
        Board board = new Board();
        window.add(board);
        board.requestFocusInWindow();
        board.show();
    }


}
