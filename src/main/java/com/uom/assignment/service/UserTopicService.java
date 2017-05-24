package com.uom.assignment.service;

import com.uom.assignment.dao.Topic;
import com.uom.assignment.dao.User;

import java.util.Set;

/**
 * A service dedicated towards handling the relationship between {@link User}s and {@link Topic}s.
 *
 * Created by jacobfalzon on 11/05/2017.
 */
public interface UserTopicService {

    // TODO TRANSACTION SCOPE
    /**
     * Subscribe a {@link User} to a {@link Set} of {@link Topic}s.
     * Note that this action may unsubscribe the {@link User} from any previously subscribed {@link Topic}s.
     *
     * @param user the {@link User} that wishes to subscribe to a particular {@link Set} of {@link Topic}s.
     * @param topics the {@link Set} of {@link Topic}s which the {@link User} wishes to subscribe to.
     */
    void subscribe(User user, Set<Topic> topics);

}
