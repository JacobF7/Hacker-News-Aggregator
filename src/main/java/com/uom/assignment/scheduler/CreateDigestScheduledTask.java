package com.uom.assignment.scheduler;

import com.uom.assignment.batch.job.CreateDigestJob;
import com.uom.assignment.dao.Digest;
import com.uom.assignment.dao.Topic;
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
 * A {@link Scheduled} task to create a {@link Digest} for every persisted {@link Topic}.
 *
 * Created by jacobfalzon on 10/05/2017.
 */
@Component
public class CreateDigestScheduledTask {

    private static final String START_TIME = "START_TIME";
    private static final Logger LOG = LoggerFactory.getLogger(CreateDigestScheduledTask.class);

    private final JobLauncher jobLauncher;
    private final Job job;

    @Autowired
    public CreateDigestScheduledTask(final JobLauncher jobLauncher, @Qualifier(CreateDigestJob.JOB_NAME) final Job job) {
        this.jobLauncher = jobLauncher;
        this.job = job;
    }

   // @Scheduled(fixedDelay = 1_800_000) // 30 Minutes = 1,800,000 Milliseconds // TODO CRON
    public void createDigest() {

        LOG.info("Running Create Digest Scheduled Task");

        try {
            jobLauncher.run(job, new JobParameters(Collections.singletonMap(START_TIME, new JobParameter(System.currentTimeMillis()))));
        } catch (final Exception e) {
            LOG.error("Encountered Error during Create Digest Scheduled Task", e);
        }

        LOG.info("Finished Create Digest Scheduled Task");
    }
}
