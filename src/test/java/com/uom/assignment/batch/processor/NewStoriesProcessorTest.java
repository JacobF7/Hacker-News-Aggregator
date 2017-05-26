package com.uom.assignment.batch.processor;

import com.uom.assignment.hacker.news.api.request.ItemRequest;
import com.uom.assignment.hacker.news.api.response.EmptyResponse;
import com.uom.assignment.hacker.news.api.response.ItemResponse;
import com.uom.assignment.hacker.news.api.service.HackerNewsApiService;
import com.uom.assignment.model.request.StoryIdModel;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

/**
 * A test suite for {@link NewStoriesProcessor}.
 *
 * Created by jacobfalzon on 19/05/2017.
 */
@RunWith(MockitoJUnitRunner.class)
public class NewStoriesProcessorTest {

    @Mock
    private StoryIdModel mockStoryIdModel;

    @Mock
    private ItemResponse mockItemResponse;

    @Mock
    private HackerNewsApiService hackerNewsApiService;

    @InjectMocks
    private NewStoriesProcessor newStoriesProcessor;

    private static final String STORY_ID = "1234";
    private static final String URL = "url";

    @Test
    public void process_emptyResponse_returnsNull() throws Exception  {

        // Mocking mockStoryIdModel to contain STORY_ID
        Mockito.when(mockStoryIdModel.getId()).thenReturn(STORY_ID);

        // Mocking that empty response is returned
        Mockito.when(hackerNewsApiService.doGet(new ItemRequest(Long.valueOf(mockStoryIdModel.getId())))).thenReturn(new EmptyResponse());

        final ItemResponse response = newStoriesProcessor.process(mockStoryIdModel);

        // Verifying that the request was made
        Mockito.verify(hackerNewsApiService).doGet(new ItemRequest(Long.valueOf(mockStoryIdModel.getId())));

        // Verifying that the processor returned null
        Assert.assertNull(response);
    }

    @Test
    public void process_responseUrlSet_returnsItemResponse() throws Exception  {

        // Mocking mockStoryIdModel to contain STORY_ID
        Mockito.when(mockStoryIdModel.getId()).thenReturn(STORY_ID);

        // Mocking that mockItemResponse is returned
        Mockito.when(hackerNewsApiService.doGet(new ItemRequest(Long.valueOf(mockStoryIdModel.getId())))).thenReturn(mockItemResponse);

        // Mocking that the URL is set
        Mockito.when(mockItemResponse.getUrl()).thenReturn(URL);

        final ItemResponse itemResponse = newStoriesProcessor.process(mockStoryIdModel);

        // Verifying that the request was made
        Mockito.verify(hackerNewsApiService).doGet(new ItemRequest(Long.valueOf(mockStoryIdModel.getId())));

        // Verifying that the processor returned mockItemResponse
        Assert.assertEquals(mockItemResponse, itemResponse);

        // Verifying that the mockItemResponse url is set to the response URL
        Assert.assertEquals(URL, itemResponse.getUrl());
    }

    @Test
    public void process_responseUrlNotSet_returnsItemResponse() throws Exception {

        // Mocking mockStoryIdModel to contain STORY_ID
        Mockito.when(mockStoryIdModel.getId()).thenReturn(STORY_ID);

        final ItemRequest request = new ItemRequest(Long.valueOf(mockStoryIdModel.getId()));

        // Mocking that mockItemResponse is returned
        Mockito.when(hackerNewsApiService.doGet(request)).thenReturn(mockItemResponse);

        // Mocking that the URL is not set, i.e. it is null
        Mockito.when(mockItemResponse.getUrl()).thenReturn(null);

        // Mocking that the id for mockItemResponse is STORY_ID
        Mockito.when(mockItemResponse.getId()).thenReturn(Long.valueOf(STORY_ID));

        final ItemResponse itemResponse = newStoriesProcessor.process(mockStoryIdModel);

        // Verifying that the request was made
        Mockito.verify(hackerNewsApiService).doGet(request);

        // Verifying that the mockItemResponse url is set to the default url
        Mockito.verify(mockItemResponse).setUrl(NewStoriesProcessor.DEFAULT_URL + STORY_ID);

        // Verifying that the processor returned mockItemResponse
        Assert.assertEquals(mockItemResponse, itemResponse);
    }
}
