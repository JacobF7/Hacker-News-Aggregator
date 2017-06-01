package com.uom.assignment.batch.processor;

import com.uom.assignment.dao.Story;
import com.uom.assignment.dao.Topic;
import com.uom.assignment.dao.User;
import com.uom.assignment.dao.UserTopic;
import com.uom.assignment.model.request.CreateDigestModel;
import com.uom.assignment.service.DurationType;
import com.uom.assignment.service.StoryService;
import com.uom.assignment.service.UserTopicService;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * The {@link ItemProcessor} that is responsible for transforming a {@link Topic} into a {@link List} of {@link CreateDigestModel}s.
 *
 * Created by jacobfalzon on 27/05/2017.
 */
@Component
public class CreateDigestProcessor implements ItemProcessor<Topic, List<CreateDigestModel>> {

    private static final Logger LOG = LoggerFactory.getLogger(CreateDigestProcessor.class);

    private final StoryService storyService;
    private final UserTopicService userTopicService;

    @Value("${digest.top.stories.count}")
    private Integer N; // Top N Stories, i.e. 3 by default

    @Autowired
    public CreateDigestProcessor(final StoryService storyService, final UserTopicService userTopicService) {
        this.storyService = storyService;
        this.userTopicService = userTopicService;
    }

    @Override
    public List<CreateDigestModel> process(final Topic topic) {

        // All users currently subscribed to the topic
        final Set<User> subscribedUsers = userTopicService.findEffectiveByTopic(topic).stream().map(UserTopic::getUser).collect(Collectors.toSet());

        if(subscribedUsers.isEmpty()) {
            LOG.info("No Users subscribed to Topic {}", topic.getName());
            return null;
        }

        final List<Story> topStories = storyService.findTopStoriesByTitleContainingAndCreationDate(topic.getName(), DurationType.WEEKLY.getDuration(), N);

        // If no top stories exist for the topic users currently subscribed to the topic
        if(topStories.isEmpty()) {
            LOG.info("No top story exists for Topic {}", topic.getName());
            return null;
        }

        LOG.info("The top stories for Topic {} and Users {} are {}", topic.getName(), subscribedUsers.stream().map(User::getId).collect(Collectors.toSet()), topStories);

        return topStories.stream().map(topStory -> new CreateDigestModel(topic, topStory, subscribedUsers)).collect(Collectors.toList());
    }

}
