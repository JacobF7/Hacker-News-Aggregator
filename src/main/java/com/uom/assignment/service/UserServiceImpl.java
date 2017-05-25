package com.uom.assignment.service;

import com.uom.assignment.controller.BusinessError;
import com.uom.assignment.controller.BusinessErrorException;
import com.uom.assignment.dao.Story;
import com.uom.assignment.dao.Topic;
import com.uom.assignment.dao.User;
import com.uom.assignment.dao.UserTopic;
import com.uom.assignment.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * The {@link Service} implementation for the {@link User} entity.
 *
 * Created by jacobfalzon on 14/04/2017.
 */
@Service
@Transactional
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final TopicService topicService;
    private final UserTopicService userTopicService;
    private final StoryService storyService;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(final UserRepository userRepository, final TopicService topicService, final UserTopicService userTopicService, final StoryService storyService, final PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.topicService = topicService;
        this.userTopicService = userTopicService;
        this.storyService = storyService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
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
    public Map<Topic, Story> getRealTimeTopStories(final Long id) {

        final User user = Optional.ofNullable(userRepository.findOne(id))
                .orElseThrow(() -> new BusinessErrorException(BusinessError.INVALID_USER));

        return user.getUserTopics()
                .stream()
                .filter(UserTopic::isEffective)
                .map(UserTopic::getTopic)
                .collect(HashMap::new, (topStories, topic)-> topStories.put(topic, storyService.findTopStoryByTitleContaining(topic.getName()).orElse(null)), HashMap::putAll);
    }

    @Override
    public Map<Topic, Story> getTopStories(final Long id) {

        final User user = Optional.ofNullable(userRepository.findOne(id))
                                  .orElseThrow(() -> new BusinessErrorException(BusinessError.INVALID_USER));

        return user.getUserTopics()
                   .stream()
                   .filter(UserTopic::isEffective)
                   .map(UserTopic::getTopic)
                   .collect(HashMap::new, (topStories, topic)-> topStories.put(topic, topic.getTopStory()), HashMap::putAll);
    }
}

