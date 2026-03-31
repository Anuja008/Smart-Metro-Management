package comm.smartmetro.ui;

import javax.swing.*;
import java.awt.*;
import comm.smartmetro.DAO.StationDAO;

public class AddStationPanel extends JPanel {

    private JTextField nameField, cityField, zoneField, platformField;

    public AddStationPanel() {

        setLayout(new GridBagLayout());
        setBackground(new Color(18, 28, 40));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15,15,15,15);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel title = new JLabel("Add New Station");
        title.setFont(new Font("Segoe UI", Font.BOLD, 26));
        title.setForeground(Color.WHITE);

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        add(title, gbc);

        gbc.gridwidth = 1;

        nameField = createField();
        cityField = createField();
        zoneField = createField();
        platformField = createField();

        addRow("Station Name:", nameField, 1, gbc);
        addRow("City:", cityField, 2, gbc);
        addRow("Zone:", zoneField, 3, gbc);
        addRow("Platforms:", platformField, 4, gbc);

        JButton addBtn = new JButton("Add Station");
        addBtn.setBackground(new Color(0,123,255));
        addBtn.setForeground(Color.WHITE);
        addBtn.setFocusPainted(false);

        gbc.gridx = 1;
        gbc.gridy = 5;
        add(addBtn, gbc);

        addBtn.addActionListener(e -> addStation());
    }

    private JTextField createField() {
        JTextField field = new JTextField(20);
        field.setBackground(new Color(36,48,64));
        field.setForeground(Color.WHITE);
        field.setCaretColor(Color.WHITE);
        field.setBorder(BorderFactory.createEmptyBorder(8,8,8,8));
        return field;
    }

    private void addRow(String labelText, JTextField field, int y, GridBagConstraints gbc) {
        JLabel label = new JLabel(labelText);
        label.setForeground(Color.WHITE);
        label.setFont(new Font("Segoe UI", Font.PLAIN, 15));

        gbc.gridx = 0;
        gbc.gridy = y;
        add(label, gbc);

        gbc.gridx = 1;
        add(field, gbc);
    }

    private void addStation() {

        String name = nameField.getText().trim();
        String city = cityField.getText().trim();
        String zone = zoneField.getText().trim();
        String platformText = platformField.getText().trim();

        if(name.isEmpty() || city.isEmpty() || zone.isEmpty() || platformText.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Please fill all fields!",
                    "Warning",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        int platforms;

        try {
            platforms = Integer.parseInt(platformText);
        } catch(Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Platforms must be a number!",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        StationDAO dao = new StationDAO();
        boolean success = dao.addStation(name, city, zone, platforms);

        if(success) {
            JOptionPane.showMessageDialog(this,
                    "Station added successfully!",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE);

            nameField.setText("");
            cityField.setText("");
            zoneField.setText("");
            platformField.setText("");
        } else {
            JOptionPane.showMessageDialog(this,
                    "Failed to add station!",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
}
