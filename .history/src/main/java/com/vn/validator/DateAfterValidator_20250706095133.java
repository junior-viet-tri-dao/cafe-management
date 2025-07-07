package com.vn.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.lang.reflect.Field;
import java.util.Date;

public class DateAfterValidator implements ConstraintValidator<DateAfter, Object> {
    private String startDateField;
    private String endDateField;

    @Override
    public void initialize(DateAfter constraintAnnotation) {
        this.startDateField = constraintAnnotation.startDateField();
        this.endDateField = constraintAnnotation.endDateField();
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        try {
            Field startField = value.getClass().getDeclaredField(startDateField);
            Field endField = value.getClass().getDeclaredField(endDateField);
            startField.setAccessible(true);
            endField.setAccessible(true);
            Object startObj = startField.get(value);
            Object endObj = endField.get(value);
            if (startObj == null || endObj == null) return true;
            if (!(startObj instanceof Date) || !(endObj instanceof Date)) return true;
            Date start = (Date) startObj;
            Date end = (Date) endObj;
            return end.after(start);
        } catch (Exception e) {
            return true;
        }
    }
}
