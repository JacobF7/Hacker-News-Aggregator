package com.uom.assignment.service;

import com.uom.assignment.controller.BusinessErrorException;
import com.uom.assignment.dao.Digest;
import com.uom.assignment.dao.Story;
import com.uom.assignment.dao.Topic;
import com.uom.assignment.dao.User;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

/**
 * A service containing all {@link User} related operations.
 *
 * Created by jacobfalzon on 29/04/2017.
 */
public interface UserService {

    /**
     * Retrieves every persisted {@link User}.
     *
     * @return a {@link List} containing ever persisted {@link User}.
     */
    List<User> findAll();

    /**
     * Creates a new {@link User} with the specified {@code username} and {@code password}.
     *
     * @param username the {@link User#username} for the new {@link User}.
     * @param password the {@link User#password} for the new {@link User}.
     * @param topics the {@link Set} of {@link Topic}s which the new {@link User} wishes to subscribe to.
     * @return the {@link User#id} of the new {@link User}.
     * @throws BusinessErrorException when the specified {@code username} already exists.
     */
    Long create(String username, String password, Set<String> topics);

    /**
     * Find a {@link User} with the given {@code username}.
     * Note that the {@link User#username} is unique across all users.
     *
     * @param username the {@link User#username} of the desired {@link User}.
     * @return an {@link Optional} containing the {@link User} if a {@link User} exists with the given {@code username}, otherwise {@link Optional#empty()}.
     */
    Optional<User> findByUsername(String username);

    /**
     * Updates the {@link Set} of {@link Topic}s that a particular {@link User} is subscribed to.
     *
     * @param id the {@link User#id} of the {@link User} that wishes to change the {@link Set} of {@link Topic}s.
     * @param topics the {@link Set} of {@link Topic}s  which the {@link User} wishes to subscribe to.
     * @throws BusinessErrorException when the specified {@code id} does not belong to any {@link User}.
     */
    void update(Long id, Set<String> topics);

    /**
     * For each effective {@link Topic} that the {@link User} is subscribed to, determine the top {@link Story} associated to that {@link Topic}.
     * Note that a top {@link Story} cannot be {@link Story#deleted}.
     *
     * @param id the {@link User#id} of the {@link User} for which every top {@link Story} is to be retrieved.
     * @return a {@link Map} containing the top {@link Story} for each effective {@link Topic} that the {@link User} is subscribed to.
     * @throws BusinessErrorException when the specified {@code id} does not belong to any {@link User}.
     */
    Map<Topic, Story> getRealTimeTopStories(Long id);

    /**
     * For each effective {@link Topic} that the {@link User} is subscribed to, retrieve the {@link Topic#topStory} associated to that {@link Topic}.
     * Note that a top {@link Story} cannot be {@link Story#deleted}.
     *
     * @param id the {@link User#id} of the {@link User} for which every top {@link Story} is to be retrieved.
     * @return a {@link Map} containing the the {@link Topic#topStory}  for each effective {@link Topic} that the {@link User} is subscribed to.
     * @throws BusinessErrorException when the specified {@code id} does not belong to any {@link User}.
     */
    Map<Topic, Story> getTopStories(Long id);

    /**
     * Retrieve a set of {@link Digest}s where the {@link Digest#creationDate} occurs after {@code start} and before {@code end}.
     * Note that the returned {@link Digest}s must either be:
     *          a) An {@link Digest#overall} {@link Digest} or
     *          b) The {@link Digest#topic} is contained in {@link User#getUserTopics()}}.
     *
     * @param id the {@link User#id} of the {@link User} for which the {@link Digest}s are to be retrieved.
     * @param start the start {@link LocalDate}.
     * @param end the end {@link LocalDate}.
     * @return a {@link Map} of {@link Digest}s grouped by {@link Digest#creationDate}.
     */
    Map<LocalDate, List<Digest>> getDigests(Long id, LocalDate start, LocalDate end);
}
