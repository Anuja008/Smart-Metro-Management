package comm.smartmetro.ui;

import javax.swing.*;
import java.awt.*;

public class GradientPanel extends JPanel {

    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D) g;
        int width = getWidth();
        int height = getHeight();

        Color color1 = new Color(35, 45, 60);
        Color color2 = new Color(20, 25, 35);

        GradientPaint gp = new GradientPaint(0, 0, color1, 0, height, color2);
        g2.setPaint(gp);
        g2.fillRect(0, 0, width, height);
    }
}
