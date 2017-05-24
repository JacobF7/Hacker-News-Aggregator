package com.uom.assignment.model.request;

import com.uom.assignment.dao.Story;
import com.uom.assignment.dao.Topic;

/**
 * Created by jacobfalzon on 23/05/2017.
 */
public class UpdateTopicModel {

    private final Topic topic;
    private final Story story;


    public UpdateTopicModel(final Topic topic, final Story story) {
        this.topic = topic;
        this.story = story;
    }

    public Topic getTopic() {
        return topic;
    }

    public Story getStory() {
        return story;
    }
}
