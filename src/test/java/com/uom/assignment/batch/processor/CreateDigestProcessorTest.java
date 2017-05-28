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
 * A test suite for {@link CreateDigestProcessor}.
 *
 * Created by jacobfalzon on 27/05/2017.
 */
@RunWith(MockitoJUnitRunner.class)
public class CreateDigestProcessorTest {

    @Mock
    private Topic mockTopic;

    @Mock
    private Story mockTopStory;

    @Mock
    private StoryService storyService;

    @InjectMocks
    private CreateDigestProcessor createDigestProcessor;

    private static final String TOPIC_NAME = "topic";

    @Test
    public void process_topStoryExists_returnsTopStoryModel() {

        // Mocking that mockTopic contains TOPIC_NAME
        Mockito.when(mockTopic.getName()).thenReturn(TOPIC_NAME);

        // Mocking that mockTopStory is the new top story for mockTopic
        Mockito.when(storyService.findTopStoryByTitleContainingAndCreationDate(mockTopic.getName(), DurationType.WEEKLY.getDuration())).thenReturn(Optional.of(mockTopStory));

        final TopStoryModel topStoryModel = createDigestProcessor.process(mockTopic);

        // Verifying that an attempt was made to obtain the top story for mockTopic
        Mockito.verify(storyService).findTopStoryByTitleContainingAndCreationDate(mockTopic.getName(), DurationType.WEEKLY.getDuration());

        // Verifying that topStoryModel contains mockTopic and mockTopStory
        Assert.assertEquals(new TopStoryModel(mockTopic, mockTopStory), topStoryModel);
    }

    @Test
    public void process_topStoryDoesNotExists_returnsTopStoryModel() {

        // Mocking that mockTopic contains TOPIC_NAME
        Mockito.when(mockTopic.getName()).thenReturn(TOPIC_NAME);

        // Mocking that oldTopStory is still the top story for mockTopic
        Mockito.when(storyService.findTopStoryByTitleContainingAndCreationDate(mockTopic.getName(), DurationType.WEEKLY.getDuration())).thenReturn(Optional.empty());

        final TopStoryModel topStoryModel = createDigestProcessor.process(mockTopic);

        // Verifying that an attempt was made to obtain the top story for mockTopic
        Mockito.verify(storyService).findTopStoryByTitleContainingAndCreationDate(mockTopic.getName(), DurationType.WEEKLY.getDuration());

        // Verifying that topStoryModel contains mockTopic and no top story, i.e. null
        Assert.assertEquals(new TopStoryModel(mockTopic, null), topStoryModel);
    }
}
