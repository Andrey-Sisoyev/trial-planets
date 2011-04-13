package home.tries.planets.web.solution_jsf.command;

import home.tries.planets.model.entities.Planet;
import home.tries.planets.service.PlanetsService;
import home.tries.planets.web.solution_jsf.mbeans.ManagedPlanets;
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

    // ================================
    // CONSTRUCTORS

    public PlanetCommand(Planet _planet, CRUD_Op _operation) {
        planet = _planet;
        operation = _operation;
    }

    public PlanetCommand(Integer planet_id, CRUD_Op _operation, PlanetsService planetsService) throws NoResultException {
        this.operation = _operation;

        Planet _planet;

        switch(_operation) {
            case CREATE:
                _planet = Planet.dfltNew();
                break;
            default:
                _planet = planetsService.get(planet_id, true); // detached
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

    // ================================
    // METHODS

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
                '}';
    }

    public boolean validateBeforePrepareCRUD(ManagedPlanets _managedPlanets) {
        boolean check = true;

        check = this.getPlanet() == null;
        if(check)
            _managedPlanets.registerError("Internal error!", "Internal data inconsistency!", "Internal data inconsistency: session memory cell for planet data is not ready!");

        check = (this.getPlanet() != null && (this.operation != CRUD_Op.CREATE && this.getPlanet().getPlID() != null));
        if(check)
            _managedPlanets.registerError("Planet operation error!", "Planet operation error!", "Cent register a planet, that is already identified!");
        // ...
        // and other checks

        return check;
    }

    public boolean validateBeforeDoCRUD(ManagedPlanets _managedPlanets) {
        boolean check = true;

        // checks here

        return check;
    }
}
