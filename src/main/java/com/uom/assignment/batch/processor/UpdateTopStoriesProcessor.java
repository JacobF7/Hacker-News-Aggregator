package com.uom.assignment.batch.processor;

import com.uom.assignment.dao.Story;
import com.uom.assignment.dao.Topic;
import com.uom.assignment.model.request.TopStoryModel;
import com.uom.assignment.service.DurationType;
import com.uom.assignment.service.StoryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Objects;

/**
 * The {@link ItemProcessor} that is responsible for transforming a {@link Topic} into an {@link TopStoryModel}.
 * The {@link Topic} is updated by resolving the the top {@link Story} associated to the specified {@link Topic}.
 *
 * Created by jacobfalzon on 23/05/2017.
 */
@Component
public class UpdateTopStoriesProcessor implements ItemProcessor<Topic, TopStoryModel> {

    private static final Logger LOG = LoggerFactory.getLogger(UpdateTopStoriesProcessor.class);
    private final StoryService storyService;

    @Autowired
    public UpdateTopStoriesProcessor(final StoryService storyService) {
        this.storyService = storyService;
    }

    @Override
    public TopStoryModel process(final Topic topic) {
        final Story topStory = storyService.findTopStoryByTitleContainingAndCreationDate(topic.getName(), DurationType.DAILY.getDuration()).orElse(null);

        // Update the topStory of a topic if the top story changed
        if(!Objects.equals(topic.getTopStory(), topStory)) {
            return new TopStoryModel(topic, topStory);
        }

        LOG.info("Top story for Topic: [{}] has not changed", topic.getName());
        return null;
    }
}
