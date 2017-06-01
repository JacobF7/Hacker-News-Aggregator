package com.uom.assignment.service;

import com.uom.assignment.dao.Digest;
import com.uom.assignment.dao.Story;
import com.uom.assignment.dao.Topic;
import com.uom.assignment.dao.User;
import com.uom.assignment.repository.DigestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

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

    public Digest createTopicDigest(final Topic topic, final Story topStory, final Set<User> users, final DurationType durationType) {
        final Instant now = Instant.now().truncatedTo(ChronoUnit.DAYS);
        return digestRepository.save(new Digest(topic, topStory, now.toEpochMilli(), users,false));
    }

    public Digest createOverallDigest(final Story overallTopStory, final Set<User> users, final DurationType durationType) {
        final Instant now = Instant.now().truncatedTo(ChronoUnit.DAYS);
        return digestRepository.save(new Digest(null, overallTopStory, now.toEpochMilli(), users, true));
    }

    @Override
    public Map<LocalDate, List<Digest>> findDigests(final User user, final LocalDate start, final LocalDate end) {

        final Set<Digest> digests =
            digestRepository.findByUsersAndCreationDateBetween(user, toEpoch(start), toEpoch(end));

        return digests.stream()
                      .collect(Collectors.groupingBy(Digest::getCreationDate))
                      .entrySet()
                      .stream()
                      .collect(Collectors.toMap(entry -> toLocalDate(entry.getKey()), Map.Entry::getValue));
    }

    private Long toEpoch(final LocalDate date) {
        return date.atStartOfDay().atZone(ZoneOffset.systemDefault()).toInstant().toEpochMilli();
    }

    private LocalDate toLocalDate(final Long date) {
       return Instant.ofEpochMilli(date).atZone(ZoneOffset.systemDefault()).toLocalDate();
    }
}
