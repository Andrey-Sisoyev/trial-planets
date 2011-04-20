package application.resbundle;

import application.planets.ManagedPlanets;
import home.lang.HomeUtils;

import javax.faces.application.Application;
import javax.faces.event.*;
import java.util.logging.Logger;


public class AppResBundle_FacesStartListener implements SystemEventListener {
    private static final Logger logger = Logger.getLogger(AppResBundle_FacesStartListener.class.getSimpleName());

    @Override
    public void processEvent(SystemEvent event) throws AbortProcessingException {
        logger.info("processEvent");
        AppResBundle.init();
    }

    @Override
    public boolean isListenerForSource(Object source) {
        return (source instanceof Application);
    }

}