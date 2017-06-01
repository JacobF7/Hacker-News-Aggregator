package com.uom.assignment.batch.processor;

import com.uom.assignment.dao.Story;
import com.uom.assignment.dao.Topic;
import com.uom.assignment.dao.User;
import com.uom.assignment.dao.UserTopic;
import com.uom.assignment.model.request.CreateDigestModel;
import com.uom.assignment.model.request.TopStoryModel;
import com.uom.assignment.service.DurationType;
import com.uom.assignment.service.StoryService;
import com.uom.assignment.service.UserTopicService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * A test suite for {@link CreateDigestProcessor}.
 *
 * Created by jacobfalzon on 27/05/2017.
 */
@RunWith(MockitoJUnitRunner.class)
public class CreateDigestProcessorTest {

    @Mock
    private User mockUser;

    @Mock
    private UserTopic mockUserTopic;

    @Mock
    private Topic mockTopic;

    @Mock
    private Story mockFirstTopStory;

    @Mock
    private Story mockSecondTopStory;

    @Mock
    private StoryService storyService;

    @Mock
    private UserTopicService userTopicService;

    @InjectMocks
    private CreateDigestProcessor createDigestProcessor;

    private static final String TOPIC_NAME = "topic";
    private static final int N = 3;

    @Before
    public void setup() {
        ReflectionTestUtils.setField(createDigestProcessor, "N", N);
    }

    @Test
    public void process_userTopicsDoNotExist_returnsNull() {

        // Mocking that no user is currently subscribed to mockTopic
        Mockito.when(userTopicService.findEffectiveByTopic(mockTopic)).thenReturn(Collections.emptySet());

        final List<CreateDigestModel> createDigestModels = createDigestProcessor.process(mockTopic);

        // Verifying that attempt was NOT made to obtain the top stories for mockTopic since no user is currently subscribed to mockTopic
        Mockito.verify(storyService, Mockito.never()).findTopStoriesByTitleContainingAndCreationDate(Matchers.eq(mockTopic.getName()), Matchers.eq(DurationType.WEEKLY.getDuration()), Matchers.anyInt());

        // Verifying that null is returned
        Assert.assertNull(createDigestModels);
    }

    @Test
    public void process_topStoriesDoesNotExist_returnsNull() {

        // Mocking that mockUser is currently subscribed to mockTopic, i.e. mockUserTopic
        Mockito.when(userTopicService.findEffectiveByTopic(mockTopic)).thenReturn(Collections.singleton(mockUserTopic));

        // Mocking that mockUserTopic belongs to mockUser
        Mockito.when(mockUserTopic.getUser()).thenReturn(mockUser);

        // Mocking that mockTopic contains TOPIC_NAME
        Mockito.when(mockTopic.getName()).thenReturn(TOPIC_NAME);

        // Mocking that there is no top story for mockTopic
        Mockito.when(storyService.findTopStoriesByTitleContainingAndCreationDate(Matchers.eq(mockTopic.getName()), Matchers.eq(DurationType.WEEKLY.getDuration()), Matchers.anyInt())).thenReturn(Collections.emptyList());

        final List<CreateDigestModel> createDigestModels = createDigestProcessor.process(mockTopic);

        // Verifying that an attempt was made to obtain the top stories for mockTopic
        Mockito.verify(storyService).findTopStoriesByTitleContainingAndCreationDate(Matchers.eq(mockTopic.getName()), Matchers.eq(DurationType.WEEKLY.getDuration()), Matchers.anyInt());

        // Verifying that null is returned
        Assert.assertNull(createDigestModels);
    }

    @Test
    public void process_userTopicExists_topStoriesExist_returnsCreateDigestModels() {

        // Mocking that mockUser is currently subscribed to mockTopic, i.e. mockUserTopic
        Mockito.when(userTopicService.findEffectiveByTopic(mockTopic)).thenReturn(Collections.singleton(mockUserTopic));

        // Mocking that mockUserTopic belongs to mockUser
        Mockito.when(mockUserTopic.getUser()).thenReturn(mockUser);

        // Mocking that mockTopic contains TOPIC_NAME
        Mockito.when(mockTopic.getName()).thenReturn(TOPIC_NAME);

        // Mocking that mockFirstTopStory and mockSecondTopStory are the top stories for mockTopic
        Mockito.when(storyService.findTopStoriesByTitleContainingAndCreationDate(Matchers.eq(mockTopic.getName()), Matchers.eq(DurationType.WEEKLY.getDuration()), Matchers.anyInt())).thenReturn(Arrays.asList(mockFirstTopStory, mockSecondTopStory));

        final List<CreateDigestModel> createDigestModels = createDigestProcessor.process(mockTopic);

        // Verifying that an attempt was made to obtain the top stories for mockTopic
        Mockito.verify(storyService).findTopStoriesByTitleContainingAndCreationDate(Matchers.eq(mockTopic.getName()), Matchers.eq(DurationType.WEEKLY.getDuration()), Matchers.anyInt());

        // Verifying that createDigestModels two models
        Assert.assertEquals(2, createDigestModels.size());

        // firstDigestModel contains mockTopic, mockFirstTopStory and mockUser
        final CreateDigestModel firstDigestModel = new CreateDigestModel(mockTopic, mockFirstTopStory, Collections.singleton(mockUser));

        // secondDigestModel contains mockTopic, mockSecondTopStory and mockUser
        final CreateDigestModel secondDigestModel = new CreateDigestModel(mockTopic, mockSecondTopStory, Collections.singleton(mockUser));

        // Verifying that createDigestModels contains firstDigestModel and secondDigestModel
        Assert.assertEquals(createDigestModels, Arrays.asList(firstDigestModel, secondDigestModel));
    }

}
