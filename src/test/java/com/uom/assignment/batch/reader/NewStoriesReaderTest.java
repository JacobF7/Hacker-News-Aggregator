package com.uom.assignment.batch.reader;

import com.uom.assignment.hacker.news.api.request.NewStoriesRequest;
import com.uom.assignment.hacker.news.api.response.NewStoriesResponse;
import com.uom.assignment.hacker.news.api.service.HackerNewsApiService;
import com.uom.assignment.model.request.StoryIdModel;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

/**
 * A test suite for {@link NewStoriesReader}.
 *
 * Created by jacobfalzon on 19/05/2017.
 */
@RunWith(MockitoJUnitRunner.class)
public class NewStoriesReaderTest {

    @Mock
    private HackerNewsApiService hackerNewsApiService;

    private static final String NEW_STORY_ID = "1234";
    private static final NewStoriesResponse NEW_STORIES_RESPONSE = new NewStoriesResponse(new String[] {NEW_STORY_ID});

    @Test
    public void read_newStoriesNotEmpty_returnsStoryIdModel() throws Exception {

        // Mocking that NEW_STORIES_RESPONSE is returned, i.e. a response containing NEW_STORY_ID
        Mockito.when(hackerNewsApiService.doGet(new NewStoriesRequest())).thenReturn(NEW_STORIES_RESPONSE);

        final StoryIdModel storyIdModel = new NewStoriesReader(hackerNewsApiService).read();

        // Verifying that the request was made
        Mockito.verify(hackerNewsApiService).doGet(new NewStoriesRequest());

        // Verifying that the reader returned a StoryIdModel containing NEW_STORY_ID
        Assert.assertEquals(storyIdModel.getId(), NEW_STORY_ID);
    }

    @Test
    public void read_newStoriesEmpty_returnsNull() throws Exception {

        // Mocking that an empty response is returned
        Mockito.when(hackerNewsApiService.doGet(new NewStoriesRequest())).thenReturn(new NewStoriesResponse(new String[]{}));

        final StoryIdModel storyIdModel = new NewStoriesReader(hackerNewsApiService).read();

        // Verifying that the request was made
        Mockito.verify(hackerNewsApiService).doGet(new NewStoriesRequest());

        // Verifying that the reader returned null
        Assert.assertNull(storyIdModel);
    }
}
