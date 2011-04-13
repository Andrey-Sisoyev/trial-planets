package home.tries.planets.web.solution_spring.validator;

import home.tries.planets.model.entities.Planet;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

public class PlanetValidator implements Validator {
    protected final Log logger = LogFactory.getLog(getClass());

    public boolean supports(Class candidate) {
        return Planet.class.isAssignableFrom(candidate);
    }

    public void validate(Object obj, Errors errors) {
        Planet target = (Planet) obj;
        // this.logger.info(">>>>>>>DDDDD>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
        // this.logger.info(new Boolean(target == null).toString());
        // this.logger.info(new Boolean(errors == null).toString());

        // ValidationUtils.rejectIfEmptyOrWhitespace(errors, "plID", "required", "Field is required.");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "name", "required", "Field is required.");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "distToEarth", "required", "Field is required.");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "diameter", "required", "Field is required.");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "discovererName", "required"); //, "Field is required.");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "atmosphere", "required", "Field is required.");


        if(target.getPlID() != null && target.getPlID() <= 0 && !target.isNew()) {
            errors.rejectValue("plID", "no_id", "Planet ID can't be negative (unless new one is registered)!");
        }

        if(target.getDistToEarth() == null)
            errors.rejectValue("distToEarth", "null_not_allowed", "Distance to Earth can't be null!");
        else if(target.getDistToEarth() < 0) {
            errors.rejectValue("distToEarth", "not_positive", "Distance to Earth can't be negative!");
        }

        if(target.getDiameter() == null)
            errors.rejectValue("diameter", "null_not_allowed", "Diameter can't be null!");
        else if(target.getDiameter() <= 0) {
            errors.rejectValue("diameter", "not_positive", "Diameter to Earth must be positive!");
        }

        if(target.getDistToEarth() != null && target.getDistToEarth() == 0 && !target.isAtmosphere()) {
            errors.rejectValue("atmosphere", "not_earth", "Earth has atmosphere!");
        }

    }
}