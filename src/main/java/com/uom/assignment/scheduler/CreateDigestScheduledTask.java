package com.uom.assignment.scheduler;

import com.uom.assignment.batch.job.CreateDigestJob;
import com.uom.assignment.cache.CacheConfiguration;
import com.uom.assignment.dao.Digest;
import com.uom.assignment.dao.Topic;
import com.uom.assignment.dao.User;
import com.uom.assignment.service.DigestService;
import com.uom.assignment.service.DurationType;
import com.uom.assignment.service.StoryService;
import com.uom.assignment.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * A {@link Scheduled} task to create the Overall {@link Digest}s as well as the {@link Digest}s for the {@link Topic}s that every {@link User} is subscribed to.
 *
 * Created by jacobfalzon on 10/05/2017.
 */
@Component
public class CreateDigestScheduledTask {

    private static final String START_TIME = "START_TIME";
    private static final Logger LOG = LoggerFactory.getLogger(CreateDigestScheduledTask.class);

    private final UserService userService;
    private final StoryService storyService;
    private final DigestService digestService;
    private final JobLauncher jobLauncher;
    private final Job job;

    @Value("${digest.top.stories.count}")
    private Integer N; // Top N Stories, i.e. 3 by default

    @Autowired
    public CreateDigestScheduledTask(final UserService userService, final StoryService storyService, final DigestService digestService, final JobLauncher jobLauncher, @Qualifier(CreateDigestJob.JOB_NAME) final Job job) {
        this.userService = userService;
        this.storyService = storyService;
        this.digestService = digestService;
        this.jobLauncher = jobLauncher;
        this.job = job;
    }

    @Scheduled(cron = "0 0 9 ? * SAT") // runs every Saturday at 9
    @CacheEvict(value = CacheConfiguration.LATEST_DIGESTS_CACHE_KEY, allEntries = true) // Evict all from Latest Digests Cache
    public void createDigest() {

        LOG.info("Running Create Digest Scheduled Task");

        // Obtain all users
        final List<User> users = userService.findAll();

        if(users.isEmpty()) {
            LOG.info("No Users to create Digest for");
            LOG.info("Finished Create Digest Scheduled Task");
            return;
        }

        // Create the digest for all topics subscribed by users
        try {
            jobLauncher.run(job, new JobParameters(Collections.singletonMap(START_TIME, new JobParameter(System.currentTimeMillis()))));
        } catch (final Exception e) {
            LOG.error("Encountered Error during Create Digest Scheduled Task", e);
        }

        // Create a digest for the overall top stories for all current users
        storyService.findOverallTopStoriesByCreationDate(DurationType.WEEKLY.getDuration(), N)
                    .stream()
                    .map(topStory -> digestService.createOverallDigest(topStory, new HashSet<User>(users), DurationType.WEEKLY))
                    .forEach(digest -> LOG.info("Created Overall Digest: {}", digest));



        LOG.info("Finished Create Digest Scheduled Task");
    }
}
