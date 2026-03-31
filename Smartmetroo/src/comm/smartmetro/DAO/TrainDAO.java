package comm.smartmetro.DAO;

import comm.smartmetro.DBTest;

import java.sql.*;
import java.util.Random;

public class TrainDAO {

    // ===============================
    // SIMULATE TRAIN MOVEMENT
    // ===============================
    public void simulateTrainMovement() {

        try (Connection con = DBTest.getConnection()) {

            // 1️⃣ Get total stations
            String stationSql = "SELECT station_id FROM stations WHERE status='Active'";
            PreparedStatement stationStmt = con.prepareStatement(stationSql);
            ResultSet stationRs = stationStmt.executeQuery();

            java.util.List<Integer> stationIds = new java.util.ArrayList<>();

            while (stationRs.next()) {
                stationIds.add(stationRs.getInt("station_id"));
            }

            if (stationIds.isEmpty()) return;

            // 2️⃣ Get all trains
            String trainSql = "SELECT train_id FROM trains";
            PreparedStatement trainStmt = con.prepareStatement(trainSql);
            ResultSet trainRs = trainStmt.executeQuery();

            Random random = new Random();

            while (trainRs.next()) {

                int trainId = trainRs.getInt("train_id");

                // Pick random station
                int randomStation =
                        stationIds.get(random.nextInt(stationIds.size()));

                // Random status
                String[] statuses = {"Running", "Stopped", "Delayed"};
                String randomStatus =
                        statuses[random.nextInt(statuses.length)];

                // Increase revenue randomly
                double revenueIncrease = random.nextInt(2000);

                String updateSql =
                        "UPDATE trains SET current_station=?, status=?, revenue_today = revenue_today + ? WHERE train_id=?";

                PreparedStatement updateStmt =
                        con.prepareStatement(updateSql);

                updateStmt.setInt(1, randomStation);
                updateStmt.setString(2, randomStatus);
                updateStmt.setDouble(3, revenueIncrease);
                updateStmt.setInt(4, trainId);

                updateStmt.executeUpdate();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ===============================
    // GET LIVE TRAIN DATA (Dashboard)
    // ===============================
    public ResultSet getAllTrains() throws SQLException {

        Connection con = DBTest.getConnection();

        String sql = "SELECT t.train_id, t.train_name, s.station_name, t.status, t.revenue_today " +
                     "FROM trains t " +
                     "LEFT JOIN stations s ON t.current_station = s.station_id";

        PreparedStatement pst = con.prepareStatement(sql);

        return pst.executeQuery();
    }

}
