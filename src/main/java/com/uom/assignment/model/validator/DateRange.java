package com.uom.assignment.model.validator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

/**
 * A constraint annotation to validate a date range.
 *
 * Created by jacobfalzon on 28/05/2017.
 */
@Documented
@Constraint(validatedBy = DateRangeValidator.class)
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface DateRange {

    String message() default "com.uom.assignment.model.validator.DateRange";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
