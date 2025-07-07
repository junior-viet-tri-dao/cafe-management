package com.vn.validator;


import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import jakarta.validation.Payload;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Documented
@jakarta.validation.Constraint(validatedBy = { DateAfterValidator.class })
@Target({ TYPE })
@Retention(RUNTIME)
public @interface DateAfter {
    String message() default "Ngày kết thúc phải sau ngày bắt đầu";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

    String startDateField();
    String endDateField();
}
