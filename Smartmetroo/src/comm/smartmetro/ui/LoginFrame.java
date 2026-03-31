package comm.smartmetro.ui;

import comm.smartmetro.DAO.AdminDAO;

import javax.swing.*;
import java.awt.*;

public class LoginFrame extends JFrame {

    private JTextField emailField;
    private JPasswordField passwordField;

    public LoginFrame() {

        setTitle("Smart Metro - Admin Login");
        setSize(400, 300);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(new Color(24,32,44));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10,10,10,10);

        JLabel title = new JLabel("ADMIN LOGIN");
        title.setForeground(Color.WHITE);
        title.setFont(new Font("Segoe UI", Font.BOLD, 20));

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        panel.add(title, gbc);

        gbc.gridwidth = 1;

        // Email
        gbc.gridy = 1;
        gbc.gridx = 0;
        panel.add(createLabel("Email:"), gbc);

        gbc.gridx = 1;
        emailField = new JTextField(15);
        panel.add(emailField, gbc);

        // Password
        gbc.gridy = 2;
        gbc.gridx = 0;
        panel.add(createLabel("Password:"), gbc);

        gbc.gridx = 1;
        passwordField = new JPasswordField(15);
        panel.add(passwordField, gbc);

        // Login Button
        gbc.gridy = 3;
        gbc.gridx = 0;
        gbc.gridwidth = 2;

        JButton loginBtn = new JButton("Login");
        loginBtn.setBackground(new Color(0,123,255));
        loginBtn.setForeground(Color.WHITE);

        loginBtn.addActionListener(e -> login());

        panel.add(loginBtn, gbc);

        add(panel);
        setVisible(true);
    }

    private JLabel createLabel(String text) {
        JLabel label = new JLabel(text);
        label.setForeground(Color.WHITE);
        return label;
    }

    private void login() {

        String email = emailField.getText().trim();
        String password = new String(passwordField.getPassword());

        AdminDAO dao = new AdminDAO();

        if (dao.verifyPassword(email, password)) {

            JOptionPane.showMessageDialog(this,
                    "Login Successful!",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE);

            dispose();
            new MainFrame(email);   // pass logged in admin

        } else {
            JOptionPane.showMessageDialog(this,
                    "Invalid credentials!",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
}
