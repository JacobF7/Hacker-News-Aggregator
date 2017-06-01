package com.uom.assignment.service;

import com.uom.assignment.dao.Story;
import com.uom.assignment.dao.Topic;

import java.io.IOException;
import java.time.Duration;
import java.util.List;
import java.util.Optional;

/**
 * A service containing all {@link Story} related operations.
 *
 * Created by jacobfalzon on 20/05/2017.
 */
public interface StoryService {

    /**
     * Find a {@link Story} with the given {@link Story#hackerNewsId}.
     * Note that the {@link Story#hackerNewsId} is should be unique for any {@link Story}.
     *
     * @param hackerNewsId the {@link Story#hackerNewsId} of the desired {@link Story}.
     * @return an {@link Optional} containing the {@link Story} if a {@link Story} exists with the given {@code hackerNewsId}, otherwise {@link Optional#empty()}.
     */
    Optional<Story> findByHackerNewsId(Long hackerNewsId);


    /**
     * Creates a {@link Story} for the given {@link Story#hackerNewsId}, {@link Story#title}, {@link Story#author}, {@link Story#url} and {@link Story#score}.
     * Note that if the {@link Story} exists, it is refreshed with the latest {@link Story#score} using {@link StoryService#update}.
     *
     * @param hackerNewsId the Hacker News Id of the {@link Story}.
     * @param title the title of the {@link Story}.
     * @param author the author of the {@link Story}.
     * @param url the url of the {@link Story}.
     * @param score the score of the {@link Story}.
     * @param creationDate the creation date of the {@link Story}.
     * @return the {@link Story} that is created or updated.
     */
    Story createOrUpdate(Long hackerNewsId, String title, String author, String url, Long score, Long creationDate);

    /**
     * Updates the {@link Story#score} of the given {@link Story}.
     *
     * @param story the {@link Story} to be updated.
     * @param score the new {@link Story#score} of the {@link Story}.
     * @return the {@link Story} that is updated.
     */
    Story update(Story story, Long score);

    /**
     * Updates the given {@link Story} to be marked as {@link Story#deleted}.
     *
     * @param story the {@link Story} to be updated.
     * @return the {@link Story} that is updated.
     */
    Story delete(Story story);

    /**
     * Retrieves every persisted {@link Story}.
     * Note that any {@link Story} that is {@link Story#deleted} is ignored.
     *
     * @return a {@link List} containing ever persisted {@link Story}.
     */
    List<Story> findAllActive();

    /**
     * Retrieves every persisted {@link Story} where the duration between now and {@link Story#lastUpdated} is NOT longer than specified {@link Duration}.
     * Note that any {@link Story} that is {@link Story#deleted} is ignored.
     *
     * @param duration the {@link Duration} of time.
     * @return a {@link List} containing ever persisted {@link Story} where the duration between now and {@link Story#lastUpdated} is NOT longer than specified {@link Duration}.
     */
    List<Story> findActiveByLastUpdatedDuration(Duration duration);

    /**
     * Retrieves every persisted {@link Story} where the duration between now and {@link Story#creationDate} is NOT longer than specified {@link Duration}.
     * Note that any {@link Story} that is {@link Story#deleted} is ignored.
     *
     * @param duration the {@link Duration} of time.
     * @return a {@link List} containing ever persisted {@link Story} where the duration between now and {@link Story#creationDate} is NOT longer than specified {@link Duration}.
     */
    List<Story> findActiveByCreationDateDuration(Duration duration);

    /**
     * Retrieve the top {@link Story} which has the {@link Story#title} containing {@code topicName}.
     * Note that any {@link Story} that is {@link Story#deleted} is ignored.
     *
     * @param topicName the name of the {@link Topic}.
     * @return the top {@link Story} which has the {@link Story#title} containing {@code topicName}.
     */
    @Deprecated // TODO REMOVE
    Optional<Story> findTopStoryByTitleContaining(String topicName);

    /**
     * Retrieve the top {@link Story} which has the {@link Story#title} containing {@code topicName}.
     * Note that the {@link Duration} between now and the {@link Story#creationDate} must NOT be longer than the specified {@link Duration}.
     * Note that any {@link Story} that is {@link Story#deleted} is ignored.
     *
     * @param topicName the name of the {@link Topic}.
     * @param duration the {@link Duration} of time.
     * @return the top {@link Story} which has the {@link Story#title} containing {@code topicName}.
     */
    Optional<Story> findTopStoryByTitleContainingAndCreationDate(String topicName, Duration duration);

    /**
     * Retrieve the overall Top {@code n} Stories.
     * Note that the {@link Duration} between now and the {@link Story#creationDate} must NOT be longer than the specified {@link Duration}.
     * Note that any {@link Story} that is {@link Story#deleted} is ignored.
     *
     * @param duration the {@link Duration} of time.
     * @param n the number of Top Stories to be returned.
     * @return a {@link List} containing the Top {@code n} Stories.
     */
    List<Story> findOverallTopStoriesByCreationDate(Duration duration, int n);

    /**
     * Retrieve the Top {@code n} Stories which have the {@link Story#title} containing {@code topicName}.
     * Note that the {@link Duration} between now and the {@link Story#creationDate} must NOT be longer than the specified {@link Duration}.
     * Note that any {@link Story} that is {@link Story#deleted} is ignored.
     *
     * @param topicName the name of the {@link Topic}.
     * @param duration the {@link Duration} of time.
     * @param n the number of Top Stories to be returned.
     * @return a {@link List} containing the Top {@code n} Stories.
     */
    List<Story> findTopStoriesByTitleContainingAndCreationDate(String topicName, Duration duration, int n);
}
