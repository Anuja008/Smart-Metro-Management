package comm.smartmetro.DAO;

import java.sql.*;
import comm.smartmetro.DBTest;
import comm.smartmetro.util.PasswordUtil;

public class AdminDAO {

    public boolean verifyPassword(String email, String inputPassword) {

        try {
            Connection con = DBTest.getConnection();

            String sql = "SELECT password FROM admins WHERE official_email=?";
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setString(1, email);

            ResultSet rs = pst.executeQuery();

            if (rs.next()) {

                String storedHash = rs.getString("password");
                String inputHash = PasswordUtil.hashPassword(inputPassword);

                System.out.println("Stored: " + storedHash);
                System.out.println("Input:  " + inputHash);
                System.out.println("Entered Email: " + email);
System.out.println("Entered Password: " + inputPassword);
System.out.println("Generated Hash: " + inputHash);
System.out.println("Stored Hash: " + storedHash);



                return storedHash.equals(inputHash);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    public boolean updatePassword(String email, String newPassword) {

        try {
            Connection con = DBTest.getConnection();

            String hashed = PasswordUtil.hashPassword(newPassword);

            String sql = "UPDATE admins SET password=? WHERE official_email=?";
            PreparedStatement pst = con.prepareStatement(sql);

            pst.setString(1, hashed);
            pst.setString(2, email);

            return pst.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }
    
    public boolean login(String email, String password) {

    try {
        Connection con = DBTest.getConnection();

        String sql = "SELECT password FROM admins WHERE official_email=?";
        PreparedStatement pst = con.prepareStatement(sql);
        pst.setString(1, email);

        ResultSet rs = pst.executeQuery();

        if (rs.next()) {

    String storedHash = rs.getString("password");
    String inputHash = PasswordUtil.hashPassword(password);

    // 👇 ADD THESE TWO LINES HERE
    System.out.println("Stored: " + storedHash);
    System.out.println("Input : " + inputHash);

    return storedHash.equals(inputHash);
}


    } catch (Exception e) {
        e.printStackTrace();
    }

    return false;
}

}
