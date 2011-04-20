package application.planets.validator;

import application.planets.Planet;

import javax.validation.Constraint;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.util.logging.Logger;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;
import static java.lang.annotation.RetentionPolicy.values;

@Documented
@Constraint(validatedBy = EarthHasAtmosphere.EarthHasAtmosphereValidator.class)
@Target({ TYPE, METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER })
@Retention(RUNTIME)
public @interface EarthHasAtmosphere {
    String message() default "{planet.EarthHasAtmosphereValidator.message}";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

    @Target({ METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER })
    @Retention(RUNTIME)
    @Documented
    @interface List {
        EarthHasAtmosphere[] value();
    }

    public class EarthHasAtmosphereValidator implements ConstraintValidator<EarthHasAtmosphere, Planet> {
        private static final Logger logger = Logger.getLogger(EarthHasAtmosphereValidator.class.getSimpleName());

        public void initialize(EarthHasAtmosphere constraintAnnotation) {}

        public boolean isValid(Planet _value, ConstraintValidatorContext context) {
            logger.info("isValid");
            if(_value == null) return true;
            Double dist = _value.getDistToEarth();
            if(dist == null) return true;
            if(dist == 0.0 && !_value.isAtmosphere())
                return false;
            return true;
        }
    }
}
