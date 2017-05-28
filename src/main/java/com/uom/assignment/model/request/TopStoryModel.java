package com.uom.assignment.model.request;

import com.uom.assignment.dao.Story;
import com.uom.assignment.dao.Topic;

import java.util.Objects;

/**
 * A model serving as placeholder for a {@link Topic} to be updated.
 *
 * Created by jacobfalzon on 23/05/2017.
 */
public class TopStoryModel {

    private final Topic topic;
    private final Story story;


    public TopStoryModel(final Topic topic, final Story story) {
        this.topic = topic;
        this.story = story;
    }

    public Topic getTopic() {
        return topic;
    }

    public Story getStory() {
        return story;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        final TopStoryModel that = (TopStoryModel) o;
        return Objects.equals(getTopic(), that.getTopic()) &&
                Objects.equals(getStory(), that.getStory());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getTopic(), getStory());
    }
}
