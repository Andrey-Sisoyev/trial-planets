package planets;


import org.richfaces.model.DataProvider;
import org.richfaces.model.ExtendedTableDataModel;
import org.richfaces.model.selection.Selection;
import org.richfaces.model.selection.SimpleSelection;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import java.util.*;
import java.util.logging.Logger;

@SuppressWarnings({"SynchronizeOnNonFinalField"})
public class ManagedPlanets implements java.io.Serializable {
    private static final Logger logger = Logger.getLogger(ManagedPlanets.class.getSimpleName());

    // ====================================
    // NON-STATIC STUFF
    private final PlanetCommand planetCommand = new PlanetCommand(); // todo: volatile applicable??
    private volatile SimpleSelection selection = new SimpleSelection(); // final n/a
    private volatile Object tableState; // todo: remove???
    private final ExtendedTableDataModel<Planet> tableDataModel =
                new ExtendedTableDataModel<Planet>(new DataProvider<Planet>(){
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

    // ====================================
    // CONSTRUCTORS

    public ManagedPlanets() {
        logger.info("constructor call");
    }

    @PostConstruct
    public void init() { // todo: doesn't work
        logger.info("@PostConstruct.");
    }

    // ====================================
    // GETTERS/SETTERS

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
        synchronized (planetCommand) {
            selection = _selection;
        }
    }

    public Object getTableState() {
        return tableState;
    }

    public void setTableState(Object _tableState) {
        tableState = _tableState;
    }

    public ExtendedTableDataModel<Planet> getPlanetsDataModel() {  // src: http://anonsvn.jboss.org/repos/richfaces/branches/community/3.3.X/samples/richfaces-demo/src/main/java/org/richfaces/demo/extendedDataTable/ExtendedTableBean.java
        return tableDataModel;
    }
    // ====================================
    // ACTIONS

    public Object reloadTable() {
        logger.info("before reloadTable (" + this + ")");
        delay();

        synchronized (tableDataModel) {
            tableDataModel.reset();
        }
        logger.info("after reloadTable (" + this + ")");
        noticePerformance("reloadTable");
        return null;
    }

    public Object takeSelection(){
        logger.info("before takeSelection (" + this + ")");
        delay();

        synchronized (planetCommand) {
            Integer key = (Integer) selection.getKeys().next();
            planetCommand.takeSelection(tableDataModel.getObjectByKey(key));
        }

        logger.info("after takeSelection (" + this + ")");
        noticePerformance("takeSelection");
        return null;
	}

    public Object prepareCreate() {
        logger.info("before prepareCreate (" + this + ")");
        delay();

        synchronized (planetCommand) {
            planetCommand.prepareCreate();
        }

        logger.info("after prepareCreate (" + this + ")");
        noticePerformance("prepareCreate");
        return null;
    }

    public Object prepareUpdate() {
        logger.info("before prepareUpdate (" + this + ")");
        delay();

        synchronized (planetCommand) {
            planetCommand.prepareUpdate();
        }

        logger.info("after prepareUpdate (" + this + ")");
        noticePerformance("prepareUpdate");
        return null;
    }

    public Object prepareDelete() {
        logger.info("before prepareDelete (" + this + ")");
        delay();

        synchronized (planetCommand) {
            planetCommand.prepareDelete();
        }

        logger.info("after prepareDelete (" + this + ")");
        noticePerformance("prepareDelete");
        return null;
    }

    public Object doCRUD() {
        logger.info("before doCRUD  (" + this + ")");
        delay();

        synchronized (planetCommand) {
            planetCommand.doCRUD();
            synchronized (tableDataModel) {
                tableDataModel.reset();
            }
        }

        logger.info("after doCRUD (" + this + ")");
        noticePerformance("doCRUD");
        return null;
    }

    public Object cancelAction() {
        logger.info("before cancelAction (" + this + ")");
        delay();

        synchronized (planetCommand) {
            planetCommand.cancelAction();
        }

        logger.info("after cancelAction (" + this + ")");
        noticePerformance("cancelAction");
        return null;
    }

    private void noticePerformance(String performed_what) {
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Performed '" + performed_what + "': Summary.", "Performed '" + performed_what + "': Details."));
    }

    private void delay() {
        try { Thread.sleep(1000); } catch(InterruptedException e) { e.printStackTrace(); }
    }
}