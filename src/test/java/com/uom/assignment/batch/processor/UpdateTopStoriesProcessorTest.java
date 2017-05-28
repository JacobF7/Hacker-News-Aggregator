package com.uom.assignment.batch.processor;

import com.uom.assignment.dao.Story;
import com.uom.assignment.dao.Topic;
import com.uom.assignment.model.request.TopStoryModel;
import com.uom.assignment.service.DurationType;
import com.uom.assignment.service.StoryService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Optional;

/**
 * A test suite for {@link UpdateTopStoriesProcessor}.
 *
 * Created by jacobfalzon on 24/05/2017.
 */
@RunWith(MockitoJUnitRunner.class)
public class UpdateTopStoriesProcessorTest {

    @Mock
    private Story oldTopStory;

    @Mock
    private Story newTopStory;

    @Mock
    private Topic mockTopic;

    @Mock
    private StoryService storyService;

    @InjectMocks
    private UpdateTopStoriesProcessor updateTopStoriesProcessor;

    private static final String TOPIC_NAME = "topic";

    @Test
    public void process_topStoryExists_topStoryChanges_returnsTopStoryModel() {

        // Mocking that mockTopic contains TOPIC_NAME
        Mockito.when(mockTopic.getName()).thenReturn(TOPIC_NAME);

        // Mocking that the top story for mockTopic is currently oldTopStory
        Mockito.when(mockTopic.getTopStory()).thenReturn(oldTopStory);

        // Mocking that newTopStory is the new top story for mockTopic
        Mockito.when(storyService.findTopStoryByTitleContainingAndCreationDate(mockTopic.getName(), DurationType.DAILY.getDuration())).thenReturn(Optional.of(newTopStory));

        final TopStoryModel topStoryModel = updateTopStoriesProcessor.process(mockTopic);

        // Verifying that an attempt was made to obtain the top story for mockTopic
        Mockito.verify(storyService).findTopStoryByTitleContainingAndCreationDate(mockTopic.getName(), DurationType.DAILY.getDuration());

        // Verifying that topStoryModel contains mockTopic and newTopStory
        Assert.assertEquals(new TopStoryModel(mockTopic, newTopStory), topStoryModel);
    }

    @Test
    public void process_topStoryExists_topStoryDoesNotChange_returnsNull() {

        // Mocking that mockTopic contains TOPIC_NAME
        Mockito.when(mockTopic.getName()).thenReturn(TOPIC_NAME);

        // Mocking that the top story for mockTopic is currently oldTopStory
        Mockito.when(mockTopic.getTopStory()).thenReturn(oldTopStory);

        // Mocking that oldTopStory is still the top story for mockTopic
        Mockito.when(storyService.findTopStoryByTitleContainingAndCreationDate(mockTopic.getName(), DurationType.DAILY.getDuration())).thenReturn(Optional.of(oldTopStory));

        final TopStoryModel topStoryModel = updateTopStoriesProcessor.process(mockTopic);

        // Verifying that an attempt was made to obtain the top story for mockTopic
        Mockito.verify(storyService).findTopStoryByTitleContainingAndCreationDate(mockTopic.getName(), DurationType.DAILY.getDuration());

        // Verifying that topStoryModel is null since topStory did not change
        Assert.assertNull(topStoryModel);
    }

    @Test
    public void process_topStoryDoesNotExist_topStoryChanges_returnsTopStoryModel() {

        // Mocking that mockTopic contains TOPIC_NAME
        Mockito.when(mockTopic.getName()).thenReturn(TOPIC_NAME);

        // Mocking that the top story for mockTopic is currently oldTopStory
        Mockito.when(mockTopic.getTopStory()).thenReturn(oldTopStory);

        // Mocking that mockTopic no longer has a top story, e.g. if oldTopStory got deleted
        Mockito.when(storyService.findTopStoryByTitleContainingAndCreationDate(mockTopic.getName(), DurationType.DAILY.getDuration())).thenReturn(Optional.empty());

        final TopStoryModel topStoryModel = updateTopStoriesProcessor.process(mockTopic);

        // Verifying that an attempt was made to obtain the top story for mockTopic
        Mockito.verify(storyService).findTopStoryByTitleContainingAndCreationDate(mockTopic.getName(), DurationType.DAILY.getDuration());

        // Verifying that topStoryModel contains mockTopic and no top story, i.e. null
        Assert.assertEquals(new TopStoryModel(mockTopic, null), topStoryModel);
    }

    @Test
    public void process_topStoryDoesNotExist_topStoryDoesNotChange_returnsNull() {

        // Mocking that mockTopic contains TOPIC_NAME
        Mockito.when(mockTopic.getName()).thenReturn(TOPIC_NAME);

        // Mocking that no top story exists for mockTopic
        Mockito.when(mockTopic.getTopStory()).thenReturn(null);

        // Mocking that mockTopic still has no top story associated to it
        Mockito.when(storyService.findTopStoryByTitleContainingAndCreationDate(mockTopic.getName(), DurationType.DAILY.getDuration())).thenReturn(Optional.empty());

        final TopStoryModel topStoryModel = updateTopStoriesProcessor.process(mockTopic);

        // Verifying that an attempt was made to obtain the top story for mockTopic
        Mockito.verify(storyService).findTopStoryByTitleContainingAndCreationDate(mockTopic.getName(), DurationType.DAILY.getDuration());

        // Verifying that topStoryModel is null since topStory did not change
        Assert.assertNull(topStoryModel);
    }

}
