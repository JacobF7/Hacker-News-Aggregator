package com.uom.assignment.scheduler;

import com.uom.assignment.dao.Digest;
import com.uom.assignment.service.DigestService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Set;

/**
 *  A {@link Scheduled} task to delete any expired {@link Digest}.
 *
 * Created by jacobfalzon on 02/06/2017.
 */
@Component
public class DeleteExpiredDigestsScheduledTask {

    private static final Logger LOG = LoggerFactory.getLogger(DeleteExpiredDigestsScheduledTask.class);

    private final DigestService digestService;

    @Autowired
    public DeleteExpiredDigestsScheduledTask(final DigestService digestService) {
        this.digestService = digestService;
    }

//    @Scheduled(cron = "0 0 9 ? * SAT") // running every Saturday at 9 TODO UNCOMMENT
    public void deleteExpiredDigests() {

        LOG.info("Running Delete Expired Digests Scheduled Task");

        final Set<Long> deletedDigests = digestService.deleteExpiredDigests();

        deletedDigests.parallelStream().forEach(digestId -> LOG.info("Removing Expired Digest [{}]", digestId));

        LOG.info("Finished Delete Expired Digests Scheduled Task");
    }

}
