package home.tries.planets.web.solution_spring.validator;

import home.tries.planets.model.entities.Planet;
import home.tries.planets.web.solution_spring.controller.command.PlanetCommand;
import home.utils.CRUD_Op;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

public class PlanetCommandValidator implements Validator {
protected final Log logger = LogFactory.getLog(getClass());

    private final Validator planetValidator;

    public PlanetCommandValidator(Validator  _planetValidator) {
        if (_planetValidator == null)
            throw new IllegalArgumentException("The supplied [Validator] is required and must not be null.");

        if (!_planetValidator.supports(Planet.class))
            throw new IllegalArgumentException("The supplied [Validator] must support the validation of [Planet] instances.");

        this.planetValidator = (PlanetValidator) _planetValidator;
    }

    public boolean supports(Class candidate) {
        return PlanetCommand.class.isAssignableFrom(candidate);
    }

    public void validate(Object o, Errors _errors) {
        ValidationUtils.rejectIfEmptyOrWhitespace(_errors, "operation", "required", "Field is required.");

        PlanetCommand plCmd = (PlanetCommand) o;
        try {
            _errors.pushNestedPath("planet");
            ValidationUtils.invokeValidator(this.planetValidator, plCmd.getPlanet(), _errors);
        } finally {
            _errors.popNestedPath();
        }

        if(plCmd.getOperation() != null && plCmd.getOperation() == CRUD_Op.READ) {
            ValidationUtils.rejectIfEmptyOrWhitespace(_errors, "plID", "required", "Field is required.");
        }

        logger.info("PlanetCommandValidator.validate");
        logger.info(_errors.getErrorCount());
        logger.info(_errors.getAllErrors());
    }
}
