package com.uom.assignment.batch.writer;

import com.uom.assignment.dao.Story;
import com.uom.assignment.hacker.news.api.response.ItemResponse;
import com.uom.assignment.service.StoryService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Collections;
import java.util.Random;

import static com.uom.assignment.batch.writer.NewStoriesWriter.toEpoch;

/**
 * A test suite for {@link NewStoriesWriter}.
 *
 * Created by jacobfalzon on 20/05/2017.
 */
@RunWith(MockitoJUnitRunner.class)
public class NewStoriesWriterTest {

    @Mock
    private ItemResponse mockItemResponse;

    @Mock
    private Story mockStory;

    @Mock
    private StoryService storyService;

    @InjectMocks
    private NewStoriesWriter newStoriesWriter;

    private static final Random RANDOM = new Random();
    private static final Long HACKER_NEWS_ID = RANDOM.nextLong();
    private static final String TITLE = "title";
    private static final String AUTHOR = "author";
    private static final String URL = "url";
    private static final Long SCORE = RANDOM.nextLong();
    private static final Long CREATION_DATE = RANDOM.nextLong();

    @Test
    public void write_persistsStories() {

        // Mocking mockItemResponse to contain HACKER_NEWS_ID, TITLE, AUTHOR, URL, SCORE
        Mockito.when(mockItemResponse.getId()).thenReturn(HACKER_NEWS_ID);
        Mockito.when(mockItemResponse.getTitle()).thenReturn(TITLE);
        Mockito.when(mockItemResponse.getBy()).thenReturn(AUTHOR);
        Mockito.when(mockItemResponse.getUrl()).thenReturn(URL);
        Mockito.when(mockItemResponse.getScore()).thenReturn(SCORE);
        Mockito.when(mockItemResponse.getTime()).thenReturn(CREATION_DATE);

        // Mocking that mockStory is returned
        Mockito.when(storyService.createOrUpdate(HACKER_NEWS_ID, TITLE, AUTHOR, URL, SCORE, toEpoch(CREATION_DATE))).thenReturn(mockStory);

        // Mocking that mockStory contains HACKER_NEWS_ID
        Mockito.when(mockStory.getHackerNewsId()).thenReturn(HACKER_NEWS_ID);

        newStoriesWriter.write(Collections.singletonList(mockItemResponse));

        // Verifying that mockStory was returned
        Mockito.when(storyService.createOrUpdate(HACKER_NEWS_ID, TITLE, AUTHOR, URL, SCORE, toEpoch(CREATION_DATE))).thenReturn(mockStory);
    }
}
