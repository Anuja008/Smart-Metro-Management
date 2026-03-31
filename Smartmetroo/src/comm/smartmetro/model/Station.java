package comm.smartmetro.model;

public class Station {

    private int id;
    private String name;
    private String location;
    private int platformCount;
    private String zone;

    public Station(int id, String name, String location, int platformCount, String zone) {
        this.id = id;
        this.name = name;
        this.location = location;
        this.platformCount = platformCount;
        this.zone = zone;
    }

    public Station(String name, String location, int platformCount, String zone) {
        this.name = name;
        this.location = location;
        this.platformCount = platformCount;
        this.zone = zone;
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public String getLocation() { return location; }
    public int getPlatformCount() { return platformCount; }
    public String getZone() { return zone; }

    public void setId(int id) { this.id = id; }
    
    private boolean active;

public boolean isActive() {
    return active;
}

public void setActive(boolean active) {
    this.active = active;
}

public Station(int id, String name, String location,
               int platformCount, String zone, boolean active) {

    this.id = id;
    this.name = name;
    this.location = location;
    this.platformCount = platformCount;
    this.zone = zone;
    this.active = active;
}


}
