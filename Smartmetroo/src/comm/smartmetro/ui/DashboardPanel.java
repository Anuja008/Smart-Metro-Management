package comm.smartmetro.ui;

import javax.swing.*;
import java.awt.*;
import java.sql.*;
import comm.smartmetro.DBTest;

public class DashboardPanel extends JPanel {

    private JLabel totalStationsLabel;
    private JLabel totalZonesLabel;
    private JLabel passengerLabel;

    private JLabel[] trainStatusLabels = new JLabel[4];
    private MapPanel mapPanel;
    private ZoneChart zoneChart;

    public DashboardPanel() {

        setLayout(new BorderLayout(20,20));
        setBackground(new Color(24,32,44));
        setBorder(BorderFactory.createEmptyBorder(20,20,20,20));

        add(createTopStats(), BorderLayout.NORTH);
        add(createCenterSection(), BorderLayout.CENTER);

        refreshLiveData();   // Initial load
    }

    // ================= TOP STATS =================

    private JPanel createTopStats() {

        JPanel panel = new JPanel(new GridLayout(1,3,20,20));
        panel.setOpaque(false);

        totalStationsLabel = createStatCard(panel,"Total Stations",new Color(0,123,255));
        totalZonesLabel = createStatCard(panel,"Total Zones",new Color(40,167,69));
        passengerLabel = createStatCard(panel,"Passengers Today",new Color(255,193,7));

        return panel;
    }

    private JLabel createStatCard(JPanel parent,String title,Color accent){

        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(new Color(36,48,64));
        card.setBorder(BorderFactory.createEmptyBorder(20,20,20,20));

        JPanel topBar = new JPanel();
        topBar.setBackground(accent);
        topBar.setPreferredSize(new Dimension(100,5));

        JLabel titleLabel = new JLabel(title);
        titleLabel.setForeground(Color.LIGHT_GRAY);

        JLabel valueLabel = new JLabel("0");
        valueLabel.setForeground(Color.WHITE);
        valueLabel.setFont(new Font("Segoe UI",Font.BOLD,26));

        JPanel center = new JPanel(new BorderLayout());
        center.setOpaque(false);
        center.add(titleLabel,BorderLayout.NORTH);
        center.add(valueLabel,BorderLayout.CENTER);

        card.add(topBar,BorderLayout.NORTH);
        card.add(center,BorderLayout.CENTER);

        parent.add(card);
        return valueLabel;
    }

    // ================= CENTER SECTION =================

    private JPanel createCenterSection(){

        JPanel main = new JPanel(new BorderLayout(20,20));
        main.setOpaque(false);

        JPanel upper = new JPanel(new GridLayout(1,2,20,20));
        upper.setOpaque(false);

        mapPanel = new MapPanel();
        JPanel mapContainer = new JPanel(new BorderLayout());
        mapContainer.setBackground(new Color(36,48,64));
        mapContainer.add(mapPanel,BorderLayout.CENTER);

        upper.add(mapContainer);
        upper.add(createLiveStatsPanel());

        main.add(upper,BorderLayout.CENTER);

        zoneChart = new ZoneChart();
        main.add(zoneChart,BorderLayout.SOUTH);

        return main;
    }

    // ================= LIVE TRAIN PANEL =================

    private JPanel createLiveStatsPanel(){

        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(36,48,64));
        panel.setBorder(BorderFactory.createEmptyBorder(20,20,20,20));

        JLabel title = new JLabel("Live Train Statistics");
        title.setForeground(Color.WHITE);
        title.setFont(new Font("Segoe UI",Font.BOLD,18));

        JPanel list = new JPanel(new GridLayout(4,1,10,10));
        list.setOpaque(false);

        for(int i=0;i<4;i++){
            list.add(createTrainRow("Train 10"+(i+1),i));
        }

        panel.add(title,BorderLayout.NORTH);
        panel.add(list,BorderLayout.CENTER);

        return panel;
    }

    private JPanel createTrainRow(String name,int index){

        JPanel row = new JPanel(new BorderLayout());
        row.setBackground(new Color(45,60,80));
        row.setBorder(BorderFactory.createEmptyBorder(10,15,10,15));

        JLabel train = new JLabel(name);
        train.setForeground(Color.WHITE);

        JLabel status = new JLabel("Loading...");
        status.setForeground(Color.GREEN);

        trainStatusLabels[index] = status;

        row.add(train,BorderLayout.WEST);
        row.add(status,BorderLayout.EAST);

        return row;
    }

    // ================= MAIN REFRESH METHOD =================

    public void refreshLiveData(){

        refreshStats();
        refreshTrainStatuses();
        zoneChart.reload();
        mapPanel.refreshTrainMarkers();

        revalidate();
        repaint();
    }

    // ================= DATABASE REFRESH METHODS =================

    private void refreshStats(){

        try{
            Connection con = DBTest.getConnection();
            Statement st = con.createStatement();

            ResultSet rs1 = st.executeQuery("SELECT COUNT(*) FROM stations");
            if(rs1.next())
                totalStationsLabel.setText(String.valueOf(rs1.getInt(1)));

            ResultSet rs2 = st.executeQuery("SELECT COUNT(DISTINCT zone) FROM stations");
            if(rs2.next())
                totalZonesLabel.setText(String.valueOf(rs2.getInt(1)));

            ResultSet rs3 = st.executeQuery("SELECT IFNULL(SUM(count),0) FROM passengers");
            if(rs3.next())
                passengerLabel.setText(String.valueOf(rs3.getInt(1)));

        }catch(Exception e){
            e.printStackTrace();
        }
    }

    private void refreshTrainStatuses(){

        try{
            Connection con = DBTest.getConnection();
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery("SELECT status FROM trains LIMIT 4");

            int i=0;
            while(rs.next() && i<4){

                String status = rs.getString("status");
                trainStatusLabels[i].setText(status);

                if(status.equalsIgnoreCase("Running"))
                    trainStatusLabels[i].setForeground(new Color(40,167,69));
                else if(status.equalsIgnoreCase("Delayed"))
                    trainStatusLabels[i].setForeground(Color.RED);
                else
                    trainStatusLabels[i].setForeground(Color.ORANGE);

                i++;
            }

        }catch(Exception e){
            e.printStackTrace();
        }
    }

    // ================= ZONE CHART =================

    class ZoneChart extends JPanel {

        int central=0,east=0,south=0;

        public ZoneChart(){
            setPreferredSize(new Dimension(800,220));
            setBackground(new Color(36,48,64));
        }

        public void reload(){

            central=0; east=0; south=0;

            try{
                Connection con = DBTest.getConnection();
                Statement st = con.createStatement();
                ResultSet rs = st.executeQuery(
                        "SELECT zone, COUNT(*) FROM stations GROUP BY zone");

                while(rs.next()){
                    String z = rs.getString(1);
                    int c = rs.getInt(2);

                    if(z.equalsIgnoreCase("Central")) central=c;
                    if(z.equalsIgnoreCase("East")) east=c;
                    if(z.equalsIgnoreCase("South")) south=c;
                }

            }catch(Exception e){
                e.printStackTrace();
            }

            repaint();
        }

        protected void paintComponent(Graphics g){

            super.paintComponent(g);

            int max = Math.max(1,Math.max(central,Math.max(east,south)));
            int baseY = getHeight()-40;

            drawBar(g,120,central,max,new Color(0,123,255),"Central",baseY);
            drawBar(g,320,east,max,new Color(40,167,69),"East",baseY);
            drawBar(g,520,south,max,new Color(255,193,7),"South",baseY);
        }

        private void drawBar(Graphics g,int x,int value,int max,
                             Color color,String label,int baseY){

            int height = (value*120)/max;

            g.setColor(color);
            g.fillRect(x,baseY-height,80,height);

            g.setColor(Color.WHITE);
            g.drawString(label+" ("+value+")",x+5,baseY+20);
        }
    }
}
