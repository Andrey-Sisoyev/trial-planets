package home.tries.planets.web.solution_spring.controller;

import home.tries.planets.web.solution_spring.controller.command.PlanetCommand;
import home.utils.CRUD_Op;
import home.utils.HomeUtils;
import home.utils.model.EntityExistsException;
import home.utils.model.EntityExistsNotException;
import home.utils.model.NoResultException;
import home.utils.web.controller.SessionLost;
import home.utils.web.controller.SimpleFormController2;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.validation.BindException;
import org.springframework.validation.Errors;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

// almost ready for generalization
public class CRUD_Planet extends SimpleFormController2 {
    protected final Log logger = LogFactory.getLog(getClass());

    // NoResultException will be caught and passed to
    @Override
    protected PlanetCommand formBackingObject(HttpServletRequest request) throws NoResultException {
        Integer busin_obj_id = HomeUtils.str2int(request.getHeader("busin_obj_id"));

        CRUD_Op operation;

        if(busin_obj_id == null) {
            operation = CRUD_Op.CREATE;
        } else {
            operation = CRUD_Op.valueOf2(request.getHeader("operation"));
            if(operation == null) operation = CRUD_Op.CREATE;
        }

        PlanetCommand ret = new PlanetCommand(busin_obj_id, operation);

        logger.info("CALLED formBackingObject: " + ret.toString());

        return ret;
    }

    @Override
    protected ModelAndView onFailure(Throwable e, Object command, HttpServletRequest request) {
        ModelAndView mv = super.onFailure(e, command, request);

        String busin_obj_id = null;
        String operation    = null;
        if(command == null) {
            busin_obj_id = request.getHeader("busin_obj_id");
            operation    = request.getHeader("operation");
        } else {
            operation    = request.getParameter("_operation");
            busin_obj_id = request.getParameter("_busin_obj_id");
        }

        String unknown = "&lt;unknown&gt;";
        if(busin_obj_id == null || "".equals(busin_obj_id)) busin_obj_id = unknown;
        if(operation    == null || "".equals(operation))    operation    = unknown;

        mv.addObject("busin_obj_id", busin_obj_id);
        mv.addObject("operation"   , operation);
        mv.addObject("failure_msg" , e.getMessage());

        return mv;
    }

    @Override
    protected Map referenceData(HttpServletRequest request, Object command, Errors errors) throws Exception {
        Map ref_data = super.referenceData(request, command, errors);
        if(ref_data == null) ref_data = new HashMap();
        // ref_data.put("name", val); // this goes to form jsp (not success jsp)
        ref_data.put("cmd", (PlanetCommand) command); // this goes to form jsp (not success jsp)
        return ref_data;
    }

    private static final String ERR__SESSION_LOST = "It appears, that user is trying to commit a different operation (operation: %1$s; business obj ID: %2$d), than one that is currently pending in server's session memory: (operation: %3$s; business obj ID: %4$d)!";
    protected void validateSession(HttpServletRequest request, Object command) throws SessionLost {
        PlanetCommand planetCmd = (PlanetCommand) command;
        CRUD_Op operation = CRUD_Op.valueOf2(request.getParameter("_operation"));
        Integer busin_obj_id = HomeUtils.str2int(request.getParameter("_busin_obj_id"));
        if(  (busin_obj_id == null) != (planetCmd.getPlanet().getPlID() == null)
          || (operation == null) != (planetCmd.getOperation() == null)
          || (busin_obj_id != null && !busin_obj_id.equals(planetCmd.getPlanet().getPlID()))
          || (operation != null && !operation.equals(planetCmd.getOperation()))
          ) {
            // then apparently user filled something else, then it is seen in session
            throw new SessionLost(String.format(ERR__SESSION_LOST, operation, busin_obj_id, planetCmd.getOperation(), planetCmd.getPlanet().getPlID()));
        }
    }

    @Override
    protected ModelAndView onSubmit(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors)  {

        PlanetCommand planetCmd = (PlanetCommand) command;

        ModelAndView mv = new ModelAndView(this.getSuccessView());
        logger.info("CALLED onSubmit");

        try {
            validateSession(request, command);
            planetCmd.doCRUD();
        } catch (EntityExistsException e) {
            return onFailure(e, planetCmd, request);
        } catch (EntityExistsNotException e) {
            return onFailure(e, planetCmd, request);
        } catch (SessionLost e) {
            return onFailure(e, planetCmd, request);
        }

        mv.addObject("busin_obj_id", planetCmd.getPlanet().getPlID());
        mv.addObject("operation", planetCmd.getOperation().toString()); // this goes to success jsp (not form jsp)

        return mv;
    }

    // ===============================================
    // A hack to become able to get to terminating-failure from a formBackingObject.


}

