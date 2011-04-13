package home.tries.planets.web.solution_spring.controller.command;

import home.tries.planets.service.PlanetsService;
import home.tries.planets.model.entities.Planet;
import home.utils.CRUD_Op;
import home.utils.model.EntityExistsException;
import home.utils.model.EntityExistsNotException;
import home.utils.model.NoResultException;

import java.util.Date;

public class PlanetCommand {
    // ================================
    // NON-STATIC STUFF
    private volatile Planet planet;
    private volatile CRUD_Op operation; // final, actually
    private volatile boolean committed = false;
    private volatile String modifiedTime;  // todo: Date

    // ================================
    // CONSTRUCTORS

    public PlanetCommand(Planet _planet, CRUD_Op _operation) {
        planet = _planet;
        operation = _operation;
    }

    public PlanetCommand(Integer planet_id, CRUD_Op _operation) throws NoResultException {
        this.operation = _operation;

        Planet _planet;

        switch(_operation) {
            case CREATE:
                _planet = Planet.dfltNew();
                break;
            default:
                _planet = new PlanetsService().get(planet_id, true);

        }

        this.planet = _planet;
    }

    // ================================
    // GETTERS/SETTERS

    public Planet getPlanet() {
        return planet;
    }

    public void setPlanet(Planet _planet) {
        planet = _planet;
    }

    public CRUD_Op getOperation() {
        return operation;
    }

    public void setOperation(CRUD_Op _operation) {
        operation = _operation;
    }

    public boolean isCommitted() {
        return committed;
    }

    public void setCommitted(boolean _committed) {
        committed = _committed;
    }

    // ================================
    // METHODS
    public void updateModified() {
        this.modifiedTime = (new Date()).toString();
    }

    public void doCRUD() throws EntityExistsException, EntityExistsNotException {
        this.doCRUD(new PlanetsService());
    }

    public void doCRUD(PlanetsService planetServ) throws EntityExistsException, EntityExistsNotException {
        planetServ.doCRUD(this.getOperation(), this.getPlanet());
    }

    // ================================
    // LOW-LEVEL OVERRIDES

    @Override
    public String toString() {
        return "PlanetCommand{" +
                "planet=" + planet +
                ", operation=" + operation +
                ", committed=" + committed +
                '}';
    }

    public Object getModifiedTime() {
        return modifiedTime;
    }
}
