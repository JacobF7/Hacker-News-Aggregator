//package com.uom.assignment.scheduler;
//
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.mockito.InjectMocks;
//import org.mockito.Matchers;
//import org.mockito.Mock;
//import org.mockito.Mockito;
//import org.mockito.runners.MockitoJUnitRunner;
//import org.springframework.batch.core.Job;
//import org.springframework.batch.core.JobParameters;
//import org.springframework.batch.core.launch.JobLauncher;
//
//import javax.batch.operations.JobExecutionIsRunningException;
//
///**
// * A test suite for {@link FetchNewStoriesScheduledTask}.
// *
// * Created by jacobfalzon on 19/05/2017.
// */
//@RunWith(MockitoJUnitRunner.class)
//public class FetchNewUpdateStoriesScheduledTaskTest {
//
//    @Mock
//    private JobLauncher jobLauncher;
//
//    @Mock
//    private Job job;
//
//    @InjectMocks
//    private FetchNewStoriesScheduledTask fetchNewStoriesScheduledTask;
//
//    @Test
//    public void fetchNewStories_delegatesToJobLauncher() throws Exception {
//
//        fetchNewStoriesScheduledTask.fetchNewStories();
//
//        Mockito.verify(jobLauncher).run(Matchers.eq(job), Matchers.any(JobParameters.class));
//    }
//
//    @Test
//    public void fetchNewStories_delegatesToJobLauncher_jobThrowsException() throws Exception {
//
//        Mockito.when(jobLauncher.run(Matchers.eq(job), Matchers.any(JobParameters.class))).thenThrow(new JobExecutionIsRunningException());
//
//        fetchNewStoriesScheduledTask.fetchNewStories();
//
//        Mockito.verify(jobLauncher).run(Matchers.eq(job), Matchers.any(JobParameters.class));
//    }
//}
