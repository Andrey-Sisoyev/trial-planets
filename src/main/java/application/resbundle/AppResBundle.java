package application.resbundle;

import home.lang.CRUD_Op;
import home.lang.HomeUtils;
import application.planets.ManagedPlanets;

import javax.faces.application.Application;
import javax.faces.context.FacesContext;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.SystemEvent;
import javax.faces.event.SystemEventListener;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.logging.Logger;

public class AppResBundle {
    private static final Logger logger = Logger.getLogger(ManagedPlanets.class.getSimpleName());
    private static final String FILE_ID = "app.messages";
    private static List<Locale> SUPPORTED_LOCALES;
    private static Map<Locale,ResourceBundle> APP_BUNDLES;

    // ================================
    // CONSTRUCTORS

    protected static void init() {
        SUPPORTED_LOCALES = Collections.unmodifiableList(
                HomeUtils.it2list
                (FacesContext.getCurrentInstance().getApplication().getSupportedLocales()
                        , new LinkedList<Locale>()
                )
        );

        Map<Locale,ResourceBundle> map = new HashMap<Locale,ResourceBundle>();
        for(Locale loc : SUPPORTED_LOCALES)
            map.put(loc, ResourceBundle.getBundle(FILE_ID, loc));
        APP_BUNDLES = Collections.unmodifiableMap(map);
    }

    private static final AppResBundle SINGLETON = new AppResBundle();
    public static AppResBundle getInstance() { return SINGLETON; } // special for JSF
    protected AppResBundle() {}

    // ================================
    // GETTERS/SETTERS
    public List<Locale> getSupportedLocales() { return SUPPORTED_LOCALES; }

    public static ResourceBundle getBundle() {
        Locale loc = FacesContext.getCurrentInstance().getViewRoot().getLocale();
        return APP_BUNDLES.get(loc);
    }

    private static final String MKEY__PREPARE_OPERATION = "operation.prepare";
    public static String getPrepareOperation(CRUD_Op op) {
        ResourceBundle rb = getBundle();
        MessageFormat mf = new MessageFormat(rb.getString(MKEY__PREPARE_OPERATION)); // todo: put in static map
        return mf.format(new Object[] { rb.getString(op.getMsgPropertyName()) } );
    }

    private static final String MKEY__DATETIME_FORMAT = "ui.dateTimePattern";
    public static String getDatetimeStr(Date datetime) {
        ResourceBundle rb = getBundle();
        String date_time_format = rb.getString(MKEY__DATETIME_FORMAT);
        // logger.info("getDatetimeStr (" + date_time_format + ", " + datetime + ")");
        SimpleDateFormat mf = new SimpleDateFormat(date_time_format); // todo: put in static map
        return mf.format(datetime);
    }

    // ================================
    // METHODS

    // ================================
    // LOW-LEVEL OVERRIDES
}
