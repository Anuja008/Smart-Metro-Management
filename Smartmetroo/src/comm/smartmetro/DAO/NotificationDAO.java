package comm.smartmetro.DAO;

import comm.smartmetro.DBTest;
import java.sql.*;
import java.util.*;

public class NotificationDAO {

    public int getUnreadCount() {

        int count = 0;

        try {
            Connection con = DBTest.getConnection();
            String sql = "SELECT COUNT(*) FROM notifications WHERE is_read = 0";
            PreparedStatement pst = con.prepareStatement(sql);
            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                count = rs.getInt(1);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return count;
    }

    public List<String> getAllNotifications() {

        List<String> list = new ArrayList<>();

        try {
            Connection con = DBTest.getConnection();
            String sql = "SELECT message FROM notifications ORDER BY created_at DESC";
            PreparedStatement pst = con.prepareStatement(sql);
            ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                list.add(rs.getString("message"));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    public void markAllAsRead() {

        try {
            Connection con = DBTest.getConnection();
            String sql = "UPDATE notifications SET is_read = 1";
            PreparedStatement pst = con.prepareStatement(sql);
            pst.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
