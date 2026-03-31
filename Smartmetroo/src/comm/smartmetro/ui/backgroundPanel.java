package comm.smartmetro.ui;

import javax.swing.*;
import java.awt.*;

public class backgroundPanel extends JPanel {

    private Image backgroundImage;

    public backgroundPanel() {
    backgroundImage = new ImageIcon(
            getClass().getResource("/comm/smartmetro/ui/metro.jpg")
    ).getImage();

    setLayout(new BorderLayout());
}


    @Override
protected void paintComponent(Graphics g) {
    super.paintComponent(g);

    if (backgroundImage != null) {
        g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
    }

    // 🔥 Dark overlay for readability
    Graphics2D g2 = (Graphics2D) g.create();
    g2.setColor(new Color(0, 0, 0, 120)); // adjust 120 → darker/lighter
    g2.fillRect(0, 0, getWidth(), getHeight());
    g2.dispose();
}

}
