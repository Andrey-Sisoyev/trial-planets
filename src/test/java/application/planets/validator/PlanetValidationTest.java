package application.planets.validator;

import application.planets.Planet;
import static org.testng.Assert.*;

import home.lang.HomeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.util.LinkedList;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

public class PlanetValidationTest {
    private static final Logger logger = LoggerFactory.getLogger(PlanetValidationTest.class.getSimpleName());
    private static Validator validator;

    @BeforeClass
    public static void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @BeforeTest
    public static void beforeTest() {
        logger.info("- - - - - -");
    }

    @Test
    public void t_earthHasAtmosphere() {
        logger.info("t_earthHasAtmosphere >>>");
        Planet planet = new Planet(1, "TestPlanet", 0.0, "TestName", 0.0, false);

        Set<ConstraintViolation<Planet>> constraintViolations = validator.validate(planet);

        logger.info(String.valueOf(constraintViolations.size()));
        for(ConstraintViolation<Planet> cviol : HomeUtils.it2list(constraintViolations.iterator(), new LinkedList<ConstraintViolation<Planet>>()))
            logger.info(cviol.getMessage());
    }

    @Test
    public void t_positiveDiameter() {
        logger.info("t_positiveDiameter >>>");
        Planet planet = new Planet(1, null, 0.0, "TestName", 0.0, false);

        Set<ConstraintViolation<Planet>> constraintViolations = validator.validate(planet);

        logger.info(String.valueOf(constraintViolations.size()));
        for(ConstraintViolation<Planet> cviol : HomeUtils.it2list(constraintViolations.iterator(), new LinkedList<ConstraintViolation<Planet>>()))
            logger.info(cviol.getPropertyPath() + " ||| " + cviol.getMessage());
    }

}