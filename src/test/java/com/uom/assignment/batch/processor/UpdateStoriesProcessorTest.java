package com.uom.assignment.batch.processor;

import com.uom.assignment.dao.Story;
import com.uom.assignment.hacker.news.api.request.ItemRequest;
import com.uom.assignment.hacker.news.api.response.EmptyResponse;
import com.uom.assignment.hacker.news.api.response.ItemResponse;
import com.uom.assignment.hacker.news.api.service.HackerNewsApiService;
import com.uom.assignment.model.request.UpdateStoryModel;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

/**
 * A test suite for {@link UpdateStoriesProcessor}.
 *
 * Created by jacobfalzon on 21/05/2017.
 */
@RunWith(MockitoJUnitRunner.class)
public class UpdateStoriesProcessorTest {

    @Mock
    private Story mockStory;

    @Mock
    private ItemResponse mockItemResponse;

    @Mock
    private HackerNewsApiService hackerNewsApiService;

    @InjectMocks
    private UpdateStoriesProcessor updateStoriesProcessor;

    private static final Long HACKER_NEWS_ID = 1234L;
    private static final Long OLD_SCORE = 1L;
    private static final Long NEW_SCORE = 2L;

    @Before
    public void setup() throws Exception {
        // Mocking mockStory to contain HACKER_NEWS_ID
        Mockito.when(mockStory.getHackerNewsId()).thenReturn(HACKER_NEWS_ID);

        // Mocking mockStory to contain OLD_SCORE
        Mockito.when(mockStory.getScore()).thenReturn(OLD_SCORE);
    }

    @Test
    public void process_emptyResponse_returnsNull() throws Exception {

        // Mocking that empty response is returned
        Mockito.when(hackerNewsApiService.doGet(new ItemRequest(HACKER_NEWS_ID))).thenReturn(new EmptyResponse());

        final UpdateStoryModel updateStoryModel = updateStoriesProcessor.process(mockStory);

        // Verifying that the request was made
        Mockito.verify(hackerNewsApiService).doGet(new ItemRequest(mockStory.getHackerNewsId()));

        // Verifying that the processor returned null
        Assert.assertNull(updateStoryModel);
    }


    @Test
    public void process_storyNotDeleted_storyScoreChanged_returnsUpdateStoryModel() throws Exception {

        // Mocking that mockItemResponse is returned
        Mockito.when(hackerNewsApiService.doGet(new ItemRequest(HACKER_NEWS_ID))).thenReturn(mockItemResponse);

        // Mocking that the story is NOT deleted
        Mockito.when(mockItemResponse.isDeleted()).thenReturn(false);

        // Mocking that the score has changed, i.e. it is NEW_SCORE
        Mockito.when(mockItemResponse.getScore()).thenReturn(NEW_SCORE);

        final UpdateStoryModel updateStoryModel = updateStoriesProcessor.process(mockStory);

        // Verifying that the request was made
        Mockito.verify(hackerNewsApiService).doGet(new ItemRequest(mockStory.getHackerNewsId()));

        // Verifying that the processor returned a model containing mockStory, the NEW_SCORE and deleted as false
        Assert.assertEquals(new UpdateStoryModel(mockStory, NEW_SCORE, false), updateStoryModel);
    }

    @Test
    public void process_storyDeleted_returnsUpdateStoryModel() throws Exception {

        // Mocking that mockItemResponse is returned
        Mockito.when(hackerNewsApiService.doGet(new ItemRequest(HACKER_NEWS_ID))).thenReturn(mockItemResponse);

        // Mocking that the story is deleted
        Mockito.when(mockItemResponse.isDeleted()).thenReturn(true);

        // Mocking that the story is deleted
        Mockito.when(mockItemResponse.getScore()).thenReturn(null);

        final UpdateStoryModel updateStoryModel = updateStoriesProcessor.process(mockStory);

        // Verifying that the request was made
        Mockito.verify(hackerNewsApiService).doGet(new ItemRequest(mockStory.getHackerNewsId()));

        // Verifying that the processor returned a model containing the mockStory and deleted as true.
        Assert.assertEquals(new UpdateStoryModel(mockStory, null, true), updateStoryModel);
    }

    @Test
    public void process_storyNotDeleted_storyScoreNotChanged_returnsNull() throws Exception {

        // Mocking that mockItemResponse is returned
        Mockito.when(hackerNewsApiService.doGet(new ItemRequest(HACKER_NEWS_ID))).thenReturn(mockItemResponse);

        // Mocking that the story is NOT deleted
        Mockito.when(mockItemResponse.isDeleted()).thenReturn(false);

        // Mocking that the score has NOT changed, i.e. it is still OLD_SCORE
        Mockito.when(mockItemResponse.getScore()).thenReturn(OLD_SCORE);

        final UpdateStoryModel updateStoryModel = updateStoriesProcessor.process(mockStory);

        // Verifying that the request was made
        Mockito.verify(hackerNewsApiService).doGet(new ItemRequest(mockStory.getHackerNewsId()));

        // Verifying that the processor returned null
        Assert.assertNull(updateStoryModel);
    }

}
