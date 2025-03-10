import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import javax.imageio.ImageIO;

public class Window extends JFrame{

    private int pX, pY;

    public static JFrame instance;


    public Window(int width, int height, String title) {
        super(title);
        instance = this;
        this.setSize(width, height);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        this.setLayout(null);

        try {
            Image icon = ImageIO.read(new File("assets/icon.png"));
            this.setIconImage(icon);
        } catch (IOException e) {
            e.printStackTrace();
        }

        this.setVisible(true);
    }

    public static int gWidth() {
        return instance.getWidth();
    }

    public static int gHeight() {
        return instance.getHeight();
    }



}
