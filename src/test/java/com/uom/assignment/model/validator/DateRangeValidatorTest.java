package com.uom.assignment.model.validator;

import com.uom.assignment.model.request.DateRangeModel;
import com.uom.assignment.service.StoryService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.time.LocalDate;
import java.util.Set;

/**
 * A test suite for {@link DateRangeValidator}.
 *
 * Created by jacobfalzon on 29/05/2017.
 */
@RunWith(MockitoJUnitRunner.class)
public class DateRangeValidatorTest {

    private static Validator VALIDATOR = Validation.buildDefaultValidatorFactory().getValidator();

    @Test
    public void isValid_effectiveFromAfterEffectiveTo_invalid_throwsException() {

        // effectiveFrom occurs after effectiveTo
        final LocalDate effectiveTo =  LocalDate.now();
        final LocalDate effectiveFrom = effectiveTo.plusDays(1);
        final DateRangeModel dateRangeModel = new DateRangeModel(effectiveFrom, effectiveTo);

        final Set<ConstraintViolation<DateRangeModel>> constraintViolations = VALIDATOR.validate(dateRangeModel);

        // Verifying that the error exists
        Assert.assertFalse(constraintViolations.isEmpty());
        Assert.assertEquals("Start Date cannot occur after End Date", constraintViolations.iterator().next().getMessage());
    }

    @Test
    public void isValid_effectiveFromBeforeEffectiveTo_valid() {

        // effectiveFrom occurs after effectiveTo
        final LocalDate effectiveFrom =  LocalDate.now();
        final LocalDate effectiveTo = effectiveFrom.plusDays(1);
        final DateRangeModel dateRangeModel = new DateRangeModel(effectiveFrom, effectiveTo);

        final Set<ConstraintViolation<DateRangeModel>> constraintViolations = VALIDATOR.validate(dateRangeModel);

        // Verifying that no error exists
        Assert.assertTrue(constraintViolations.isEmpty());
    }
}