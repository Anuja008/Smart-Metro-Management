package comm.smartmetro.ui;

import javax.swing.*;
import java.awt.*;

public class GlassPanel extends JPanel {

    public GlassPanel() {
        setOpaque(false);
    }

    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();

        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);

        g2.setColor(new Color(255, 255, 255, 180));
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), 30, 30);

        g2.setColor(new Color(255, 255, 255, 100));
        g2.drawRoundRect(0, 0, getWidth()-1, getHeight()-1, 30, 30);

        g2.dispose();

        super.paintComponent(g);
    }
}
