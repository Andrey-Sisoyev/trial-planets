package home.utils.business;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

public interface HasEntityManager {
    // ================================
    // GETTERS/SETTERS

    // ================================
    // METHODS

    @PersistenceContext
    EntityManager getEM();
}
