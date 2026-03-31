package comm.smartmetro.ui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;
import comm.smartmetro.DBTest;

public class StationPanel extends JPanel {

    private JTable table;
    private DefaultTableModel model;

    public StationPanel() {

        setLayout(new BorderLayout());
        setBackground(new Color(18, 28, 40));

        JLabel title = new JLabel("Stations List");
        title.setFont(new Font("Segoe UI", Font.BOLD, 28));
        title.setForeground(Color.WHITE);
        title.setBorder(BorderFactory.createEmptyBorder(20, 30, 10, 0));

        add(title, BorderLayout.NORTH);

        // TABLE COLUMNS (UPDATED)
        String[] columns = {
                "ID",
                "Name",
                "City",
                "Zone",
                "Platforms",
                "Status"
        };

        model = new DefaultTableModel(columns, 0);
        table = new JTable(model);

        table.setRowHeight(30);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

        add(scrollPane, BorderLayout.CENTER);

        JButton deleteBtn = new JButton("Delete Selected");
        deleteBtn.setBackground(new Color(220, 53, 69));
        deleteBtn.setForeground(Color.WHITE);
        deleteBtn.setFocusPainted(false);
        deleteBtn.setFont(new Font("Segoe UI", Font.BOLD, 14));

        JPanel bottom = new JPanel();
        bottom.setBackground(new Color(18, 28, 40));
        bottom.add(deleteBtn);

        add(bottom, BorderLayout.SOUTH);

        deleteBtn.addActionListener(e -> deleteStation());

        loadStations();
        
        Window window = SwingUtilities.getWindowAncestor(this);
if (window instanceof MainFrame) {
    ((MainFrame) window).refreshStations();
}

    }

    // ================= LOAD DATA =================

    public void loadStations() {

        model.setRowCount(0);

        try {
            Connection con = DBTest.getConnection();

            String sql = "SELECT station_id, station_name, city, zone, platforms, status FROM stations";

            PreparedStatement pst = con.prepareStatement(sql);
            ResultSet rs = pst.executeQuery();

            while (rs.next()) {

                Object[] row = {
                        rs.getInt("station_id"),
                        rs.getString("station_name"),
                        rs.getString("city"),
                        rs.getString("zone"),
                        rs.getInt("platforms"),
                        rs.getString("status")
                };

                model.addRow(row);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ================= DELETE =================

    private void deleteStation() {

        int selectedRow = table.getSelectedRow();

        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this,
                    "Please select a station!",
                    "Warning",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        int stationId = (int) model.getValueAt(selectedRow, 0);

        try {
            Connection con = DBTest.getConnection();

            String sql = "DELETE FROM stations WHERE station_id=?";
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setInt(1, stationId);

            pst.executeUpdate();

            JOptionPane.showMessageDialog(this,
                    "Station deleted successfully!",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE);

            loadStations();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    
}
