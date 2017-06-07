package com.uom.assignment.service;

import com.uom.assignment.cache.CacheConfiguration;
import com.uom.assignment.dao.Digest;
import com.uom.assignment.dao.Story;
import com.uom.assignment.dao.Topic;
import com.uom.assignment.dao.User;
import com.uom.assignment.repository.DigestRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

/**
 * The {@link Service} implementation for the {@link Digest} entity.
 *
 * Created by jacobfalzon on 27/05/2017.
 */
@Service
@Transactional
public class DigestServiceImpl implements DigestService {

    private static final Logger LOG = LoggerFactory.getLogger(DigestServiceImpl.class);

    private final DigestRepository digestRepository;

    @Autowired
    public DigestServiceImpl(final DigestRepository digestRepository) {
        this.digestRepository = digestRepository;
    }

    @Override
    public Digest createTopicDigest(final Topic topic, final Story topStory, final Set<User> users, final DurationType durationType) {
        final Instant now = Instant.now().truncatedTo(ChronoUnit.DAYS);
        return digestRepository.save(new Digest(topic, topStory, now.toEpochMilli(), users,false));
    }

    @Override
    public Digest createOverallDigest(final Story overallTopStory, final Set<User> users, final DurationType durationType) {
        final Instant now = Instant.now().truncatedTo(ChronoUnit.DAYS);
        return digestRepository.save(new Digest(null, overallTopStory, now.toEpochMilli(), users, true));
    }

    @Override
    public Map<LocalDate, List<Digest>> findDigests(final User user, final LocalDate start, final LocalDate end) {

        final Set<Digest> digests =
            digestRepository.findByUsersAndCreationDateBetween(user, DigestService.toEpoch(start), DigestService.toEpoch(end));

        return digests.stream()
                      .collect(Collectors.groupingBy(Digest::getCreationDate))
                      .entrySet()
                      .stream()
                      .collect(Collectors.toMap(entry -> DigestService.toLocalDate(entry.getKey()), Map.Entry::getValue));
    }

    @Cacheable(value = CacheConfiguration.LATEST_DIGESTS_CACHE_KEY, key = "#user.getId()")
    @Override
    public Map<LocalDate, List<Digest>> findLatestDigests(final User user) {

        LOG.info("Bypassing {} Cache for Key: [{}] ", CacheConfiguration.LATEST_DIGESTS_CACHE_KEY, user.getId());

        final Optional<Long> latestCreationDate = Optional.ofNullable(digestRepository.findMaxCreationDateByUser(user));

        if (!latestCreationDate.isPresent()) {
            return Collections.emptyMap();
        }

        final Set<Digest> digests = digestRepository.findByUsersAndCreationDate(user, latestCreationDate.get());

        return digests.stream()
                .collect(Collectors.groupingBy(Digest::getCreationDate))
                .entrySet()
                .stream()
                .collect(Collectors.toMap(entry -> DigestService.toLocalDate(entry.getKey()), Map.Entry::getValue));
    }

    @Override
    public Set<Long> deleteExpiredDigests() {

        final Set<Long> expiredDigests = digestRepository.findAll()
                .parallelStream()
                .filter(DigestService::isDigestExpired)
                .map(Digest::getId)
                .collect(Collectors.toSet());

        expiredDigests.forEach(digestRepository::delete);

        return expiredDigests;
    }

}
