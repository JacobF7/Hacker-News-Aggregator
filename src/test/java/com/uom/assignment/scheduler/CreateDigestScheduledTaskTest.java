package com.uom.assignment.scheduler;

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

import javax.batch.operations.JobExecutionIsRunningException;

/**
 * A test suite for {@link CreateDigestScheduledTask}.
 *
 * Created by jacobfalzon on 27/05/2017.
 */
@RunWith(MockitoJUnitRunner.class)
public class CreateDigestScheduledTaskTest {

    @Mock
    private JobLauncher jobLauncher;

    @Mock
    private Job job;

    @InjectMocks
    private CreateDigestScheduledTask createDigestScheduledTask;

    @Test
    public void createDigest_delegatesToJobLauncher() throws Exception {

        createDigestScheduledTask.createDigest();

        // Verifying that the job was run
        Mockito.verify(jobLauncher).run(Matchers.eq(job), Matchers.any(JobParameters.class));
    }

    @Test
    public void updateAllStories_delegatesToJobLauncher_jobThrowsException() throws Exception {

        Mockito.when(jobLauncher.run(Matchers.eq(job), Matchers.any(JobParameters.class))).thenThrow(new JobExecutionIsRunningException());

        createDigestScheduledTask.createDigest();

        // Verifying that the job was run
        Mockito.verify(jobLauncher).run(Matchers.eq(job), Matchers.any(JobParameters.class));
    }
}
