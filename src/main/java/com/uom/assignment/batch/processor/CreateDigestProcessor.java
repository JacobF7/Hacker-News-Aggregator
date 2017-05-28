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

import java.util.Optional;

/**
 * The {@link ItemProcessor} that is responsible for transforming a {@link Topic} into an {@link TopStoryModel}.
 *
 * Created by jacobfalzon on 27/05/2017.
 */
@Component
public class CreateDigestProcessor implements ItemProcessor<Topic, TopStoryModel> {

    private static final Logger LOG = LoggerFactory.getLogger(CreateDigestProcessor.class);

    private final StoryService storyService;

    @Autowired
    public CreateDigestProcessor(final StoryService storyService) {
        this.storyService = storyService;
    }

    @Override
    public TopStoryModel process(final Topic topic) {

        final Optional<Story> topStory = storyService.findTopStoryByTitleContainingAndCreationDate(topic.getName(), DurationType.WEEKLY.getDuration());

        if(topStory.isPresent()) {
            LOG.info("The top story for Topic {} is {}", topic.getName(), topStory.get().getTitle());
        } else {
            LOG.info("No top story exists for Topic {}", topic.getName());
        }

        return new TopStoryModel(topic, topStory.orElse(null));
    }

}
