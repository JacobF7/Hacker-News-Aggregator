package com.uom.assignment.service;

import com.uom.assignment.dao.Digest;
import com.uom.assignment.dao.Story;
import com.uom.assignment.dao.Topic;
import com.uom.assignment.dao.User;

import java.time.Duration;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * A service containing all {@link Digest} related operations.
 *
 * Created by jacobfalzon on 27/05/2017.
 */
public interface DigestService {

    /**
     * Create a {@link Digest} for the Top {@link Story} of a particular {@link Topic} that a {@link Set} of {@link User}s are currently subscribed to.
     * Note that the {@link DurationType} determines the {@link Duration} span of the {@link Digest}.
     *
     * @param topic the {@link Topic} for which the {@link Digest} is created.
     * @param story the Top {@link Story} of a particular {@link Topic}.
     * @param users the {@link Set} of {@link User}s associated to the {@link Digest}.
     * @param durationType the {@link DurationType}.
     * @return a {@link Digest} for the Top {@link Story} of a particular {@link Topic}.
     */
    Digest createTopicDigest(Topic topic, Story story, Set<User> users, DurationType durationType);

    /**
     * Create a {@link Digest} for an Overall Top {@link Story} and for the given {@link Set} of {@link User}s .
     * Note that the {@link DurationType} determines the {@link Duration} span of the {@link Digest}.
     *
     * @param durationType the {@link DurationType}.
     * @param story the overall Top {@link Story}.
     * @param users the {@link Set} of {@link User}s associated to the {@link Digest}.
     * @return a {@link Digest} for an Overall Top {@link Story}.
     */
    Digest createOverallDigest(Story story, Set<User> users, DurationType durationType);

    /**
     * Retrieve a set of {@link Digest}s where the {@link Digest#creationDate} occurs after {@code start} and before {@code end}.
     * Note that the returned {@link Digest}s must either be associated to the given {@link User}.
     *
     * @param user the {@link User} for which the {@link Digest}s were created.
     * @param start the start {@link LocalDate}.
     * @param end the end {@link LocalDate}.
     * @return a {@link Map} of {@link Digest}s associated to the given {@link User} grouped by {@link Digest#creationDate}.
     */
    Map<LocalDate, List<Digest>> findDigests(User user, LocalDate start, LocalDate end);
}
