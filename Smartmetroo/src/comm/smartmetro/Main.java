package comm.smartmetro;

import javax.swing.SwingUtilities;
import comm.smartmetro.ui.LoginFrame;

public class Main {

    public static void main(String[] args) {

        System.out.println("Starting Smart Metro Project...");

        SwingUtilities.invokeLater(() -> {

            LoginFrame frame = new LoginFrame();
            frame.setVisible(true);

        });
    }
}
