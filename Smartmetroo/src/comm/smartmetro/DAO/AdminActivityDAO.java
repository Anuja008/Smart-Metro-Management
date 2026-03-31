package comm.smartmetro.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import comm.smartmetro.DBTest;

public class AdminActivityDAO {

    // 🔹 Insert Log
    public void logActivity(String email, String action) {

        try {
            Connection con = DBTest.getConnection();

            String sql = "INSERT INTO admin_activity_log (admin_email, action) VALUES (?, ?)";

            PreparedStatement pst = con.prepareStatement(sql);
            pst.setString(1, email);
            pst.setString(2, action);

            pst.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 🔹 Get All Logs
    public List<String> getAllLogs() {

        List<String> logs = new ArrayList<>();

        try {
            Connection con = DBTest.getConnection();

            String sql = "SELECT admin_email, action, action_time FROM admin_activity_log ORDER BY action_time DESC";

            PreparedStatement pst = con.prepareStatement(sql);
            ResultSet rs = pst.executeQuery();

            while (rs.next()) {

                String entry =
                        rs.getTimestamp("action_time") +
                        " — " +
                        rs.getString("admin_email") +
                        " — " +
                        rs.getString("action");

                logs.add(entry);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return logs;
    }
}
