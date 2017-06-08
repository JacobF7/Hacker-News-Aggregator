package com.uom.assignment.scheduler;

import com.uom.assignment.dao.Story;
import com.uom.assignment.service.DurationType;
import com.uom.assignment.service.StoryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Set;

/**
 * Delete any {@link Story} that is unused and older than {@link DurationType#WEEKLY}.
 * Note that a {@link Story} is considered unused if {@link Story#digests} is {@link Collections#emptyList()}.
 *
 * Created by jacobfalzon on 02/06/2017.
 */
@Component
public class DeleteUnusedStoriesScheduledTask {

    private static final Logger LOG = LoggerFactory.getLogger(DeleteUnusedStoriesScheduledTask.class);

    private final StoryService storyService;

    @Autowired
    public DeleteUnusedStoriesScheduledTask(final StoryService storyService) {
        this.storyService = storyService;
    }

    @Scheduled(cron = "0 0 9 ? * SAT") // runs every Saturday at 9
    public void deleteUnusedStories() {

        LOG.info("Running Delete Unused Stories Scheduled Task");

        final Set<Long> deletedDigests = storyService.deleteUnusedStoriesByCreationDate(DurationType.WEEKLY.getDuration());

        deletedDigests.parallelStream().forEach(storyId -> LOG.info("Removing Unused Story [{}]", storyId));

        LOG.info("Finished Delete Unused Stories Scheduled Task");
    }

}
