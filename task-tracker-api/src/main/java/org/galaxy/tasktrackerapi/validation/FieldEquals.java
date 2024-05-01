package org.galaxy.tasktrackerapi.validation;


import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import org.galaxy.tasktrackerapi.validation.impl.FieldEqualsValidator;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target(TYPE)
@Retention(RUNTIME)
@Constraint(validatedBy = FieldEqualsValidator.class)
public @interface FieldEquals  {

    String message() default "{error.fields.notMatches}";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
    String field();
    String equalsTo();
}
