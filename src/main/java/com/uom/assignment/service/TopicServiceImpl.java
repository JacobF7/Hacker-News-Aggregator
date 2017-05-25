package com.uom.assignment.service;

import com.uom.assignment.cache.CacheConfiguration;
import com.uom.assignment.dao.Story;
import com.uom.assignment.dao.Topic;
import com.uom.assignment.repository.TopicRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * The {@link Service} implementation for the {@link Topic} entity.
 *
 * Created by jacobfalzon on 06/05/2017.
 */
@Service
@Transactional
public class TopicServiceImpl implements TopicService {

    private static final Logger LOG = LoggerFactory.getLogger(TopicServiceImpl.class);

    private final StoryService storyService;
    private final TopicRepository topicRepository;

    @Autowired
    public TopicServiceImpl(final StoryService storyService, final TopicRepository topicRepository) {
        this.storyService = storyService;
        this.topicRepository = topicRepository;
    }

    @Override
    public Optional<Topic> findByName(final String name) {
        return topicRepository.findByName(name);
    }

    @Cacheable(value = CacheConfiguration.TOPICS_CACHE_KEY, key = "T(com.uom.assignment.service.TopicService).sanitize(#topic)")
    @Override
    public Topic create(final String topic) {

        LOG.info("Bypassing {} Cache for Key: [{}] ", CacheConfiguration.TOPICS_CACHE_KEY, TopicService.sanitize(topic));

        // Note that topics are trimmed and converted to lowercase.
        final String sanitizedTopic = TopicService.sanitize(topic);

        // If a topic already exists, fetch it, otherwise create a new one.
        return this.findByName(sanitizedTopic).orElseGet(() -> topicRepository.save(new Topic(sanitizedTopic, storyService.findTopStoryByTitleContaining(sanitizedTopic).orElse(null))));
    }

    @Override
    public List<Topic> findAll() {
        return topicRepository.findAll();
    }

    @Override
    public Topic update(final Topic topic, final Story topStory) {
        topic.setTopStory(topStory);
        return topicRepository.save(topic);
    }

}
