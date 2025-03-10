import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.lang.reflect.Array;
import java.util.ArrayList;



public class Board extends JPanel {

    private class Mouse extends MouseAdapter {
        @Override
        public void mouseDragged(MouseEvent e) {

            //super.mouseDragged(e);
            int x = e.getX()/GRAIN_SIZE;
            int y = e.getY()/GRAIN_SIZE;
            Grain grain = new Grain((Board.cpt == 0) ? Grain.Type.RED : Grain.Type.GREEN,(Board.cpt == 0 ) ? Color.red : Color.GREEN);
            if (x >= 0 && x < 80 && y >= 0 && y < 160)
                board[x][y] = grain;
            Window.instance.repaint();
        }
        @Override
        public void mouseClicked(MouseEvent e) {
            Board.cpt = (Board.cpt+1)%2;
        }
    }

    private class Keyboard extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            if (e.getKeyCode() == KeyEvent.VK_SPACE) {

                parcours(0,0,board[0][0].type);
            }
        }
    }

    public static class Pair<T,U> {
        T first;
        U second;

        public Pair(T first, U second) {
            this.first = first;
            this.second = second;
        }
    }




    private static final int WIDTH = 400;
    private static final int HEIGHT = 800;
    private static final int GRAIN_SIZE = 5;


    private static int cpt = 0;


    public static Grain[][] board;
    public static boolean[][] marked;
    //public static ArrayList<Pack> packs = new ArrayList<>();


    public Board() {
        bound();
        this.setBackground(Color.LIGHT_GRAY);
        board = new Grain[80][160];
        marked = new boolean[80][160];
        this.addMouseMotionListener(new Mouse());
        this.addMouseListener(new Mouse());
    }

    public void show() {
        this.setVisible(true);
    }
    private void bound() {
        this.setBounds((Window.gWidth()-(WIDTH*5/4))/2,(Window.gHeight()-HEIGHT-40),WIDTH,HEIGHT);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        bound();
        for (int i = 0 ; i < WIDTH/GRAIN_SIZE ; i++) {
            for (int j = 0 ; j < HEIGHT/GRAIN_SIZE ; j++) {
                if (board[i][j] != null) {
                    g.setColor((board[i][j].falling) ? Color.cyan  : board[i][j].color);
                    g.fillRect(i*GRAIN_SIZE, j*GRAIN_SIZE, GRAIN_SIZE, GRAIN_SIZE);
                }
            }
        }
    }

    public static void update() {

        for (int i = WIDTH/GRAIN_SIZE-1 ; i >= 0 ; i--) {
            for (int j = HEIGHT/GRAIN_SIZE-1 ; j >= 0 ; j--) {
                if (board[i][j] != null) {
                    updategrain(i, j);
                }
            }
        }


        for (int i = WIDTH / GRAIN_SIZE - 1; i >= 0; i--) {
            for (int j = HEIGHT / GRAIN_SIZE - 1; j >= 0; j--) {
                if (board[i][j] != null && !marked[i][j] && !board[i][j].falling) {
                    Pair<Boolean, Boolean> res = parcours(i, j, board[i][j].type);
                    if (res.first && res.second) {
                        delete(i, j, board[i][j].type);
                    }
                }
            }
        }

        marked = new boolean[80][160];
        Window.instance.repaint();

    }

    public static Pair<Boolean,Boolean> parcours(int i, int j , Grain.Type type) {
        if (i < 0 || i >=  WIDTH/GRAIN_SIZE || j < 0 || j >= HEIGHT/GRAIN_SIZE || marked[i][j] || board[i][j] == null || board[i][j].doDelete > 0   || board[i][j].type != type || board[i][j].falling) {
            return new Pair<>(false,false);
        }
        marked[i][j] = true;
        Pair<Boolean,Boolean> left = parcours(i-1,j,type);
        Pair<Boolean,Boolean> right = parcours(i+1,j,type);
        Pair<Boolean,Boolean> up = parcours(i,j-1,type);
        Pair<Boolean,Boolean> down = parcours(i,j+1,type);

        return new Pair<>(left.first || right.first || up.first || down.first || i == 0 ,
                left.second || right.second || up.second || down.second || i == WIDTH/GRAIN_SIZE-1);
    }


    public static void delete(int i, int j, Grain.Type type) {
        if (i < 0 || i >=  WIDTH/GRAIN_SIZE || j < 0 || j >= HEIGHT/GRAIN_SIZE || !marked[i][j] || board[i][j] == null || board[i][j].type != type || board[i][j].doDelete > 0 ||  board[i][j].falling) {
            return;
        }
        board[i][j].doDelete = 1;
        delete(i+1,j,type);
        delete(i-1,j,type);
        delete(i,j-1,type);
        delete(i,j+1,type);
    }

    public static void updategrain(int i, int j) {
        if (board[i][j].doDelete == 0) {
            if (j < HEIGHT/GRAIN_SIZE-1 && board[i][j+1] == null) {
                board[i][j + 1] = board[i][j];
                board[i][j].falling = true;
                board[i][j+1].move(Grain.Moves.DOWN);
                board[i][j] = null;
            } else {
                board[i][j].falling = false;
            }

        } else if (board[i][j].doDelete > 50) {
            board[i][j].doDelete = 0;
            board[i][j] = null;
        } else {
            board[i][j].doDelete +=2;
            board[i][j].color = Color.black;
        }




    }




}
