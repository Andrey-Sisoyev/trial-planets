package home.tries.planets.model.utils;

import home.tries.planets.model.entities.Planet;
import home.tries.planets.service.PlanetsService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.persistence.*;

public class ModelUtils {
    protected final static Log logger = LogFactory.getLog(ModelUtils.class);
    public static final EntityManagerFactory emf = Persistence.createEntityManagerFactory("Planets_AdminUser_PU");
    private static final Boolean CFG__CREATE_INITIAL_DATA = true; // todo: move to persistence.xml

    static {
        if(CFG__CREATE_INITIAL_DATA) loadInitialData();
    }

    // index starts from 1
    public static void setParameters(Query q, Object... params) {
        for(int i = 0; i< params.length; i++)
            q.setParameter(i+1, params[i]);
    }

    private static void loadInitialData() {
        EntityManager em = emf.createEntityManager();

        try {
            Query q = em.createNativeQuery("insert into planets (planet_id, name, dist_to_earth, discoverer_name, diameter, atmosphere, is_deleted) values(nextval('planets_pk_seq'),?1,?2,?3,?4,?5,false)");
            EntityTransaction tx = em.getTransaction();
            tx.begin();

            ModelUtils.setParameters(q, "Меркурий", 10.0, "<неизвестно>",  10, false); q.executeUpdate();
            ModelUtils.setParameters(q, "Венера"  ,  5.0, "<неизвестно>",  10, true ); q.executeUpdate();
            ModelUtils.setParameters(q, "Земля"   ,  0.0, "<неизвестно>",  10, true ); q.executeUpdate();
            ModelUtils.setParameters(q, "Марс"    ,  5.0, "<неизвестно>",  10, false); q.executeUpdate();
            ModelUtils.setParameters(q, "Юпитер"  , 15.0, "<неизвестно>", 500, true ); q.executeUpdate();

            tx.commit();
        } catch (PersistenceException e) {
            try {
                Planet pl = new Planet();

                EntityTransaction tx = em.getTransaction();
                tx.begin();

                pl.setName("Земля");
                pl.setDistToEarth(0.0);
                pl.setDiscovererName("<неизвестно>");
                pl.setDiameter(10.0);
                pl.setAtmosphere(true);

                em.persist(pl);

                tx.commit();
            } catch (Exception ex) {}
        }

        /*
        INSERT INTO planets (name, dist_to_earth, discoverer_name, diameter, atmosphere)
        VALUES ('Меркурий', 10, '<неизвестно>', 10, false)
             , ('Венера', 5, '<неизвестно>', 10, true)
             , ('Земля', 0, '<неизвестно>', 10, true)
             , ('Марс', 5, '<неизвестно>', 10, true)
             , ('Юпитер', 15, '<неизвестно>', 500, true)
             ;
        */


    }
}
