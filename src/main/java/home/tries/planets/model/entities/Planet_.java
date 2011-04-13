package home.tries.planets.model.entities;

import javax.persistence.metamodel.SingularAttribute;

@javax.persistence.metamodel.StaticMetamodel(home.tries.planets.model.entities.Planet.class)
public class Planet_ {
    public static volatile SingularAttribute<Planet,Integer> plID;
    public static volatile SingularAttribute<Planet,String> name;
    public static volatile SingularAttribute<Planet,Double> distToEarth;
    public static volatile SingularAttribute<Planet,String> discovererName;
    public static volatile SingularAttribute<Planet,Double> diameter;
    public static volatile SingularAttribute<Planet,Boolean> atmosphere;
    public static volatile SingularAttribute<Planet,Boolean> deleted;
}

