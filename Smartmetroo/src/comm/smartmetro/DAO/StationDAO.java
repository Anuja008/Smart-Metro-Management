package comm.smartmetro.DAO;

import comm.smartmetro.DBTest;
import java.sql.*;

public class StationDAO {

public boolean addStation(String name, String city, String zone, int platforms) {

    String sql = "INSERT INTO stations (station_name, city, zone, platforms) VALUES (?, ?, ?, ?)";

    try {
        Connection con = DBTest.getConnection();
        PreparedStatement pst = con.prepareStatement(sql);

        pst.setString(1, name);
        pst.setString(2, city);
        pst.setString(3, zone);
        pst.setInt(4, platforms);

        return pst.executeUpdate() > 0;

    } catch(Exception e) {
        e.printStackTrace();
    }

    return false;
}

}
