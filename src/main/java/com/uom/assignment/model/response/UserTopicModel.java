package com.uom.assignment.model.response;

import com.uom.assignment.dao.Topic;
import com.uom.assignment.dao.User;
import com.uom.assignment.service.TopicService;

import java.util.Set;
import java.util.stream.Collectors;

/**
 * The model containing the {@link User#id} and the subscribed {@link Topic#name}s, which are returned as response when creating/updating a {@link User}.
 *
 * Created by jacobfalzon on 14/05/2017.
 */
public class UserTopicModel {

    private final Long userId;
    private final Set<String> topics;

    public UserTopicModel(final Long userId, final Set<String> topics) {
        this.userId = userId;
        this.topics = topics.stream().map(TopicService::sanitize).collect(Collectors.toSet());
    }

    public Long getUserId() {
        return userId;
    }

    public Set<String> getTopics() {
        return topics;
    }
}
