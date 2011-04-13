package home.tries.planets.web.solution_jsf.mbeans;


import home.tries.planets.model.entities.Planet;
import home.tries.planets.service.PlanetsService;
import home.tries.planets.web.solution_jsf.command.PlanetCommand;
import home.utils.CRUD_Op;
import home.utils.HomeUtils;
import home.utils.model.EntityExistsException;
import home.utils.model.EntityExistsNotException;
import home.utils.model.NoResultException;
import org.ajax4jsf.component.html.HtmlAjaxCommandLink;
import org.hibernate.validator.NotEmpty;
import org.richfaces.component.UIDataTable;
import org.richfaces.component.html.HtmlModalPanel;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import java.util.Date;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;

// todo: decompose
public class ManagedPlanets implements java.io.Serializable {
    // ================================
    // NON-STATIC STUFF
    private LinkedBlockingQueue<PlanetsService> planetsServiceResource;

    private UIDataTable table;          // todo: thread safe?
    private HtmlModalPanel CRUD_window;

    private volatile PlanetCommand planetCommand;
    private volatile List<Planet> planets;
    private volatile String snapshotTime;  // todo: Date

    private UIInput operatedPlanetId;

    // ================================
    // CONSTRUCTOR
    @PostConstruct
    public void create() {
        this.updatePlanetsList();
        planetCommand = new PlanetCommand(new Planet(), null);
    }

    // ================================
    // GETTERS/SETTERS

    public PlanetsService acquirePlanetsService() {
        try {
            return planetsServiceResource.take();
        } catch (InterruptedException e) {
            // e.printStackTrace();
            // throw new ImpossibleException(e);
            return null;   // todo: better strategy?
        }
    }

    public void releasePlanetsService(PlanetsService ps) {
        try {
            planetsServiceResource.put(ps);
        } catch (InterruptedException e) {
            // e.printStackTrace();
            // throw new ImpossibleException(e);
        }
    }

    public void setPlanetsService(PlanetsService _planetsService) {
        LinkedBlockingQueue<PlanetsService> queue = new LinkedBlockingQueue<PlanetsService>(1);
        try {
            queue.put(_planetsService);
        } catch (InterruptedException e) {
            // e.printStackTrace();
            // throw new ImpossibleException(e);
        }
        this.planetsServiceResource = queue;
    }

    public UIDataTable getTable() {
        return table;
    }

    public void setTable(UIDataTable _table) {
        table = _table;
    }

    public HtmlModalPanel getCRUD_window() {
        return CRUD_window;
    }

    public void setCRUD_window(HtmlModalPanel _CRUD_window) {
        CRUD_window = _CRUD_window;
    }

    public int getPlanetsCount() {
        return planets.size();
    }

    public List<Planet> getPlanets() {
        return planets;
    }

    public PlanetCommand getPlanetCommand() {
        return planetCommand;
    }

    public void setOperatedPlanetId(UIInput operatedPlanetId) {
        this.operatedPlanetId = operatedPlanetId;
    }

    public UIInput getOperatedPlanetId() {
        return operatedPlanetId;
    }

    public Object getSnapshotTime() {
        return snapshotTime;
    }

    // ================================
    // METHODS
    public void updateSnapshotTime() {
        this.snapshotTime = (new Date()).toString();
    }

    public void registerError(String element, String summary, String details) {
        FacesMessage fm = new FacesMessage(summary, details);
        FacesContext.getCurrentInstance().addMessage(element, fm);
    }

    public void updatePlanetsList() {
        PlanetsService ps = acquirePlanetsService();
        this.planets = ps.getPlanets(); // todo: try-catch?
        this.updateSnapshotTime();
        releasePlanetsService(ps);
    }

    public void cancelAction(ActionEvent e){
        this.getCRUD_window().setRendered(false);
    }

    public String doCRUD() {
        if(!this.getPlanetCommand().validateBeforeDoCRUD(this))
            return null;
        PlanetsService ps = null;
        try {
            ps = acquirePlanetsService();
            this.getPlanetCommand().doCRUD(ps);
            // todo: destroy session?
        } catch (EntityExistsException e) {
            e.printStackTrace();
            registerError("Create new planet form error!", "Planet exists error!", "Error, planet you are trying to register by a specified planet ID already exists!");
        } catch (EntityExistsNotException e) {
            e.printStackTrace();
            registerError("Operation with planet form error!", "Planet does not exists error!", "Error, planet you are trying to operate on does not exists (by specified ID)!");
        } finally {
            if(ps != null)
                releasePlanetsService(ps);
        }

        return null;
    }

    public String prepareCRUD(CRUD_Op op) {
        PlanetsService ps = null;
        Integer plID = null;
        if(op != CRUD_Op.CREATE) {
            plID = HomeUtils.str2int((String) getOperatedPlanetId().getValue());
            if(plID == null) {
                registerError("Planet ID error!", "Planet ID required, but is not specified or not numeric!", "Planet ID required!, but is not specified or not numeric!!");
                return null;
            }
        }
        if(!this.getPlanetCommand().validateBeforePrepareCRUD(this))
            return null;
        try {
            ps = acquirePlanetsService();
            this.planetCommand = new PlanetCommand(plID, op, ps);
        } catch (NoResultException e) {
            registerError("Operation with planet form error!", "Planet does not exists error!", "Error, planet you are trying to operate on does not exists (by specified ID)!"); // todo:same error as for EntityExistsNotException
            e.printStackTrace();
        } finally {
            if(ps != null)
                releasePlanetsService(ps);
        }
        this.getCRUD_window().setRendered(true);
        return null;
    }

    public String showForm_CREATE() { return prepareCRUD(CRUD_Op.CREATE); } // todo: elaborate ENUM in facelet
    public String showForm_READ() { return prepareCRUD(CRUD_Op.READ); }
    public String showForm_UPDATE() { return prepareCRUD(CRUD_Op.READ); }
    public String showForm_DELETE() { return prepareCRUD(CRUD_Op.READ); }

    public void reloadTable() {
        this.updatePlanetsList();
    }

    // ================================
    // LOW-LEVEL OVERRIDES
}
