package com.uom.assignment.model.request;

import com.uom.assignment.dao.Topic;
import com.uom.assignment.dao.User;

import javax.validation.constraints.NotNull;
import java.util.Set;

/**
 * The model containing the new {@link Set} of {@link Topic}s that a {@link User} wishes to subscribe to.
 *
 * Created by jacobfalzon on 09/05/2017.
 */
public class TopicModel {

    @NotNull
    private Set<String> topics;

    public TopicModel(final Set<String> topics) {
        this.topics = topics;
    }

    public TopicModel() {
    }

    public Set<String> getTopics() {
        return topics;
    }
}
