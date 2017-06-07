package com.uom.assignment.service;

import com.uom.assignment.cache.CacheConfiguration;
import com.uom.assignment.controller.BusinessError;
import com.uom.assignment.controller.BusinessErrorException;
import com.uom.assignment.dao.*;
import com.uom.assignment.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

/**
 * The {@link Service} implementation for the {@link User} entity.
 *
 * Created by jacobfalzon on 14/04/2017.
 */
@Service
@Transactional
public class UserServiceImpl implements UserService {

    private static final Logger LOG = LoggerFactory.getLogger(UserServiceImpl.class);

    private final UserRepository userRepository;
    private final TopicService topicService;
    private final UserTopicService userTopicService;
    private final DigestService digestService;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(final UserRepository userRepository, final TopicService topicService, final UserTopicService userTopicService, final DigestService digestService, final PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.topicService = topicService;
        this.userTopicService = userTopicService;
        this.digestService = digestService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    @Transactional(isolation = Isolation.READ_UNCOMMITTED) // Using this isolation level in the case that a user is about to be created with the same username
    public Long create(final String username, final String password, final Set<String> topics) {

        if(findByUsername(username).isPresent()) {
            throw new BusinessErrorException(String.format("Username [%s] is not unique", username), HttpStatus.CONFLICT);
        }

        final User user = new User(username, passwordEncoder.encode(password));
        userRepository.save(user);

        // If a topic already exists, fetch it, otherwise create a new one.
        // Note that topics are trimmed and converted to lowercase.
        final Set<Topic> topicSet = topics.stream().map(topicService::create).collect(Collectors.toSet());

        // Register the user to the topics
        userTopicService.subscribe(user, topicSet);

        return user.getId();
    }

    @Override
    public Optional<User> findByUsername(final String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    @CacheEvict(value = CacheConfiguration.TOP_STORIES_CACHE_KEY, key = "#id")
    public void update(final Long id, final Set<String> topics) {

        final User user = Optional.ofNullable(userRepository.findOne(id))
                                  .orElseThrow(() -> new BusinessErrorException(BusinessError.INVALID_USER));

        // If a topic already exists, fetch it, otherwise create a new one.
        // Note that topics are trimmed and converted to lowercase.
        final Set<Topic> topicSet = topics.stream().map(topicService::create).collect(Collectors.toSet());

        // Register the user to the topics
        userTopicService.subscribe(user, topicSet);
    }

    @Override
    @Cacheable(value = CacheConfiguration.TOP_STORIES_CACHE_KEY)
    public Map<Topic, Story> getTopStories(final Long id) {

        LOG.info("Bypassing {} Cache for Key: [{}] ", CacheConfiguration.TOP_STORIES_CACHE_KEY, id);

        final User user = Optional.ofNullable(userRepository.findOne(id))
                                  .orElseThrow(() -> new BusinessErrorException(BusinessError.INVALID_USER));

        return user.getUserTopics()
                   .stream()
                   .filter(UserTopic::isEffective)
                   .map(UserTopic::getTopic)
                   .collect(HashMap::new, (topStories, topic)-> topStories.put(topic, topic.getTopStory()), HashMap::putAll);
    }

    @Override
    public Map<LocalDate, List<Digest>> getDigests(final Long id, final LocalDate start, final LocalDate end) {

        final User user = Optional.ofNullable(userRepository.findOne(id))
                .orElseThrow(() -> new BusinessErrorException(BusinessError.INVALID_USER));

        return digestService.findDigests(user, start, end);
    }

    @Override
    public Map<LocalDate, List<Digest>> getLatestDigests(final Long id) {

        final User user = Optional.ofNullable(userRepository.findOne(id))
                .orElseThrow(() -> new BusinessErrorException(BusinessError.INVALID_USER));

        return digestService.findLatestDigests(user);
    }

}

