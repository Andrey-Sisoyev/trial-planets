package planets;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class Planet implements java.io.Serializable {
    public static final ConcurrentMap<Integer, Planet> planetsDB = new ConcurrentHashMap<Integer, Planet>();
    public static volatile Integer planetsIDs_seq = 2;
    public static Integer planetsIDs_seq_next() {
        planetsIDs_seq = planetsIDs_seq + 1;
        return planetsIDs_seq;
    }
    static {
        planetsDB.put(1, new Planet(1, "Земля", 0.0, "{ неприменимо }", 10.0, true));
        planetsDB.put(2, new Planet(2, "Юпитер", 10.0, "{ неизвестно }", 500.0, true));
    }

    private Integer plID;
    private String name;
    private Double distToEarth;
    private String discovererName;
    private Double diameter;
    private boolean atmosphere;

    public Planet() {
        plID = null;
        name = "{unspecified}";
        distToEarth = 10.0;
        discovererName = "{unspecified}";
        diameter = 10.0;
        atmosphere = true;
    }

    public Planet(Integer _plID, String _name, Double _distToEarth, String _discovererName, Double _diameter, boolean _atmosphere) {
        plID = _plID;
        name = _name;
        distToEarth = _distToEarth;
        discovererName = _discovererName;
        diameter = _diameter;
        atmosphere = _atmosphere;
    }

    public Integer getPlID() {
        return plID;
    }

    public void setPlID(Integer _plID) {
        plID = _plID;
    }

    public String getName() {
        return name;
    }

    public void setName(String _name) {
        name = _name;
    }

    public Double getDistToEarth() {
        return distToEarth;
    }

    public void setDistToEarth(Double _distToEarth) {
        distToEarth = _distToEarth;
    }

    public String getDiscovererName() {
        return discovererName;
    }

    public void setDiscovererName(String _discovererName) {
        discovererName = _discovererName;
    }

    public Double getDiameter() {
        return diameter;
    }

    public void setDiameter(Double _diameter) {
        diameter = _diameter;
    }

    public boolean isAtmosphere() {
        return atmosphere;
    }

    public void setAtmosphere(boolean _atmosphere) {
        atmosphere = _atmosphere;
    }

    @Override
    public String toString() {
        return "Planet{" +
                "plID=" + plID +
                ", name='" + name + '\'' +
                ", distToEarth=" + distToEarth +
                ", discovererName='" + discovererName + '\'' +
                ", diameter=" + diameter +
                ", atmosphere=" + atmosphere +
                '}';
    }
}