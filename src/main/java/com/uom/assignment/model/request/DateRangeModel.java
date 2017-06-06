package com.uom.assignment.model.request;

import com.uom.assignment.model.validator.DateRange;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.Objects;

/**
 * The model containing a date range, i.e. a start {@link LocalDate} until an end {@link LocalDate}.
 *
 * Created by jacobfalzon on 28/05/2017.
 */
@DateRange
public class DateRangeModel {

    @NotNull
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate start;

    @NotNull
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate end;

    public DateRangeModel() {
    }

    public DateRangeModel(final LocalDate start, final LocalDate end) {
        this.start = start;
        this.end = end;
    }

    public LocalDate getStart() {
        return start;
    }

    public void setStart(final LocalDate start) {
        this.start = start;
    }

    public LocalDate getEnd() {
        return end;
    }

    public void setEnd(final LocalDate end) {
        this.end = end;
    }

}
