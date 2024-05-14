package org.galaxy.tasktrackerapi.validation.impl;


import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.galaxy.tasktrackerapi.validation.FieldEquals;

public class FieldEqualsValidator implements ConstraintValidator<FieldEquals, Object> {

    private String field;
    private String equalsTo;

    @Override
    public void initialize(FieldEquals constraintAnnotation) {
        field = constraintAnnotation.field();
        equalsTo = constraintAnnotation.equalsTo();
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        var fieldValue = FieldValueUtil.getFieldValue(value, field);
        var equalsToValue = FieldValueUtil.getFieldValue(value, equalsTo);
        if (fieldValue == null && equalsToValue == null) {
            return true;
        }
        return fieldValue.equals(equalsToValue);
    }
}
