package planets;


import org.richfaces.model.DataProvider;
import org.richfaces.model.ExtendedTableDataModel;
import org.richfaces.model.selection.Selection;
import org.richfaces.model.selection.SimpleSelection;

import javax.faces.event.ActionEvent;
import java.util.*;
import java.util.logging.Logger;

public class ManagedPlanets implements java.io.Serializable {
    private static final Logger logger = Logger.getLogger(ManagedPlanets.class.getSimpleName());

    public class PlanetCommand {
        private Planet selectedPlanet;
        private CRUD_Op operation;
        private Boolean doingOperation;

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

        public Planet getSelectedPlanet() {
            return selectedPlanet;
        }

        public CRUD_Op getOperation() {
            return operation;
        }

        public Boolean getDoingOperation() {
            return doingOperation;
        }
    }

    private PlanetCommand planetCommand = new PlanetCommand();
    private SimpleSelection selection = new SimpleSelection();
    private Object tableState; // todo: remove???
    private ExtendedTableDataModel<Planet> tableDataModel;

    // ====================================
    // GET/SET

    public PlanetCommand getPlanetCommand() {
        return planetCommand;
    }

    public List<Planet> getPlanetsList() {
        return new LinkedList<Planet>(Planet.planetsDB.values());
    }

    public SimpleSelection getSelection() {
        return selection;
    }

    public void setSelection(SimpleSelection _selection) {
        selection = _selection;
    }

    public Object getTableState() {
        return tableState;
    }

    public void setTableState(Object _tableState) {
        tableState = _tableState;
    }

    public ExtendedTableDataModel<Planet> getPlanetsDataModel() {  // src: http://anonsvn.jboss.org/repos/richfaces/branches/community/3.3.X/samples/richfaces-demo/src/main/java/org/richfaces/demo/extendedDataTable/ExtendedTableBean.java
        if (tableDataModel == null) {
            tableDataModel = new ExtendedTableDataModel<Planet>(new DataProvider<Planet>(){
                public Planet getItemByKey(Object key) {
                    for(Planet p : Planet.planetsDB.values())
                        if (key.equals(getKey(p))) return p;
                    return null;
                }
                public List<Planet> getItemsByRange(int firstRow, int endRow) {
                    return new ArrayList<Planet>(Planet.planetsDB.values()).subList(firstRow, endRow);
                }
                public Object getKey(Planet item) { return item.getPlID(); }
                public int getRowCount() { return Planet.planetsDB.size(); }
            });
        }
        return tableDataModel;
    }
    // ====================================
    // ACTIONS

    public Object reloadTable() {
        tableDataModel.reset();
        return null;
    }

    public Object takeSelection(){
        logger.info("takeSelection");
        Object key = getSelection().getKeys().next();
		planetCommand.selectedPlanet = tableDataModel.getObjectByKey(key);
        planetCommand.operation = CRUD_Op.READ;
        planetCommand.doingOperation = true;
        return null;
	}

    public Object showNew() {
        logger.info("showNew");
        planetCommand.selectedPlanet = new Planet();
        planetCommand.operation = CRUD_Op.CREATE;
        planetCommand.doingOperation = true;
        return null;
    }

    public Object prepareUpdate() {
        logger.info("prepareUpdate");
        // planetCommand.selectedPlanet = new Planet();
        planetCommand.operation = CRUD_Op.UPDATE;
        // planetCommand.doingOperation = true;
        return null;
    }

    public Object prepareDelete() {
        // planetCommand.selectedPlanet = new Planet();
        planetCommand.operation = CRUD_Op.DELETE;
        // planetCommand.doingOperation = true;
        return null;
    }

    public Object doCRUD() {
        logger.info("doCRUD");
        switch(planetCommand.operation) {
            case CREATE:
                Integer id = Planet.planetsIDs_seq_next();
                planetCommand.selectedPlanet.setPlID(id);
                Planet.planetsDB.put(id, planetCommand.selectedPlanet);
                break;
            case READ: break;
            case UPDATE:
                Planet.planetsDB.replace(planetCommand.selectedPlanet.getPlID(), planetCommand.selectedPlanet);
                break;
            case DELETE:
                Planet.planetsDB.remove(planetCommand.selectedPlanet.getPlID());
                break;
        }
        tableDataModel.reset();
        planetCommand.selectedPlanet = null;
        planetCommand.operation = null;
        planetCommand.doingOperation = false;
        return null;
    }

    public Object cancelAction() {
        logger.info("cancelAction");
        planetCommand.selectedPlanet = null;
        planetCommand.operation = null;
        planetCommand.doingOperation = false;
        return null;
    }
}