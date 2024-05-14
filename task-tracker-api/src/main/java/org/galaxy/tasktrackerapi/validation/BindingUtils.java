package org.galaxy.tasktrackerapi.validation;

import lombok.experimental.UtilityClass;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;

@UtilityClass
public class BindingUtils {

    public void handleBindingResult(BindingResult bindingResult) throws BindException {
        if (bindingResult.hasErrors()) {
            if (bindingResult instanceof BindException exception) {
                throw exception;
            } else {
                throw new BindException(bindingResult);
            }
        }
    }
}
