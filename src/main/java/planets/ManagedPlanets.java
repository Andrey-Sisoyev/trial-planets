package planets;

import home.lang.CRUD_Op;
import home.lang.EntityExistsException;
import home.lang.EntityExistsNotException;
import org.richfaces.model.DataProvider;
import org.richfaces.model.ExtendedTableDataModel;
import org.richfaces.model.selection.SimpleSelection;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import java.text.MessageFormat;
import java.util.*;
import java.util.logging.Logger;

@SuppressWarnings({"SynchronizeOnNonFinalField"})
public class ManagedPlanets implements java.io.Serializable {
    private static final Logger logger = Logger.getLogger(ManagedPlanets.class.getSimpleName());

    // ====================================
    // NON-STATIC STUFF
    private volatile String currentLocaleStr = FacesContext.getCurrentInstance().getViewRoot().getLocale().toString();
    private final PlanetCommand planetCommand;
    private volatile SimpleSelection selection; // final n/a
    private Date snapshotTime;
    private volatile Object tableState; // todo: remove???
    private final ExtendedTableDataModel<Planet> tableDataModel;

    // ====================================
    // CONSTRUCTORS

    public ManagedPlanets() {
        logger.info("constructor call");
        planetCommand = new PlanetCommand();
        selection = new SimpleSelection();
        tableDataModel =
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
        tableDataModel.reset();
        snapshotTime = new Date();
    }

    @PostConstruct
    public void init() { // todo: doesn't work
        logger.info("@PostConstruct.");
    }

    // ====================================
    // GETTERS/SETTERS

    public String getCurrentLocaleStr() {
        return currentLocaleStr;
    }

    public void setCurrentLocaleStr(String _currentLocaleStr) {
        currentLocaleStr = _currentLocaleStr;
    }

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

    public String getSnapshotTime() {
        return ApplicationResourceBundle.getDatetimeStr(snapshotTime);
    }

    public ApplicationResourceBundle getApplicationResourceBundle() {
        return ApplicationResourceBundle.getInstance();
    }

    // ====================================
    // METHODS
    private void noticePerformance(String performed_what) {
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Performed '" + performed_what + "': Summary.", "Performed '" + performed_what + "': Details."));
    }

    private void delay() {
        try { Thread.sleep(1000); } catch(InterruptedException e) { e.printStackTrace(); }
    }

    private String getLocalizedMsg(String bundle, String key) {
        Locale loc = FacesContext.getCurrentInstance().getViewRoot().getLocale();
        ResourceBundle labels = ResourceBundle.getBundle(bundle, loc);
        return labels.getString(key);

    }

    public void resetTableSnapshot() {
        synchronized (tableDataModel) {
            tableDataModel.reset();
            snapshotTime = new Date();
        }
    }

    public void notifyAboutCRUD_Error(CRUD_Op operation_log, Integer planet_id_log, Exception e) {
        Locale loc = FacesContext.getCurrentInstance().getViewRoot().getLocale();
        ResourceBundle app_labels    = ResourceBundle.getBundle("app.messages", loc);
        ResourceBundle planet_labels = ResourceBundle.getBundle("planets.messages", loc);

        MessageFormat msgfmt_failure         = new MessageFormat(app_labels.getString("operation.failure"));
        MessageFormat msgfmt_failure_details = new MessageFormat(app_labels.getString("operation.failure.details"));

        String msg_operation = app_labels.getString(operation_log.getMsgPropertyName());
        String msg_obj_type  = planet_labels.getString("planet");
        String msg_id        = planet_id_log.toString();
        String msg_details   = e.getLocalizedMessage(); // for bes case getLocalizedMessage is to by manually overriden
        String msg_failure_details = msgfmt_failure_details.format(msg_details);

        Object[] args = {msg_operation, msg_obj_type, msg_id, msg_failure_details};

        String msg_failure = msgfmt_failure.format(args);

        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, msg_failure, ""));
    }

    public void notifyAboutCRUD_Success(CRUD_Op operation_log, Integer planet_id_log) {
        Locale loc = FacesContext.getCurrentInstance().getViewRoot().getLocale();
        ResourceBundle app_labels    = ResourceBundle.getBundle("app.messages", loc);
        ResourceBundle planet_labels = ResourceBundle.getBundle("planets.messages", loc);

        MessageFormat msgfmt_success = new MessageFormat(app_labels.getString("operation.success"));

        String msg_operation = app_labels.getString(operation_log.getMsgPropertyName());
        String msg_obj_type  = planet_labels.getString("planet");
        String msg_id        = planet_id_log.toString();

        Object[] args = {msg_operation, msg_obj_type, msg_id};

        String msg_success = msgfmt_success.format(args);

        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, msg_success, ""));
    }

    // ====================================
    // ACTIONS

    public Object reloadTable() {
        logger.info("before reloadTable (" + this + ")");
        delay();

        resetTableSnapshot();

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

        CRUD_Op operation_log;
        Integer planet_id_log;
        Exception failure_cause = null;

        synchronized (planetCommand) {
            // critical section!
            // operation of the command have no setter, and may be changed only through action
            // all actions are assummed to be thread-safe
            operation_log = planetCommand.getOperation();
            planet_id_log = planetCommand.getSelectedPlanet().getPlID(); // getSelectedPlanet != null

            try { planetCommand.doCRUD(); }
            catch (EntityExistsNotException e) { failure_cause = e; }
            catch (EntityExistsException    e) { failure_cause = e; }

            if(planet_id_log == null) planet_id_log = planetCommand.getSelectedPlanet().getPlID();
            assert planet_id_log != null;

            resetTableSnapshot();
        }
        if(failure_cause != null)
             notifyAboutCRUD_Error  (operation_log, planet_id_log, failure_cause);
        else notifyAboutCRUD_Success(operation_log, planet_id_log);

        logger.info("after doCRUD (" + this + ")");
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

    public Object changeLocale() {
        logger.info("before changeLocale " + FacesContext.getCurrentInstance().getViewRoot().getLocale());

        FacesContext.getCurrentInstance().getViewRoot().setLocale(new Locale(this.currentLocaleStr));

        logger.info("after changeLocale " + FacesContext.getCurrentInstance().getViewRoot().getLocale());
        return null;
    }
}