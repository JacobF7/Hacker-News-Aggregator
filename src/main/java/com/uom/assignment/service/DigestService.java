package com.uom.assignment.service;

import com.uom.assignment.dao.Digest;
import com.uom.assignment.dao.Story;
import com.uom.assignment.dao.Topic;

import java.time.Duration;

/**
 * A service containing all {@link Digest} related operations.
 *
 * Created by jacobfalzon on 27/05/2017.
 */
public interface DigestService {

    /**
     * Create a {@link Digest} for the top {@link Story} of a particular {@link Topic}.
     * Note that the {@link DurationType} determines the {@link Duration} span of the {@link Digest}.
     *
     * @param topic the {@link Topic} for which the {@link Digest} is created.
     * @param durationType the {@link DurationType}.
     * @return a {@link Digest} for the top {@link Story} of a particular {@link Topic}.
     */
    Digest create(Topic topic, Story story, DurationType durationType);
}
