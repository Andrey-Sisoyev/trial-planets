package planets;

import home.lang.CRUD_Op;

public class PlanetCommand {
    // ===========================
    // NON-STATIC STUFF
    private volatile Planet selectedPlanet;
    private volatile CRUD_Op operation;
    private volatile Boolean doingOperation;

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

    public void doCRUD() {
        assert doingOperation;

        // critical section!
        // guarded by synchronized (planetCommand) in ManagedBean
        Planet snapshot = selectedPlanet.clone();

        switch(operation) {
            case CREATE:
                Integer id = Planet.planetsIDs_seq_next();
                snapshot.setPlID(id);
                Planet.planetsDB.put(id, snapshot);
                Planet cur_sel = selectedPlanet;
                if(cur_sel != null) cur_sel.setPlID(id); // critical section! // guarded by synchronized (planetCommand) in ManagedBean
                break;
            case READ: break;
            case UPDATE:
                assert snapshot != null;
                Planet.planetsDB.replace(snapshot.getPlID(), snapshot);
                break;
            case DELETE:
                assert snapshot != null;
                Planet.planetsDB.remove(snapshot.getPlID());
                break;
        }
        this.cancelAction();
    }

    public void cancelAction() {
        operation = CRUD_Op.READ;
        doingOperation = selectedPlanet != null;
    }
}