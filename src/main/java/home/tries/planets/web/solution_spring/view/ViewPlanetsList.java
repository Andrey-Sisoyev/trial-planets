package home.tries.planets.web.solution_spring.view;


import home.tries.planets.service.PlanetsService;
import home.tries.planets.model.entities.Planet;

import home.utils.HomeUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.List;

public class ViewPlanetsList implements Controller {
    protected final Log logger = LogFactory.getLog(getClass());

    public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        ModelAndView mv = new ModelAndView("/solution_spring/ajax/jsp/planets/planets_list.jsp");

        Integer from = HomeUtils.str2int(request.getParameter("pl_list_from"));
        Integer to   = HomeUtils.str2int(request.getParameter("pl_list_to"));

        List<Planet> planets_list;
        Integer pl_count;

        PlanetsService plaserv = new PlanetsService();

        if(from != null && to != null) {
            planets_list = plaserv.getPlanets(from, to);
            pl_count = plaserv.planetsCount();
        } else {
            planets_list = plaserv.getPlanets();
            pl_count = planets_list.size();
        }

        logger.info("CALLED: ViewPlanetsList.handleRequest");
        logger.info(planets_list);

        mv.addObject("planets_list" , planets_list);
        mv.addObject("palnets_count", pl_count);
        mv.addObject("now", (new Date()).toString());

        return mv;
    }


}
