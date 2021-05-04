import javax.swing.*;
import java.io.IOException;

public class GameFrame extends JFrame {

    GameFrame() throws IOException {

        GamePanel panel = new GamePanel();
        this.add(panel);

        this.setTitle("Snake!");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);
        this.pack();

        this.setVisible(true);
        this.setLocationRelativeTo(null);
    }

}
