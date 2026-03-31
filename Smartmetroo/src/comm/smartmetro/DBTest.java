package comm.smartmetro;

import comm.smartmetro.ui.MainFrame;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBTest {

    private static final String URL = "jdbc:mysql://localhost:3306/smartmetro";
    private static final String USER = "root";
    private static final String PASSWORD = "@mysql";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
