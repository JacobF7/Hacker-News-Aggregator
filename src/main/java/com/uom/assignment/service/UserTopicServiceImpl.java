package com.uom.assignment.service;

import com.uom.assignment.dao.Topic;
import com.uom.assignment.dao.User;
import com.uom.assignment.dao.UserTopic;
import com.uom.assignment.repository.UserTopicRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

/**
 * The {@link Service} implementation for the {@link UserTopic} entity.
 *
 * Created by jacobfalzon on 11/05/2017.
 */
@Service
@Transactional
public class UserTopicServiceImpl implements UserTopicService {

    private final UserTopicRepository userTopicRepository;

    @Autowired
    public UserTopicServiceImpl(final UserTopicRepository userTopicRepository) {
        this.userTopicRepository = userTopicRepository;
    }

    @Override
    public void subscribe(final User user, final Set<Topic> topics) {

        // unsubscribe the user from any topic that is no longer effective
        user.getEffectiveUserTopics()
            .stream()
            .filter(userTopic -> !topics.contains(userTopic.getTopic()))
            .forEach(userTopic -> userTopic.setEffectiveTo(System.currentTimeMillis()));

        // only create a relationship between a user and some topic if the relationship does not exist OR is NOT currently effective.
        topics.stream()
              .filter(topic -> user.getEffectiveUserTopics().stream().map(UserTopic::getTopic).noneMatch(topic::equals))
              .map(topic -> new UserTopic(user, topic))
              .forEach(userTopicRepository::save);
    }

    @Override
    public Set<UserTopic> findEffectiveByTopic(final Topic topic) {
        return userTopicRepository.findByTopicAndEffectiveToIsNull(topic);
    }
}

