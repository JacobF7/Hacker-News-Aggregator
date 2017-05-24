package com.uom.assignment.scheduler;

import com.uom.assignment.dao.Session;
import com.uom.assignment.service.SessionService;
import com.uom.assignment.service.SessionServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Set;

/**
 * A {@link Scheduled} task to delete any expired {@link Session}.
 *
 * Created by jacobfalzon on 10/05/2017.
 */
@Component
public class DeleteExpiredSessionsScheduledTask {

    private static final Logger LOG = LoggerFactory.getLogger(DeleteExpiredSessionsScheduledTask.class);

    private final SessionService sessionService;

    @Autowired
    public DeleteExpiredSessionsScheduledTask(final SessionService sessionService) {
        this.sessionService = sessionService;
    }

    @Scheduled(fixedDelay = 1_800_000) // 30 Minutes = 1,800,000 Milliseconds
    public void deleteExpiredSessions() {

        LOG.info("Running Delete Expired Sessions Scheduled Task");

        final Set<Long> deletedSessions = sessionService.deleteExpiredSessions();

        deletedSessions.parallelStream().forEach(sessionId -> LOG.info("Removing Expired Session [{}]", sessionId));

        LOG.info("Finished Delete Expired Sessions Scheduled Task");
    }
}
