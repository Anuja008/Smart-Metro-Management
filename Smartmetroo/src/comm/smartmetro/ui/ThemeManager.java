package comm.smartmetro.ui;

import java.awt.*;

public class ThemeManager {

    public static boolean darkMode = true;

    public static Color SIDEBAR_BG = new Color(23, 30, 40);
    public static Color CONTENT_BG = new Color(240, 242, 245);
    public static Color CARD_BG = Color.WHITE;
    public static Color ACCENT = new Color(0, 123, 255);

    public static void enableDarkMode() {
        darkMode = true;

        SIDEBAR_BG = new Color(18, 22, 28);
        CONTENT_BG = new Color(30, 36, 45);
        CARD_BG = new Color(40, 48, 60);
        ACCENT = new Color(0, 153, 255);
    }

    public static void enableLightMode() {
        darkMode = false;

        SIDEBAR_BG = new Color(23, 30, 40);
        CONTENT_BG = new Color(240, 242, 245);
        CARD_BG = Color.WHITE;
        ACCENT = new Color(0, 123, 255);
    }
}
