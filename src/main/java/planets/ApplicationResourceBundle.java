package planets;

import home.lang.CRUD_Op;
import home.lang.HomeUtils;

import javax.faces.context.FacesContext;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.*;

public class ApplicationResourceBundle {
    private static final String FILE_ID = "app.messages";
    private static final List<Locale> SUPPORTED_LOCALES = // todo: make sure loads after FacesContext (how???)
            Collections.unmodifiableList(
                    HomeUtils.it2list
                            (FacesContext.getCurrentInstance().getApplication().getSupportedLocales()
                                    , new LinkedList<Locale>()
                            ));
    private static final Map<Locale,ResourceBundle> APP_BUNDLES;

    static {
        Map<Locale,ResourceBundle> map = new HashMap<Locale,ResourceBundle>();
        for(Locale loc : SUPPORTED_LOCALES)
            map.put(loc, ResourceBundle.getBundle(FILE_ID, loc));
        APP_BUNDLES = Collections.unmodifiableMap(map);
    }

    // ================================
    // CONSTRUCTORS
    private static final ApplicationResourceBundle SINGLETON = new ApplicationResourceBundle();
    public static ApplicationResourceBundle getInstance() { return SINGLETON; } // special for JSF
    protected ApplicationResourceBundle() {}

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
        SimpleDateFormat mf = new SimpleDateFormat(rb.getString(MKEY__DATETIME_FORMAT)); // todo: put in static map
        return mf.format(datetime);
    }

    // ================================
    // METHODS

    // ================================
    // LOW-LEVEL OVERRIDES


}
