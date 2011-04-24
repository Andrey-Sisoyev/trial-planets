package application.planets;

import application.planets.validator.EarthHasAtmosphere;
import home.lang.CloneableFixed;
import home.lang.RTCloneNotSupported;
import home.lang.jsr303mod.validator.Cmp;

import javax.validation.constraints.NotNull;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@EarthHasAtmosphere
public class Planet implements java.io.Serializable, CloneableFixed {
    public static final ConcurrentMap<Integer, Planet> planetsDB = new ConcurrentHashMap<Integer, Planet>();
    public static volatile Integer planetsIDs_seq = 2;
    public static Integer planetsIDs_seq_next() {
        planetsIDs_seq = planetsIDs_seq + 1;
        return planetsIDs_seq;
    }
    static {
        planetsDB.put(1, new Planet(1, "Earth", 0.0, "n/a", 10.0, true));
        planetsDB.put(2, new Planet(2, "Jupiter", 10.0, "{ unknown }", 500.0, true));
    }
    // ===========================
    // VALIDATORS-GROUPS
    public interface Persistent {}

    // ===========================
    // NON-STATIC STUFF
    @NotNull(message = "{planet.plID.notNull}", groups = { Persistent.class })
    private Integer plID;
    @NotNull
    private String name;
    @NotNull(message = "{planet.distToEarth.notNull}")
    @Cmp(value = 0, prop_rel_cnstr = Cmp.REL.GT_EQ)
    private Double distToEarth;
    @NotNull(message = "{planet.discovererName.notNull}")
    private String discovererName;
    @NotNull(message = "{planet.diameter.notNull}")
    @Cmp(value = 0, prop_rel_cnstr = Cmp.REL.GT)
    private Double diameter;
    @NotNull(message = "{planet.name.notNull}")
    private boolean atmosphere;

    // ===========================
    // CONSTRUCTORS
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

    // ===========================
    // GETTERS/SETTERS
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

    // ================================
    // METHODS

    // ================================
    // LOW-LEVEL OVERRIDES
    @Override
    public Planet clone() {
        try { return (Planet) super.clone(); } catch (CloneNotSupportedException e) { throw new RTCloneNotSupported(e); }
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