package comm.smartmetro.ui;

import comm.smartmetro.DAO.NotificationDAO;
import comm.smartmetro.DAO.TrainDAO;
import comm.smartmetro.DBTest;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class MainFrame extends JFrame {

    private JPanel sidebar;
    private JPanel header;
    private JPanel contentPanel;
    private CardLayout cardLayout;

    private StationPanel stationsPanel;
    private DashboardPanel dashboardPanel;
    private AddStationPanel addStationPanel;

    private JLabel notificationBadge;
    private JLabel clockLabel;

    private Timer simulationTimer;

    // ================= CONSTRUCTOR =================
    private String loggedInAdminEmail;

    public MainFrame(String email) {

    this.loggedInAdminEmail = email;


        setTitle("Smart Metro Dashboard");
        setSize(1200, 750);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        backgroundPanel bg = new backgroundPanel();
        bg.setLayout(new BorderLayout());
        setContentPane(bg);

        createHeader();
        createSidebar();
        createContentPanel();

        startClock();
        startTrainSimulation();

        setVisible(true);
    }

    // ================= HEADER =================
    private void createHeader() {

        header = new JPanel(new BorderLayout());
        header.setPreferredSize(new Dimension(getWidth(), 70));
        header.setOpaque(false);
        header.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        JLabel title = new JLabel("SMART METRO SYSTEM");
        title.setFont(new Font("Segoe UI", Font.BOLD, 26));
        title.setForeground(Color.WHITE);

        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 20, 5));
        rightPanel.setOpaque(false);

        // Clock
        clockLabel = new JLabel();
        clockLabel.setForeground(Color.WHITE);
        clockLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));

        // Notification
        NotificationDAO dao = new NotificationDAO();
        int unread = dao.getUnreadCount();

        notificationBadge = new JLabel(String.valueOf(unread));
        notificationBadge.setForeground(Color.WHITE);
        notificationBadge.setBackground(new Color(220, 53, 69));
        notificationBadge.setOpaque(true);
        notificationBadge.setHorizontalAlignment(SwingConstants.CENTER);
        notificationBadge.setPreferredSize(new Dimension(22,22));

        JLabel notificationIcon = new JLabel("🔔");
        notificationIcon.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 22));
        notificationIcon.setForeground(Color.WHITE);

        JPanel notificationPanel = new JPanel(new BorderLayout());
        notificationPanel.setOpaque(false);
        notificationPanel.add(notificationIcon, BorderLayout.CENTER);
        notificationPanel.add(notificationBadge, BorderLayout.EAST);

        notificationPanel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent e) {
                showNotifications();
            }
        });

        // Admin
        JPanel adminPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        adminPanel.setOpaque(false);

        JLabel adminIcon = new JLabel("👤");
        adminIcon.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 22));
        adminIcon.setForeground(Color.WHITE);

        JLabel adminName = new JLabel("Admin");
        adminName.setForeground(Color.WHITE);

        adminPanel.add(adminIcon);
        adminPanel.add(adminName);

        adminPanel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent e) {
                switchPanel("admin");

            }
        });

        rightPanel.add(clockLabel);
        rightPanel.add(notificationPanel);
        rightPanel.add(adminPanel);

        header.add(title, BorderLayout.WEST);
        header.add(rightPanel, BorderLayout.EAST);

        add(header, BorderLayout.NORTH);
    }

    // ================= SIDEBAR =================
    private void createSidebar() {

        sidebar = new JPanel();
        sidebar.setPreferredSize(new Dimension(220, getHeight()));
        sidebar.setBackground(new Color(20, 30, 45, 200));
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBorder(BorderFactory.createEmptyBorder(40, 15, 10, 10));

        JButton btnDashboard = createSidebarButton("Dashboard");
        JButton btnStations = createSidebarButton("Stations");
        JButton btnAddStation = createSidebarButton("Add Station");
        JButton btnExit = createSidebarButton("Exit");

        sidebar.add(btnDashboard);
        sidebar.add(Box.createVerticalStrut(20));
        sidebar.add(btnStations);
        sidebar.add(Box.createVerticalStrut(20));
        sidebar.add(btnAddStation);
        sidebar.add(Box.createVerticalStrut(20));
        sidebar.add(btnExit);

        btnDashboard.addActionListener(e -> switchPanel("dashboard"));

        btnStations.addActionListener(e -> {
            stationsPanel.loadStations();
            switchPanel("stations");
        });

        btnAddStation.addActionListener(e -> switchPanel("addstation"));
        btnExit.addActionListener(e -> System.exit(0));

        add(sidebar, BorderLayout.WEST);
    }

    // ================= CONTENT =================
    private void createContentPanel() {

    cardLayout = new CardLayout();

    contentPanel = new JPanel(cardLayout);   // ✅ MUST BE FIRST
    contentPanel.setOpaque(false);

    dashboardPanel = new DashboardPanel();
    stationsPanel = new StationPanel();
    addStationPanel = new AddStationPanel();
    AdminPanel adminPanel = new AdminPanel(loggedInAdminEmail);

    contentPanel.add(dashboardPanel, "dashboard");
    contentPanel.add(stationsPanel, "stations");
    contentPanel.add(addStationPanel, "addstation");
    contentPanel.add(adminPanel, "admin");

    add(contentPanel, BorderLayout.CENTER);
}


    private void switchPanel(String name) {
        cardLayout.show(contentPanel, name);
    }

    // ================= CLOCK =================
    private void startClock() {

        Timer timer = new Timer(1000, e -> {
            LocalTime now = LocalTime.now();
            clockLabel.setText(now.format(DateTimeFormatter.ofPattern("HH:mm:ss")));
        });

        timer.start();
    }

    // ================= TRAIN SIMULATION =================
    private void startTrainSimulation() {

        simulationTimer = new Timer(5000, e -> {

            new Thread(() -> {

                TrainDAO dao = new TrainDAO();
                dao.simulateTrainMovement();

                SwingUtilities.invokeLater(() -> {
                    refreshDashboard();
                    updateNotificationBadge();
                });

            }).start();

        });

        simulationTimer.start();
    }

    // ================= REFRESH DASHBOARD =================
    public void refreshDashboard() {

        Component comp = getCurrentPanel();

        if (comp instanceof DashboardPanel) {
            ((DashboardPanel) comp).refreshLiveData();
        }
    }

    private Component getCurrentPanel() {

        for (Component comp : contentPanel.getComponents()) {
            if (comp.isVisible()) return comp;
        }
        return null;
    }

    // ================= NOTIFICATIONS =================
    private void showNotifications() {

        NotificationDAO dao = new NotificationDAO();
        java.util.List<String> list = dao.getAllNotifications();

        StringBuilder sb = new StringBuilder();
        for (String msg : list) {
            sb.append("• ").append(msg).append("\n\n");
        }

        JOptionPane.showMessageDialog(
                this,
                sb.length() == 0 ? "No notifications" : sb.toString(),
                "Notifications",
                JOptionPane.INFORMATION_MESSAGE
        );

        dao.markAllAsRead();
        updateNotificationBadge();
    }

    private void updateNotificationBadge() {
        NotificationDAO dao = new NotificationDAO();
        notificationBadge.setText(String.valueOf(dao.getUnreadCount()));
    }

    // ================= ADMIN PANEL =================
    private void openAdminPanel() {

        JDialog dialog = new JDialog(this, "Admin Panel", true);
        dialog.setSize(400, 450);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new GridLayout(7,1,10,10));
        dialog.getContentPane().setBackground(new Color(36,48,64));

        JButton revenueBtn = createAdminButton("Today's Revenue");
        revenueBtn.addActionListener(e -> {
            double revenue = getTodayRevenue();
            JOptionPane.showMessageDialog(
                    this,
                    "Today's Total Revenue: ₹ " + revenue,
                    "Revenue Report",
                    JOptionPane.INFORMATION_MESSAGE
            );
        });

        JButton logoutBtn = createAdminButton("Logout");
        logoutBtn.addActionListener(e -> System.exit(0));

        dialog.add(createAdminButton("Profile"));
        dialog.add(createAdminButton("Change Password"));
        dialog.add(createAdminButton("Activate/Deactivate Station"));
        dialog.add(createAdminButton("Reports"));
        dialog.add(revenueBtn);
        dialog.add(createAdminButton("Toggle Theme"));
        dialog.add(logoutBtn);

        dialog.setVisible(true);
    }

    private JButton createAdminButton(String text) {
        JButton button = new JButton(text);
        button.setFocusPainted(false);
        button.setBackground(new Color(0,123,255));
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        return button;
    }

    private double getTodayRevenue() {

        double total = 0;

        try {
            Connection con = DBTest.getConnection();
            String sql = "SELECT SUM(revenue_today) FROM trains";
            PreparedStatement pst = con.prepareStatement(sql);
            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                total = rs.getDouble(1);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return total;
    }

    // ================= SIDEBAR BUTTON STYLE =================
    private JButton createSidebarButton(String text) {

        JButton button = new JButton(text);
        button.setMaximumSize(new Dimension(200, 45));
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setFocusPainted(false);
        button.setBackground(new Color(0,150,255));
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        button.setBorder(BorderFactory.createEmptyBorder());

        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(0,123,255));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(0,150,255));
            }
        });

        return button;
    }
    public void refreshStations() {
    if (stationsPanel != null) {
        stationsPanel.loadStations();
    }
}

}
