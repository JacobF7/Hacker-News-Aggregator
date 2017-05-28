package com.uom.assignment.service;

import com.uom.assignment.dao.Story;
import com.uom.assignment.repository.StoryRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * The {@link Service} implementation for the {@link Story} entity.
 *
 * Created by jacobfalzon on 20/05/2017.
 */
@Service
@Transactional
public class StoryServiceImpl implements StoryService {

    private final StoryRepository storyRepository;

    public StoryServiceImpl(final StoryRepository storyRepository) {
        this.storyRepository = storyRepository;
    }

    @Override
    public Optional<Story> findByHackerNewsId(final Long hackerNewsId) {
        return storyRepository.findByHackerNewsId(hackerNewsId);
    }

    @Override
    public Story createOrUpdate(final Long hackerNewsId, final String title, final String author, final String url, final Long score, final Long creationDate) {
        // If the Story exists, update the score of the Story, otherwise create the story.
        return findByHackerNewsId(hackerNewsId).map(story -> update(story, score)).orElseGet(() -> storyRepository.save(new Story(hackerNewsId, title, author, url, score, creationDate)));
    }

    @Override
    public Story update(final Story story, final Long score) {
       story.setScore(score);
       story.setLastUpdated(System.currentTimeMillis());
       return storyRepository.save(story);
    }

    @Override
    public Story delete(final Story story) {
        story.setDeleted(true);
        story.setLastUpdated(System.currentTimeMillis());
        return storyRepository.save(story);
    }

    @Override
    public List<Story> findAllActive() {
        return storyRepository.findAll().stream().filter(Story::isActive).collect(Collectors.toList());
    }

    @Override
    public List<Story> findActiveByLastUpdatedDuration(final Duration duration) {
        return storyRepository.findByLastUpdatedAfter(System.currentTimeMillis() - duration.toMillis()).stream().filter(Story::isActive).collect(Collectors.toList());
    }

    @Override
    public List<Story> findActiveByCreationDateDuration(final Duration duration) {
        return storyRepository.findByCreationDateAfter(System.currentTimeMillis() - duration.toMillis()).stream().filter(Story::isActive).collect(Collectors.toList());
    }

    @Override
    public Optional<Story> findTopStoryByTitleContaining(final String topicName) {
        return storyRepository.findByTitleContaining(topicName)
                              .stream()
                              .filter(Story::isActive)
                              .max(Comparator.comparing(Story::getScore));
    }

    @Override
    public Optional<Story> findTopStoryByTitleContainingAndCreationDate(final String topicName, final Duration duration) {
        return findActiveByCreationDateDuration(duration).stream()
                                                         .filter(Story::isActive)
                                                         .filter(story -> StringUtils.containsIgnoreCase(story.getTitle(), topicName))
                                                         .max(Comparator.comparing(Story::getScore));
    }

}
