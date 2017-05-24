package com.uom.assignment.repository;

import com.uom.assignment.dao.Story;
import com.uom.assignment.dao.Topic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.persistence.Entity;
import java.util.List;
import java.util.Optional;

/**
 * The {@link Repository} for the {@link Story} {@link Entity}.
 *
 * Created by jacobfalzon on 20/05/2017.
 */
@Repository
public interface StoryRepository extends JpaRepository<Story, Long> {

    /**
     * Find a {@link Story} with the given {@link Story#hackerNewsId}.
     * Note that the {@link Story#hackerNewsId} should be unique for any {@link Story}.
     *
     * @param hackerNewsId the Hacker News Id of the desired {@link Story}.
     * @return an {@link Optional} containing the {@link Story} if a {@link Story} exists with the given {@code hackerNewsId}, otherwise {@link Optional#empty()}.
     */
    Optional<Story> findByHackerNewsId(Long hackerNewsId);

    /**
     * Find every {@link Story} where {@link Story#lastUpdated} occurs after the specified {@code timestamp}.
     *
     * @param timestamp the timestamp after which any returned should {@link Story} occur.
     * @return a {@link List} containing every {@link Story} where {@link Story#lastUpdated} occurs after the specified {@code timestamp}.
     */
    List<Story> findByLastUpdatedAfter(Long timestamp);

    /**
     * Retrieve any {@link Story} which has the {@link Story#title} containing {@code topicName}.
     *
     * @param topicName the name of the {@link Topic}.
     * @return a {@link List} containing any {@link Story} which has the {@link Story#title} containing {@code topicName}.
     */
    List<Story> findByTitleContaining(String topicName);
}
