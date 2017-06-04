package com.uom.assignment.scheduler;

import com.uom.assignment.service.DurationType;
import com.uom.assignment.service.StoryService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import java.security.SecureRandom;
import java.util.Collections;

/**
 * A test suite {@link DeleteExpiredSessionsScheduledTask}.
 *
 * Created by jacobfalzon on 02/06/2017.
 */
@RunWith(MockitoJUnitRunner.class)
public class DeleteUnusedStoriesScheduledTaskTest {

    @Mock
    private StoryService storyService;

    @InjectMocks
    private DeleteUnusedStoriesScheduledTask deleteUnusedStoriesScheduledTask;

    private static final Long UNUSED_STORY_ID = new SecureRandom().nextLong();

    @Test
    public void deleteExpiredSessions_delegatesToSessionService() {

        // Mocking an unused Story
        Mockito.when(storyService.deleteUnusedStoriesByCreationDate(DurationType.WEEKLY.getDuration())).thenReturn(Collections.singleton(UNUSED_STORY_ID));

        deleteUnusedStoriesScheduledTask.deleteUnusedStories();

        Mockito.verify(storyService).deleteUnusedStoriesByCreationDate(DurationType.WEEKLY.getDuration());
    }

}
