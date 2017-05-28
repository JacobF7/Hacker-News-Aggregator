package com.uom.assignment.batch.writer;

import com.uom.assignment.dao.Topic;
import com.uom.assignment.model.request.TopStoryModel;
import com.uom.assignment.service.TopicService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * The {@link ItemWriter} that is responsible for updating any persisted {@link Topic}.
 *
 * Created by jacobfalzon on 23/05/2017.
 */
@Component
public class UpdateTopStoriesWriter implements ItemWriter<TopStoryModel> {

    private static final Logger LOG = LoggerFactory.getLogger(UpdateTopStoriesWriter.class);

    private final TopicService topicService;

    @Autowired
    public UpdateTopStoriesWriter(final TopicService topicService) {
        this.topicService = topicService;
    }

    @Override
    public void write(final List<? extends TopStoryModel> updateTopicModels) {
        updateTopicModels.stream()
                         .map(updateTopicModel -> topicService.update(updateTopicModel.getTopic(), updateTopicModel.getStory()))
                         .forEach(topic -> LOG.info("Updated Top Story of Topic {} to {}", topic.getName(), topic.getTopStory()));
    }
}

