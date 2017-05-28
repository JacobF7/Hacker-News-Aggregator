package com.uom.assignment.service;

import com.uom.assignment.dao.Digest;

import java.time.Duration;

/**
 * The {@link Digest} type, containing the span of the {@link Digest}.
 *
 * Created by jacobfalzon on 27/05/2017.
 */
public enum DurationType {

    DAILY(Duration.ofDays(1)),
    WEEKLY(Duration.ofDays(7));

    private Duration duration;

    DurationType(final Duration duration) {
        this.duration = duration;
    }

    public Duration getDuration() {
        return duration;
    }
}
