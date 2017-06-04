package com.uom.assignment.model.validator;

import com.uom.assignment.model.request.DateRangeModel;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Objects;

/**
 * A constraint validator serving to validate a {@link DateRangeModel}.
 *
 * Created by jacobfalzon on 28/05/2017.
 */
@Component
public class DateRangeValidator implements ConstraintValidator<DateRange, DateRangeModel> {

    @Override
    public void initialize(final DateRange dateRange) {}

    @Override
    public boolean isValid(final DateRangeModel model, final ConstraintValidatorContext context) {

        if(Objects.isNull(model.getStart()) || Objects.isNull(model.getEnd())) {
            // These validations are carried out in the model itself
            return false;
        }

        if(model.getStart().isAfter(model.getEnd())){
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("Start Date cannot occur after End Date").addPropertyNode("start").addConstraintViolation();
            return false;
        }

        return true;
    }
}
