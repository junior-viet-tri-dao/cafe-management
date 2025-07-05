package com.viettridao.cafe.exception;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

@Documented
@Constraint(validatedBy = OptionalSizeValidator.class)
@Target({ ElementType.METHOD, ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface OptionalSize {
	String message() default "Trường phải có tối thiểu ký tự nếu được nhập";

	int min() default 0;

	int max() default Integer.MAX_VALUE;

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};
}
