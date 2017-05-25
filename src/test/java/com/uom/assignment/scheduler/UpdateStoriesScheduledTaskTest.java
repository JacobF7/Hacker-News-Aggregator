package com.uom.assignment.scheduler;

import com.uom.assignment.batch.reader.FetchMode;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.launch.JobLauncher;

import javax.batch.operations.JobExecutionIsRunningException;

/**
 * A test suite for {@link UpdateStoriesScheduledTask}.
 *
 * Created by jacobfalzon on 21/05/2017.
 */
@RunWith(MockitoJUnitRunner.class)
public class UpdateStoriesScheduledTaskTest {

    @Mock
    private JobLauncher jobLauncher;

    @Mock
    private Job job;

    @InjectMocks
    private UpdateStoriesScheduledTask updateStoriesScheduledTask;

    @Test
    public void updateAllStories_delegatesToJobLauncher() throws Exception {

        updateStoriesScheduledTask.updateAllStories();

        final ArgumentCaptor<JobParameters> argumentCaptor = ArgumentCaptor.forClass(JobParameters.class);

        // Verifying that the job was run
        Mockito.verify(jobLauncher).run(Matchers.eq(job), argumentCaptor.capture());

        final JobParameters jobParameters = argumentCaptor.getValue();

        // Verifying that the job was run on ALL fetch mode
        Assert.assertEquals(FetchMode.ALL.name(), jobParameters.getParameters().get(FetchMode.FETCH_MODE).getValue());
    }

    @Test
    public void updateRecentStories_delegatesToJobLauncher() throws Exception {

        updateStoriesScheduledTask.updateRecentStories();

        final ArgumentCaptor<JobParameters> argumentCaptor = ArgumentCaptor.forClass(JobParameters.class);

        // Verifying that the job was run
        Mockito.verify(jobLauncher).run(Matchers.eq(job), argumentCaptor.capture());

        final JobParameters jobParameters = argumentCaptor.getValue();

        // Verifying that the job was run on RECENT fetch mode
        Assert.assertEquals(FetchMode.RECENT.name(), jobParameters.getParameters().get(FetchMode.FETCH_MODE).getValue());
    }

    @Test
    public void updateAllStories_delegatesToJobLauncher_jobThrowsException() throws Exception {

        Mockito.when(jobLauncher.run(Matchers.eq(job), Matchers.any(JobParameters.class))).thenThrow(new JobExecutionIsRunningException());

        updateStoriesScheduledTask.updateAllStories();

        final ArgumentCaptor<JobParameters> argumentCaptor = ArgumentCaptor.forClass(JobParameters.class);

        // Verifying that the job was run
        Mockito.verify(jobLauncher).run(Matchers.eq(job), argumentCaptor.capture());

        final JobParameters jobParameters = argumentCaptor.getValue();

        // Verifying that the job was run on ALL fetch mode
        Assert.assertEquals(FetchMode.ALL.name(), jobParameters.getParameters().get(FetchMode.FETCH_MODE).getValue());
    }

}
