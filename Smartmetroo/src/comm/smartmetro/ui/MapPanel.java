package comm.smartmetro.ui;

import org.jxmapviewer.JXMapViewer;
import org.jxmapviewer.OSMTileFactoryInfo;
import org.jxmapviewer.viewer.*;
import java.io.File;


import javax.swing.*;
import java.awt.*;
import java.awt.geom.Point2D;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashSet;
import java.util.Set;

import comm.smartmetro.DBTest;

public class MapPanel extends JPanel {

    private JXMapViewer mapViewer;

    public MapPanel() {

        // ✅ Important for Java 21 (fix HTTP 400)
        System.setProperty("http.agent", "SmartMetroApp");

        setLayout(new BorderLayout());
        setOpaque(false);

        mapViewer = new JXMapViewer();

TileFactoryInfo info = new TileFactoryInfo(
        0, // min zoom
        17, // max zoom
        17,
        256,
        true,
        true,
        "https://tile.openstreetmap.org",
        "x",
        "y",
        "z") {

    @Override
    public String getTileUrl(int x, int y, int zoom) {
        int z = 17 - zoom;
        return this.baseURL + "/" + z + "/" + x + "/" + y + ".png";
    }
};

DefaultTileFactory tileFactory = new DefaultTileFactory(info);
tileFactory.setThreadPoolSize(8);

mapViewer.setTileFactory(tileFactory);



mapViewer.setTileFactory(tileFactory);




        // Center Nagpur
        GeoPosition nagpur = new GeoPosition(21.1458, 79.0882);
        mapViewer.setZoom(7);
        mapViewer.setAddressLocation(nagpur);

        add(mapViewer, BorderLayout.CENTER);

        // Load markers initially
        refreshTrainMarkers();
    }

    // ================= TRAIN MARKERS =================
    public void refreshTrainMarkers() {

        try {
            Connection con = DBTest.getConnection();

            String sql = "SELECT station_id, latitude, longitude FROM stations WHERE latitude IS NOT NULL AND longitude IS NOT NULL";

            PreparedStatement pst = con.prepareStatement(sql);
            ResultSet rs = pst.executeQuery();

            Set<Waypoint> waypoints = new HashSet<>();

            while (rs.next()) {
                int id = rs.getInt("station_id");
double lat = rs.getDouble("latitude");
double lon = rs.getDouble("longitude");


                waypoints.add(new DefaultWaypoint(lat, lon));
            }

            WaypointPainter<Waypoint> painter = new WaypointPainter<>();
            painter.setWaypoints(waypoints);

            mapViewer.setOverlayPainter(painter);
            mapViewer.repaint();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
