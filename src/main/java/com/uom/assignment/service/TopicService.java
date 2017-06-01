package com.uom.assignment.service;

import com.uom.assignment.dao.Story;
import com.uom.assignment.dao.Topic;

import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * A Service containing {@link Topic} related operations.
 *
 * Created by jacobfalzon on 06/05/2017.
 */
public interface TopicService {

    /**
     * Find a {@link Topic} with the given {@link Topic#name}.
     * Note that the {@link Topic#name} is unique across all {@link Topic}s.
     *
     * @param name the {@link Topic#name} of the desired {@link Topic}.
     * @return an {@link Optional} containing the {@link Topic} if a {@link Topic} exists with the given {@code name}, otherwise {@link Optional#empty()}.
     */
    Optional<Topic> findByName(String name);

    /**
     * Creates a {@link Topic} for the given {@link Topic#name}.
     * Note that if a {@link Topic} with the given {@link Topic#name} already exists, it is fetched.
     * Note that topic names are trimmed and converted to lowercase before being persisted.
     *
     * @param topicName the name of the {@link Topic}.
     * @return the {@link Topic} that is created or fetched.
     */
    Topic create(String topicName);

    /**
     * Retrieves every persisted {@link Topic}.
     *
     * @return a {@link List} containing ever persisted {@link Topic}.
     */
    List<Topic> findAll();

    /**
     * Updates the {@link Topic#topStory} of the given {@link Topic}.
     *
     * @param topic the {@link Topic} to be updated.
     * @param topStory the {@link Story} to be set as the {@link Topic#topStory} for the specified {@link Topic}.
     * @return the {@link Topic} that is updated.
     */
    Topic update(Topic topic, Story topStory);

    /**
     * Sanitizes the given {@code topic} by converting to lowercase and trimming any trailing whitespaces.
     *
     * @param topic the topic to sanitize.
     * @return the sanitized topic.
     */
    static String sanitize(final String topic) {
        return topic.toLowerCase().trim();
    }

}
