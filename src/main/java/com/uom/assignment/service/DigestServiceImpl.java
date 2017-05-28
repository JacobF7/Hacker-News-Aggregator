package com.uom.assignment.service;

import com.uom.assignment.dao.Digest;
import com.uom.assignment.dao.Story;
import com.uom.assignment.dao.Topic;
import com.uom.assignment.repository.DigestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

/**
 * The {@link Service} implementation for the {@link Digest} entity.
 *
 * Created by jacobfalzon on 27/05/2017.
 */
@Service
@Transactional
public class DigestServiceImpl implements DigestService {

    private final DigestRepository digestRepository;

    @Autowired
    public DigestServiceImpl(final DigestRepository digestRepository) {
        this.digestRepository = digestRepository;
    }

    public Digest create(final Topic topic, final Story topStory, final DurationType durationType) {
        final Instant now = Instant.now().truncatedTo(ChronoUnit.DAYS);
        return digestRepository.save(new Digest(topic, topStory, now.toEpochMilli(), now.plusMillis(durationType.getDuration().toMillis()).toEpochMilli()));
    }
}
