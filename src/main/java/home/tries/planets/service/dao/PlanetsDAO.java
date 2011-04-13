package home.tries.planets.service.dao;

import home.tries.planets.model.entities.Planet;
import home.tries.planets.model.entities.Planet_;
import home.tries.planets.model.utils.ModelUtils;
import home.utils.business.HasEntityManager;

import home.utils.model.*;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.List;

/**
 * По совместительству фабрика EntityManager.
 * Не ThreadSafe!
 */
public class PlanetsDAO implements HasEntityManager {
    // ================================
    // NON-STATIC STUFF

    private final EntityManager em;

    // ================================
    // CONSTRUCTORS

    public PlanetsDAO(EntityManager _em) {
        this.em = _em;
    }

    public PlanetsDAO() {
        this.em = ModelUtils.emf.createEntityManager();
    }

    // ================================
    // METHODS

    @PersistenceContext
    public EntityManager getEM() {
        return this.em;
    }

    public void create(Planet planet) throws EntityExistsException {
        if(!planet.isNew()) throw new EntityExistsException("Can't create object, that has isNew flag = false.");
        getEM().persist(planet);
        planet.setNew(false);
    }

    public void edit(Planet planet) throws EntityExistsNotException {
        if(planet.isNew()) throw new EntityExistsNotException("Can't update nonregistered object, - isNew flag = true.");
        getEM().merge(planet);
    }

    public void remove(Planet planet) throws EntityExistsNotException {
        if(planet.isNew()) throw new EntityExistsNotException("Can't delete nonregistered object, - isNew flag = true.");
        planet.setDeleted(true);
        getEM().merge(planet);
    }

    public Planet find(Object id) throws NoResultException {
        // return getEM().find(Planet.class, id);

        CriteriaBuilder qb = getEM().getCriteriaBuilder();
        CriteriaQuery<Planet> c = qb.createQuery(Planet.class);
        Root<Planet> p = c.from(Planet.class);
        Predicate condition1 = qb.equal(p.get(Planet_.deleted), false);
        Predicate condition2 = qb.equal(p.get(Planet_.plID), (Integer) id);
        c.where(condition1, condition2);
        TypedQuery<Planet> q = getEM().createQuery(c);
        Planet result = q.getSingleResult();

        return result;
    }

    public List<Planet> findAll() {
        CriteriaBuilder qb = getEM().getCriteriaBuilder();
        CriteriaQuery<Planet> c = qb.createQuery(Planet.class);
        Root<Planet> p = c.from(Planet.class);
        Predicate condition = qb.equal(p.get(Planet_.deleted), false);
        c.where(condition);
        TypedQuery<Planet> q = getEM().createQuery(c);
        List<Planet> result = q.getResultList();

        return result;
    }

    public List<Planet> findRange(int from, int to) {
        if(from > to) throw new IllegalArgumentException();

        CriteriaBuilder qb = getEM().getCriteriaBuilder();
        CriteriaQuery<Planet> c = qb.createQuery(Planet.class);
        Root<Planet> p = c.from(Planet.class);
        Predicate condition = qb.equal(p.get(Planet_.deleted), false);
        c.where(condition);
        TypedQuery<Planet> q = getEM().createQuery(c);
        q.setMaxResults(to - from);
        q.setFirstResult(to);
        return q.getResultList();
    }

    public int count() {
        CriteriaBuilder qb = getEM().getCriteriaBuilder();
        CriteriaQuery cq = qb.createQuery();
        Root<Planet> rt = cq.from(Planet.class);
        Predicate condition = qb.equal(rt.get(Planet_.deleted), false);
        cq.where(condition);
        cq.select(getEM().getCriteriaBuilder().count(rt));
        Query q = getEM().createQuery(cq);
        return ((Long) q.getSingleResult()).intValue();
    }
}

