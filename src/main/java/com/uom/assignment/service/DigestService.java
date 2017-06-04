package com.uom.assignment.service;

import com.uom.assignment.dao.Digest;
import com.uom.assignment.dao.Story;
import com.uom.assignment.dao.Topic;
import com.uom.assignment.dao.User;

import java.time.*;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
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
     * Note that the returned {@link Digest}s must either be:
     *          a) An {@link Digest#overall} {@link Digest} or
     *          b) The {@link Digest#topic} is contained in {@link User#getUserTopics()}}.
     *
     * @param user the {@link User} for which the {@link Digest}s were created.
     * @param start the start {@link LocalDate}.
     * @param end the end {@link LocalDate}.
     * @return a {@link Map} of {@link Digest}s associated to the given {@link User} grouped by {@link Digest#creationDate}.
     */
    Map<LocalDate, List<Digest>> findDigests(User user, LocalDate start, LocalDate end);

    /**
     * Retrieve a set of the latest {@link Digest}s.
     * Note that the returned {@link Digest}s must either be:
     *          a) An {@link Digest#overall} {@link Digest} or
     *          b) The {@link Digest#topic} is contained in {@link User#getUserTopics()}}.
     *
     * @param user the {@link User} for which the {@link Digest}s were created.
     * @return a {@link Collections#singletonMap} of the {@link Digest}s for a particular {@link Digest#creationDate}.
     */
    Map<LocalDate, List<Digest>> findLatestDigests(User user);

    /**
     * Deletes any expired {@link Digest}, i.e. any {@link Digest} where the duration between now and {@link Digest#creationDate} is longer than the default {@link Digest} expiration time.
     *
     * @return a {@link Set} of {@link Digest#id} for every expired {@link Digest} that is deleted.
     */
    Set<Long> deleteExpiredDigests();

    /**
     * Determines whether the given {@link Digest} is expired, i.e. the duration between now and {@link Digest#creationDate} is longer than the default {@link Digest} expiration time.
     *
     * @param digest the {@link Digest} to check for expiration.
     * @return true if the given {@link Digest} is expired, false otherwise.
     */
    static boolean isDigestExpired(Digest digest) {
        return Period.between(toLocalDate(digest.getCreationDate()), toLocalDate(Instant.now().truncatedTo(ChronoUnit.DAYS).toEpochMilli())).getYears() >= getDigestExpiryTime().getYears();
    }

    /**
     * @return the default {@link Digest} expiry {@link Period} in Years.
     */
    static Period getDigestExpiryTime() {
        return Period.ofYears(1);
    }

    /**
     * Converts an Epoch {@code date} to {@link LocalDate}.
     *
     * @param date the date in Epoch milliseconds.
     * @return the converted {@code date} as a {@link LocalDate}.
     */
    static LocalDate toLocalDate(final Long date) {
        return Instant.ofEpochMilli(date).atZone(ZoneOffset.UTC).toLocalDate();
    }

    /**
     * Converts a {@link LocalDate} to Epoch milliseconds.
     *
     * @param date the {@link LocalDate}.
     * @return the converted {@code date} in Epoch milliseconds.
     */
    static Long toEpoch(final LocalDate date) {
        return date.atStartOfDay(ZoneOffset.UTC).toInstant().toEpochMilli();
    }
}
