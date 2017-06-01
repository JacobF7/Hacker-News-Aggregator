package com.uom.assignment.model.request;

import com.uom.assignment.dao.Story;
import com.uom.assignment.dao.Topic;
import com.uom.assignment.dao.User;

import java.util.Objects;
import java.util.Set;

/**
 * Created by jacobfalzon on 31/05/2017.
 */
public class CreateDigestModel {

    private final Topic topic;
    private final Story story;
    private final Set<User> users;

    public CreateDigestModel(final Topic topic, final Story story, final Set<User> users) {
        this.topic = topic;
        this.story = story;
        this.users = users;
    }

    public Topic getTopic() {
        return topic;
    }

    public Story getStory() {
        return story;
    }

    public Set<User> getUsers() {
        return users;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        final CreateDigestModel that = (CreateDigestModel) o;
        return Objects.equals(getTopic(), that.getTopic()) &&
                Objects.equals(getStory(), that.getStory()) &&
                Objects.equals(getUsers(), that.getUsers());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getTopic(), getStory(), getUsers());
    }

}
