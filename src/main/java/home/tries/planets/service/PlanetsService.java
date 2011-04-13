package home.tries.planets.service;

import home.tries.planets.service.dao.PlanetsDAO;
import home.tries.planets.model.entities.Planet;
import home.utils.CRUD_Op;
import home.utils.business.HasEntityManager;
import home.utils.model.EntityExistsException;
import home.utils.model.EntityExistsNotException;
import home.utils.model.NoResultException;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import java.util.List;

public class PlanetsService {
    // ================================
    // NON-STATIC STUFF

    private PlanetsDAO planetsDAO;

    // ================================
    // CONSTRUCTORS

    public PlanetsService() {
        planetsDAO = new PlanetsDAO();
    }

    public PlanetsService(PlanetsDAO _planetsDAO) {
        planetsDAO = _planetsDAO;
    }

    public PlanetsService(EntityManager em) {
        planetsDAO = new PlanetsDAO(em);
    }

    public PlanetsService(HasEntityManager em_holder) {
        planetsDAO = new PlanetsDAO(em_holder.getEM());
    }

    // ================================
    // METHODS

    public List<Planet> getPlanets() {  // todo: detach?
        return planetsDAO.findAll();
    }

    public List<Planet> getPlanets(int from, int to) { // todo: detach?
        return planetsDAO.findRange(from, to);
    }

    public Planet get(Integer id, boolean detached) throws NoResultException {
        Planet ret = (Planet) planetsDAO.find(id);
        if(detached) planetsDAO.getEM().detach(ret);
        return ret;
    }

    public void remove(Integer id) throws NoResultException, EntityExistsNotException {
        this.remove(get(id, false));
    }

    public void remove(Planet pl) throws EntityExistsNotException {
        EntityTransaction tx = planetsDAO.getEM().getTransaction();
        tx.begin();
        planetsDAO.getEM().merge(pl);
        planetsDAO.remove(pl);
        tx.commit();
    }

    public Integer create(Planet pl) throws EntityExistsException {
        pl.setPlID(null);
        EntityTransaction tx = planetsDAO.getEM().getTransaction();
        tx.begin();
        planetsDAO.create(pl);
        tx.commit();
        return pl.getPlID();
    }

    public void update(Planet pl) throws EntityExistsNotException {
        EntityTransaction tx = planetsDAO.getEM().getTransaction();
        tx.begin();
        planetsDAO.edit(pl);
        tx.commit();
    }

    public Integer planetsCount() {
        return planetsDAO.count();
    }

    public void doCRUD(CRUD_Op op, Planet planet) throws EntityExistsException, EntityExistsNotException {
        switch(op) {
            case CREATE: this.create(planet); break;
            case UPDATE: this.update(planet); break;
            case DELETE: this.remove(planet); break;
            case READ  : planetsDAO.getEM().refresh(planet); break;  // todo: test
        }
    }
}
