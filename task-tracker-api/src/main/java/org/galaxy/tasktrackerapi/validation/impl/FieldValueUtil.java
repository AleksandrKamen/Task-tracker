package org.galaxy.tasktrackerapi.validation.impl;

import lombok.experimental.UtilityClass;

@UtilityClass
public class FieldValueUtil {
    public Object getFieldValue(Object object, String fieldName) {
        try {
            var field = object.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            return field.get(object);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
            return null;
        }
    }
}
