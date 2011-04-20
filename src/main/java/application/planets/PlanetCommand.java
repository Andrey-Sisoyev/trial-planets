package application.planets;

import home.lang.CRUD_Op;
import home.lang.EntityExistsException;
import home.lang.EntityExistsNotException;

import javax.validation.Valid;
import javax.validation.constraints.AssertTrue;

public class PlanetCommand {
    // ===========================
    // NON-STATIC STUFF
    @Valid
    private volatile Planet selectedPlanet;
    private volatile CRUD_Op operation;
    private volatile Boolean doingOperation;

    // ===========================
    // VALIDATORS-GROUPS

    // ===========================
    // CONSTRUCTORS
    public PlanetCommand() {
        selectedPlanet = null;
        operation = null;
        doingOperation = false;

    }

    public PlanetCommand(Planet _selectedPlanet, CRUD_Op _operation, Boolean _doingOperation) {
        selectedPlanet = _selectedPlanet;
        operation = _operation;
        doingOperation = _doingOperation;
    }

    // ===========================
    // GETTERS-SETTERS
    public Planet getSelectedPlanet() {
        return selectedPlanet;
    }

    public CRUD_Op getOperation() {
        return operation;
    }

    public Boolean getDoingOperation() {
        return doingOperation;
    }

    // ==================================
    // ACTIONS
    public void takeSelection(Planet planet){
        selectedPlanet = planet;
        operation = CRUD_Op.READ;
        doingOperation = true;
    }

    public void prepareCreate() {
        selectedPlanet = new Planet();
        operation = CRUD_Op.CREATE;
        doingOperation = true;
    }

    public void prepareUpdate() {
        // selected planet assumed already there (by previous use of takeSelection())
        assert selectedPlanet != null;
        // doingOperation = true; // assumed already there (by previous use of takeSelection())
        assert doingOperation;
        operation = CRUD_Op.UPDATE;
    }

    public void prepareDelete() {
        // selected planet assumed already there (by previous use of takeSelection())
        assert selectedPlanet != null;
        // doingOperation = true; // assumed already there (by previous use of takeSelection())
        assert doingOperation;
        operation = CRUD_Op.DELETE;
    }

    public void doCRUD() throws EntityExistsNotException, EntityExistsException {
        assert doingOperation;

        // critical section!
        // guarded by synchronized (planetCommand) in ManagedBean
        Planet snapshot = selectedPlanet.clone();

        switch(operation) {
            case CREATE:
                Integer id = Planet.planetsIDs_seq_next();
                snapshot.setPlID(id);
                if(!Planet.planetsDB.containsKey(snapshot.getPlID()))
                     Planet.planetsDB.put(id, snapshot);
                else throw new EntityExistsException();
                Planet cur_sel = selectedPlanet;
                if(cur_sel != null) cur_sel.setPlID(id); // critical section! // guarded by synchronized (planetCommand) in ManagedBean
                this.cancelAction();
                break;
            case READ:
                if(!Planet.planetsDB.containsKey(snapshot.getPlID())) {
                    this.cancelAction();
                    throw new EntityExistsNotException();
                }
                break;
            case UPDATE:
                assert snapshot != null;
                if(Planet.planetsDB.containsKey(snapshot.getPlID())) {
                    Planet.planetsDB.replace(snapshot.getPlID(), snapshot);
                    this.cancelAction();
                } else {
                    operation = CRUD_Op.CREATE;
                    doingOperation = true;
                    selectedPlanet.setPlID(null);
                    throw new EntityExistsNotException();
                }
                break;
            case DELETE:
                assert snapshot != null;
                doingOperation = false;
                selectedPlanet = null;
                operation = CRUD_Op.READ;
                if(Planet.planetsDB.containsKey(snapshot.getPlID()))
                     Planet.planetsDB.remove(snapshot.getPlID());
                else throw new EntityExistsNotException();

                break;
        }
    }

    public void cancelAction() {
        operation = CRUD_Op.READ;
        doingOperation = selectedPlanet != null;
    }
}