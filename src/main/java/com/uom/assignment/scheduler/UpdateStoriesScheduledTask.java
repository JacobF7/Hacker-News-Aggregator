package com.uom.assignment.scheduler;

import com.google.common.collect.ImmutableMap;
import com.uom.assignment.batch.job.UpdateStoriesJob;
import com.uom.assignment.batch.reader.FetchMode;
import com.uom.assignment.cache.CacheConfiguration;
import com.uom.assignment.dao.Story;
import com.uom.assignment.dao.Topic;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * A {@link Scheduled} task to update any persisted {@link Story} and {@link Topic} by running the {@link UpdateStoriesJob}.
 *
 *  Created by jacobfalzon on 20/05/2017.
 */
@Component
public class UpdateStoriesScheduledTask {

    private static final Logger LOG = LoggerFactory.getLogger(UpdateStoriesScheduledTask.class);
    private static final String START_TIME = "START_TIME";

    private final Object lock = new Object();
    private final JobLauncher jobLauncher;
    private final Job job;

    @Autowired
    public UpdateStoriesScheduledTask(final JobLauncher jobLauncher, @Qualifier(UpdateStoriesJob.JOB_NAME) final Job job) {
        this.jobLauncher = jobLauncher;
        this.job = job;
    }

    @Scheduled(cron = "0 0 3 * * ?") // runs everyday at 3:00 AM
    @CacheEvict(value = {CacheConfiguration.TOP_STORIES_CACHE_KEY, CacheConfiguration.TOPICS_CACHE_KEY}, allEntries = true)
    public void updateAllStories() {
        updateStories(FetchMode.ALL);
    }

    @Scheduled(fixedDelay = 3_600_000) // runs every hour = 3,600,000 Milliseconds
    @CacheEvict(value = {CacheConfiguration.TOP_STORIES_CACHE_KEY, CacheConfiguration.TOPICS_CACHE_KEY}, allEntries = true)
    public void updateRecentStories() {
        updateStories(FetchMode.RECENT);
    }

    private void updateStories(final FetchMode fetchMode) {

        synchronized (lock) {

             LOG.info("Running [{}] Update Stories Scheduled Task", fetchMode);

             try {
                 jobLauncher.run(job, new JobParameters(ImmutableMap.of(START_TIME, new JobParameter(System.currentTimeMillis()), FetchMode.FETCH_MODE, new JobParameter(fetchMode.name()))));
             } catch (final Exception e) {
                 LOG.error(String.format("Encountered Error during [%s] Update Stories Scheduled Task", fetchMode), e);
             } finally {
                 lock.notify();
             }

             LOG.info("Finished [{}] Update Stories Scheduled Task", fetchMode);
         }
    }

}
