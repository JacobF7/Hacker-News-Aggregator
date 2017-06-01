package com.uom.assignment.scheduler;

import com.uom.assignment.dao.Story;
import com.uom.assignment.dao.User;
import com.uom.assignment.service.DigestService;
import com.uom.assignment.service.DurationType;
import com.uom.assignment.service.StoryService;
import com.uom.assignment.service.UserService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.test.util.ReflectionTestUtils;

import javax.batch.operations.JobExecutionIsRunningException;
import java.time.Duration;
import java.util.Collections;

/**
 * A test suite for {@link CreateDigestScheduledTask}.
 *
 * Created by jacobfalzon on 27/05/2017.
 */
@RunWith(MockitoJUnitRunner.class)
public class CreateDigestScheduledTaskTest {

    @Mock
    private User mockUser;

    @Mock
    private Story mockOverallTopStory;

    @Mock
    private JobLauncher jobLauncher;

    @Mock
    private Job job;

    @Mock
    private UserService userService;

    @Mock
    private StoryService storyService;

    @Mock
    private DigestService digestService;

    @InjectMocks
    private CreateDigestScheduledTask createDigestScheduledTask;

    private static final int N = 3;

    @Before
    public void setup() {
        ReflectionTestUtils.setField(createDigestScheduledTask, "N", N);
    }

    @Test
    public void createDigest_usersDoNotExist_doesNothing() throws Exception {

        // Mocking that no user exists
        Mockito.when(userService.findAll()).thenReturn(Collections.emptyList());

        createDigestScheduledTask.createDigest();

        // Verifying that an attempt was made to find all users
        Mockito.when(userService.findAll()).thenReturn(Collections.emptyList());

        // Verifying that the job was NOT run
        Mockito.verify(jobLauncher, Mockito.never()).run(Matchers.eq(job), Matchers.any(JobParameters.class));

        // Verifying that an attempt was NOT made to obtain the overall top stories
        Mockito.verify(storyService, Mockito.never()).findOverallTopStoriesByCreationDate(Matchers.any(Duration.class), Matchers.anyInt());

        // Verifying that an overall digest was NOT created
        Mockito.verify(digestService, Mockito.never()).createOverallDigest(Matchers.any(Story.class), Matchers.anySetOf(User.class), Matchers.any(DurationType.class));
    }

    @Test
    public void createDigest_userExists_delegatesToJobLauncher_createsOverallDigests() throws Exception {

        // Mocking that mockUser exists
        Mockito.when(userService.findAll()).thenReturn(Collections.singletonList(mockUser));

        // Mocking that the mockOverallTopStory is an overall top story
        Mockito.when(storyService.findOverallTopStoriesByCreationDate(Matchers.eq(DurationType.WEEKLY.getDuration()), Matchers.anyInt())).thenReturn(Collections.singletonList(mockOverallTopStory));

        createDigestScheduledTask.createDigest();

        // Verifying that an attempt was made to find all users
        Mockito.when(userService.findAll()).thenReturn(Collections.emptyList());

        // Verifying that the job was run
        Mockito.verify(jobLauncher).run(Matchers.eq(job), Matchers.any(JobParameters.class));

        // Verifying that an attempt was made to obtain the overall top stories
        Mockito.verify(storyService).findOverallTopStoriesByCreationDate(Matchers.eq(DurationType.WEEKLY.getDuration()), Matchers.anyInt());

        // Verifying that an overall digest was created for the mockOverallTopStory and mockUser
        Mockito.verify(digestService).createOverallDigest(mockOverallTopStory, Collections.singleton(mockUser), DurationType.WEEKLY);
    }

    @Test
    public void updateAllStories_userExists_delegatesToJobLauncher_jobThrowsException() throws Exception {

        // Mocking that mockUser exists
        Mockito.when(userService.findAll()).thenReturn(Collections.singletonList(mockUser));

        // Mocking that the mockOverallTopStory is an overall top story
        Mockito.when(storyService.findOverallTopStoriesByCreationDate(Matchers.eq(DurationType.WEEKLY.getDuration()), Matchers.anyInt())).thenReturn(Collections.singletonList(mockOverallTopStory));

        // Mocking that the job throws an exception
        Mockito.when(jobLauncher.run(Matchers.eq(job), Matchers.any(JobParameters.class))).thenThrow(new JobExecutionIsRunningException());

        createDigestScheduledTask.createDigest();

        // Verifying that an attempt was made to find all users
        Mockito.when(userService.findAll()).thenReturn(Collections.emptyList());

        // Verifying that the job was run
        Mockito.verify(jobLauncher).run(Matchers.eq(job), Matchers.any(JobParameters.class));

        // Verifying that an attempt was made to obtain the overall top stories
        Mockito.verify(storyService).findOverallTopStoriesByCreationDate(Matchers.eq(DurationType.WEEKLY.getDuration()), Matchers.anyInt());

        // Verifying that an overall digest was created for the mockOverallTopStory and mockUser
        Mockito.verify(digestService).createOverallDigest(mockOverallTopStory, Collections.singleton(mockUser), DurationType.WEEKLY);
    }
}
