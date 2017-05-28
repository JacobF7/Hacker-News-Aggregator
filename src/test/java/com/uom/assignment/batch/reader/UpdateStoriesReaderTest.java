package com.uom.assignment.batch.reader;

import com.uom.assignment.dao.Story;
import com.uom.assignment.service.StoryService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import java.time.Duration;
import java.util.Collections;
import java.util.Random;

/**
 * A test suite for {@link UpdateStoriesReader}.
 *
 * Created by jacobfalzon on 21/05/2017.
 */
@RunWith(MockitoJUnitRunner.class)
public class UpdateStoriesReaderTest {

    @Mock
    private Story mockStory;

    @Mock
    private StoryService storyService;

    private static final int DURATION = new Random().nextInt();

    @Test
    public void read_ALL_storyExists_returnsStory() throws Exception {

        // Mocking that mockStory is the only persisted story
        Mockito.when(storyService.findAllActive()).thenReturn(Collections.singletonList(mockStory));

        final Story story = new UpdateStoriesReader(storyService, FetchMode.ALL, DURATION).read();

        // Verifying that a request to fetch ALL stories was made
        Mockito.verify(storyService).findAllActive();

        // Verifying that no request to fetch RECENT stories was made
        Mockito.verify(storyService, Mockito.never()).findActiveByLastUpdatedDuration(Duration.ofHours(DURATION));

        // Verifying that the reader returned mockStory
        Assert.assertEquals(mockStory, story);
    }

    @Test
    public void read_RECENT_storyExists_returnsStory() throws Exception {

        // Mocking that mockStory is the only recent story
        Mockito.when(storyService.findActiveByLastUpdatedDuration(Duration.ofHours(DURATION))).thenReturn(Collections.singletonList(mockStory));

        final Story story = new UpdateStoriesReader(storyService, FetchMode.RECENT, DURATION).read();

        // Verifying that a request to fetch RECENT stories was made
        Mockito.verify(storyService).findActiveByLastUpdatedDuration(Duration.ofHours(DURATION));

        // Verifying that no request to fetch ALL stories was made
        Mockito.verify(storyService, Mockito.never()).findAllActive();

        // Verifying that the reader returned mockStory
        Assert.assertEquals(mockStory, story);
    }

    @Test
    public void read_ALL_storyDoesNotExist_returnsNull() throws Exception {

        // Mocking that there is no persisted story
        Mockito.when(storyService.findAllActive()).thenReturn(Collections.emptyList());

        final Story story = new UpdateStoriesReader(storyService, FetchMode.ALL, DURATION).read();

        // Verifying that a request to fetch ALL stories was made
        Mockito.verify(storyService).findAllActive();

        // Verifying that no request to fetch RECENT stories was made
        Mockito.verify(storyService, Mockito.never()).findActiveByLastUpdatedDuration(Duration.ofHours(DURATION));

        // Verifying that the reader returned null
        Assert.assertNull(story);
    }

}
