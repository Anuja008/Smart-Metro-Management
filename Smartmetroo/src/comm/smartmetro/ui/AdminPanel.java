package comm.smartmetro.ui;

import comm.smartmetro.DBTest;

import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime;
import comm.smartmetro.DAO.AdminDAO;
import comm.smartmetro.DAO.AdminActivityDAO;



public class AdminPanel extends JPanel {
    private String loggedInAdminEmail;


    private CardLayout cardLayout;
    private JPanel contentPanel;

    public AdminPanel(String email) {
    this.loggedInAdminEmail = email;

        setLayout(new BorderLayout());
        setBackground(new Color(18, 25, 35));

        add(createSidebar(), BorderLayout.WEST);
        add(createContentArea(), BorderLayout.CENTER);
    }

    // ================= SIDEBAR =================
    private JPanel createSidebar() {

    JPanel sidebar = new JPanel();
    sidebar.setPreferredSize(new Dimension(220, getHeight()));
    sidebar.setBackground(new Color(24, 32, 44));
    sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
    sidebar.setBorder(BorderFactory.createEmptyBorder(40, 20, 20, 20));

    sidebar.add(createMenuButton("User Profile", "user"));
    sidebar.add(Box.createVerticalStrut(15));

    sidebar.add(createMenuButton("Admin Profile", "admin"));
    sidebar.add(Box.createVerticalStrut(15));

    sidebar.add(createMenuButton("Change Password", "password"));
    sidebar.add(Box.createVerticalStrut(15));

    sidebar.add(createMenuButton("Activity Log", "activity"));
    sidebar.add(Box.createVerticalStrut(15));

    sidebar.add(createMenuButton("Logout", "logout"));

    return sidebar;
}
    
    private JButton createMenuButton(String text, String panelName) {

    JButton button = new JButton(text);
    button.setMaximumSize(new Dimension(180, 45));
    button.setAlignmentX(Component.CENTER_ALIGNMENT);
    button.setFocusPainted(false);
    button.setBackground(new Color(0, 123, 255));
    button.setForeground(Color.WHITE);
    button.setFont(new Font("Segoe UI", Font.PLAIN, 15));
    button.setBorder(BorderFactory.createEmptyBorder());

    button.addActionListener(e -> {
        if(panelName.equals("logout")){
            System.exit(0);
        } else {
            cardLayout.show(contentPanel, panelName);
        }
    });

    return button;
}



    private JButton createMenuButton(String text) {

        JButton btn = new JButton(text);
        btn.setFocusPainted(false);
        btn.setBackground(new Color(0,123,255));
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Segoe UI", Font.PLAIN, 16));

        return btn;
    }

    // ================= CONTENT AREA =================
    private JPanel createContentArea() {

        cardLayout = new CardLayout();
        contentPanel = new JPanel(cardLayout);
        contentPanel.setBackground(new Color(18, 25, 35));

        contentPanel.add(createUserProfilePanel(), "user");
        contentPanel.add(createAdminProfilePanel(), "admin");
        contentPanel.add(createPasswordPanel(), "password");
        contentPanel.add(createActivityPanel(), "activity");


        return contentPanel;
    }

    private void switchPanel(String name) {
        cardLayout.show(contentPanel, name);
    }
    
    private JPanel createActivityPanel() {

    JPanel panel = new JPanel(new BorderLayout());
    panel.setBackground(new Color(24, 32, 44));
    panel.setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));

    JLabel title = new JLabel("Admin Activity Log");
    title.setForeground(Color.WHITE);
    title.setFont(new Font("Segoe UI", Font.BOLD, 22));

    panel.add(title, BorderLayout.NORTH);

    String[] columns = {"ID", "Admin Email", "Action", "Time"};
    javax.swing.table.DefaultTableModel model =
            new javax.swing.table.DefaultTableModel(columns, 0);

    JTable table = new JTable(model);
    table.setRowHeight(28);
    table.setBackground(new Color(36, 48, 64));
    table.setForeground(Color.WHITE);
    table.setGridColor(Color.DARK_GRAY);

    JScrollPane scroll = new JScrollPane(table);
    scroll.getViewport().setBackground(new Color(36, 48, 64));

    panel.add(scroll, BorderLayout.CENTER);

    // Load data
    loadActivityData(model);

    return panel;
}
        
        private void loadActivityData(javax.swing.table.DefaultTableModel model) {

    try {
        Connection con = DBTest.getConnection();
String sql =
    "SELECT id, admin_email, action, action_time " +
    "FROM admin_activity_log " +
    "ORDER BY action_time DESC";

        PreparedStatement pst = con.prepareStatement(sql);
        ResultSet rs = pst.executeQuery();

        while (rs.next()) {

            model.addRow(new Object[]{
                    rs.getInt("id"),
                    rs.getString("admin_email"),
                    rs.getString("action"),
                    rs.getTimestamp("action_time")
            });
        }

    } catch (Exception e) {
        e.printStackTrace();
    }
}



    // ================= USER PROFILE =================
    private JPanel createUserProfilePanel() {

    JPanel mainPanel = new JPanel(new BorderLayout());
    mainPanel.setBackground(new Color(24, 32, 44));

    JLabel title = new JLabel("User Profile");
    title.setForeground(Color.WHITE);
    title.setFont(new Font("Segoe UI", Font.BOLD, 26));
    title.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 0));

    mainPanel.add(title, BorderLayout.NORTH);

    JPanel contentPanel = new JPanel();
    contentPanel.setLayout(new GridBagLayout());
    contentPanel.setBackground(new Color(24, 32, 44));

    GridBagConstraints gbc = new GridBagConstraints();
    gbc.insets = new Insets(15, 20, 15, 20);
    gbc.anchor = GridBagConstraints.WEST;

    Font labelFont = new Font("Segoe UI", Font.PLAIN, 18);

    // Example static user (replace with DB call if needed)
    String userId = "1";
    String name = "Rahul Sharma";
    String email = "rahul@gmail.com";
    String mobile = "9876543210";

    JLabel idLabel = new JLabel("User ID: " + userId);
    idLabel.setForeground(Color.WHITE);
    idLabel.setFont(labelFont);

    JLabel nameLabel = new JLabel("Name: " + name);
    nameLabel.setForeground(Color.WHITE);
    nameLabel.setFont(labelFont);

    JLabel emailLabel = new JLabel("Email: " + email);
    emailLabel.setForeground(Color.WHITE);
    emailLabel.setFont(labelFont);

    JLabel mobileLabel = new JLabel("Mobile: " + mobile);
    mobileLabel.setForeground(Color.WHITE);
    mobileLabel.setFont(labelFont);

    gbc.gridx = 0; gbc.gridy = 0;
    contentPanel.add(idLabel, gbc);

    gbc.gridy++;
    contentPanel.add(nameLabel, gbc);

    gbc.gridy++;
    contentPanel.add(emailLabel, gbc);

    gbc.gridy++;
    contentPanel.add(mobileLabel, gbc);

    mainPanel.add(contentPanel, BorderLayout.CENTER);

    return mainPanel;
}



    // ================= ADMIN PROFILE =================
private JPanel createAdminProfilePanel() {

    JPanel mainPanel = new JPanel(new BorderLayout());
    mainPanel.setBackground(new Color(24, 32, 44));

    JLabel title = new JLabel("Admin Profile");
    title.setForeground(Color.WHITE);
    title.setFont(new Font("Segoe UI", Font.BOLD, 26));
    title.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 0));

    mainPanel.add(title, BorderLayout.NORTH);

    JPanel contentPanel = new JPanel();
    contentPanel.setLayout(new GridBagLayout());
    contentPanel.setBackground(new Color(24, 32, 44));

    GridBagConstraints gbc = new GridBagConstraints();
    gbc.insets = new Insets(15, 20, 15, 20);
    gbc.anchor = GridBagConstraints.WEST;

    Font labelFont = new Font("Segoe UI", Font.PLAIN, 18);

    // Replace with logged in admin email variable
    String adminName = "Metro Admin";
    String role = "System Administrator";
    String empId = "EMP001";
    String email = "admin@metro.com";
    String lastLogin = "2026-02-15 16:31:32";

    JLabel nameLabel = new JLabel("Admin Name: " + adminName);
    nameLabel.setForeground(Color.WHITE);
    nameLabel.setFont(labelFont);

    JLabel roleLabel = new JLabel("Role: " + role);
    roleLabel.setForeground(Color.WHITE);
    roleLabel.setFont(labelFont);

    JLabel empLabel = new JLabel("Employee ID: " + empId);
    empLabel.setForeground(Color.WHITE);
    empLabel.setFont(labelFont);

    JLabel emailLabel = new JLabel("Official Email: " + email);
    emailLabel.setForeground(Color.WHITE);
    emailLabel.setFont(labelFont);

    JLabel loginLabel = new JLabel("Last Login: " + lastLogin);
    loginLabel.setForeground(Color.WHITE);
    loginLabel.setFont(labelFont);

    gbc.gridx = 0; gbc.gridy = 0;
    contentPanel.add(nameLabel, gbc);

    gbc.gridy++;
    contentPanel.add(roleLabel, gbc);

    gbc.gridy++;
    contentPanel.add(empLabel, gbc);

    gbc.gridy++;
    contentPanel.add(emailLabel, gbc);

    gbc.gridy++;
    contentPanel.add(loginLabel, gbc);

    mainPanel.add(contentPanel, BorderLayout.CENTER);

    return mainPanel;
}


    // ================= PASSWORD CHANGE =================
private JPanel createPasswordPanel() {

    JPanel panel = new JPanel(new GridBagLayout());
    panel.setBackground(new Color(24, 32, 44));

    GridBagConstraints gbc = new GridBagConstraints();
    gbc.insets = new Insets(10,10,10,10);
    gbc.fill = GridBagConstraints.HORIZONTAL;

    JLabel oldLabel = new JLabel("Current Password:");
    oldLabel.setForeground(Color.WHITE);

    JPasswordField oldField = new JPasswordField(15);

    JLabel newLabel = new JLabel("New Password:");
    newLabel.setForeground(Color.WHITE);

    JPasswordField newField = new JPasswordField(15);

    JButton changeBtn = new JButton("Update Password");
    changeBtn.setBackground(new Color(0,123,255));
    changeBtn.setForeground(Color.WHITE);

    gbc.gridx = 0; gbc.gridy = 0;
    panel.add(oldLabel, gbc);

    gbc.gridx = 1;
    panel.add(oldField, gbc);

    gbc.gridx = 0; gbc.gridy = 1;
    panel.add(newLabel, gbc);

    gbc.gridx = 1;
    panel.add(newField, gbc);

    gbc.gridx = 1; gbc.gridy = 2;
    panel.add(changeBtn, gbc);

    changeBtn.addActionListener(e -> {

        String email = loggedInAdminEmail;
 // current logged admin
        String oldPass = new String(oldField.getPassword());
        String newPass = new String(newField.getPassword());

        AdminDAO dao = new AdminDAO();

        if (!dao.verifyPassword(email, oldPass)) {
            JOptionPane.showMessageDialog(this,
                    "Current password incorrect!",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (newPass.length() < 6) {
            JOptionPane.showMessageDialog(this,
                    "Password must be at least 6 characters",
                    "Warning",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        boolean updated = dao.updatePassword(email, newPass);

        if (updated) {

    JOptionPane.showMessageDialog(this,
        "Password updated successfully!",
        "Success",
        JOptionPane.INFORMATION_MESSAGE);

    // 🔥 LOG ACTIVITY
                AdminActivityDAO logDAO = new AdminActivityDAO();
                logDAO.logActivity(email, "Changed account password");

                oldField.setText("");
                newField.setText("");
            }
             else {
            JOptionPane.showMessageDialog(this,
                    "Update failed!",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    });

    return panel;
}


    // ================= UI HELPERS =================
    private JPanel createCenteredCard() {

        JPanel container = new JPanel(new GridBagLayout());
        container.setBackground(new Color(18, 25, 35));

        JPanel card = new JPanel();
        card.setLayout(new GridLayout(6,1,10,10));
        card.setPreferredSize(new Dimension(450,300));
        card.setBackground(new Color(36,48,64));
        card.setBorder(BorderFactory.createEmptyBorder(30,30,30,30));

        container.add(card);

        return card;
    }

    private JPanel wrapCard(JPanel card, String titleText) {

        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setBackground(new Color(18, 25, 35));
        wrapper.setBorder(BorderFactory.createEmptyBorder(40,40,40,40));

        JLabel title = new JLabel(titleText);
        title.setForeground(Color.WHITE);
        title.setFont(new Font("Segoe UI", Font.BOLD, 22));

        wrapper.add(title, BorderLayout.NORTH);
        wrapper.add(card, BorderLayout.CENTER);

        return wrapper;
    }
}
