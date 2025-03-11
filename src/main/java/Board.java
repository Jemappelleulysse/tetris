import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Random;


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
        board = new Grain[WIDTH/GRAIN_SIZE][HEIGHT/GRAIN_SIZE];
        marked = new boolean[WIDTH/GRAIN_SIZE][HEIGHT/GRAIN_SIZE];
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
        Random random = new Random();
        if (board[i][j].doDelete == 0) {
            if (j < HEIGHT/GRAIN_SIZE-1 && board[i][j+1] == null) {
                moveGrainFalling(i, j, 0, 1);
            } else if (j < HEIGHT/GRAIN_SIZE-1 && i > 0 && i < WIDTH/GRAIN_SIZE-1 && board[i+1][j] == null && board[i-1][j] == null) {
                int l = checkVoid(i,j,true);
                int r = checkVoid(i,j,false);
                if (l > 0 && r > 0) {

                    if (l > r) {
                        moveGrainFalling(i, j, -1, 1);
                    } else if (r > l) {
                        moveGrainFalling(i, j, 1, 1);
                    } else {
                        moveGrainFalling(i, j, (random.nextInt(2) == 0) ? -1 : 1, 1);
                    }
                } else if (r > 0) {
                    if (r > 2) {
                        moveGrainFalling(i,j,1,1);
                    } else {
                        int ra = random.nextInt(2);
                        if (ra ==0) {
                            moveGrainFalling(i, j, 1, 1);
                        }
                    }
                } else if (l > 0) {
                    if (l > 2) {
                        moveGrainFalling(i,j,-1,1);
                    } else {
                        int ra = random.nextInt(2);
                        if (ra == 0) {
                            moveGrainFalling(i, j, -1, 1);
                        }
                    }
                } else {
                    board[i][j].falling = false;
                }

            } else if (i > 0 && board[i-1][j] == null) {
                if (j < HEIGHT/GRAIN_SIZE-1 && board[i-1][j+1] == null) {
                    moveGrainFalling(i, j, -1, 1);
                } else {
                    board[i][j].falling = false;
                }
            } else if (i < WIDTH/GRAIN_SIZE-1 && board[i+1][j] == null) {
                if (j < HEIGHT/GRAIN_SIZE-1 && board[i+1][j+1] == null) {
                    moveGrainFalling(i, j, 1, 1);
                } else {
                    board[i][j].falling = false;
                }
            } else {
                board[i][j].falling = false;
            }
        } else if (board[i][j].doDelete > 50) {
            board[i][j].doDelete = 0;
            board[i][j] = null;
        } else if (board[i][j].doDelete > 0) {
            board[i][j].doDelete +=2;
            board[i][j].color = Color.black;
        }
    }


    public static void moveGrainFalling(int i, int j, int x, int y) {
        if (x > 0) {
            board[i][j].move(Grain.Moves.RIGHT);
        } else if (x < 0) {
            board[i][j].move(Grain.Moves.LEFT);
        }
        if (y > 0) {
            board[i][j].move(Grain.Moves.DOWN);
        } else if (y < 0) {
            board[i][j].move(Grain.Moves.UP);
        }
        board[i][j].falling = true;
        board[i+x][j+y] = board[i][j];
        board[i][j] = null;
    }

    public static int checkVoid(int i, int j,boolean left) {
        if ((left && i == 0) || (!left && i == WIDTH/GRAIN_SIZE-1) || (board[i + ((left) ? -1 : 1)][j+1] != null) || (board[i + ((left) ? -1 : 1)][j] != null)) {
            return 0;
        }
        int cpt = 1;
        while (j + cpt +1 < HEIGHT/GRAIN_SIZE && board[i+((left) ? -1 : 1)][j+cpt+1] == null) {
            cpt++;
        }
        return cpt;


    }




}
