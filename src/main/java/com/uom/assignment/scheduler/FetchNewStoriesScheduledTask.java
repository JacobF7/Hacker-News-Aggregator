package com.uom.assignment.scheduler;

import com.uom.assignment.batch.job.NewStoriesJob;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Collections;

/**
 * A {@link Scheduled} task to fetch New Stories from the Hacker News API by running the {@link NewStoriesJob}.
 *
 * Created by jacobfalzon on 18/05/2017.
 */
@Component
public class FetchNewStoriesScheduledTask {

    private static final String START_TIME = "START_TIME";
    private static final Logger LOG = LoggerFactory.getLogger(FetchNewStoriesScheduledTask.class);

    private final JobLauncher jobLauncher;
    private final Job job;

    @Autowired
    public FetchNewStoriesScheduledTask(final JobLauncher jobLauncher, @Qualifier(NewStoriesJob.JOB_NAME) final Job job) {
        this.jobLauncher = jobLauncher;
        this.job = job;
    }

 // TODO UNCOMMENT   @Scheduled(fixedDelay = 1_800_000) // 30 Minutes = 1,800,000 Milliseconds
    public void fetchNewStories() {

        LOG.info("Running Fetch New Stories Scheduled Task");

        try {
            jobLauncher.run(job, new JobParameters(Collections.singletonMap(START_TIME, new JobParameter(System.currentTimeMillis()))));
        } catch (final Exception e) {
            LOG.error("Encountered Error during Fetch New Stories Scheduled Task: {}",  e);
        }

        LOG.info("Finished Fetch New Stories Scheduled Task");
    }

}
