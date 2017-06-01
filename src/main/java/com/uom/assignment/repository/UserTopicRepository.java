package com.uom.assignment.repository;

import com.uom.assignment.dao.Topic;
import com.uom.assignment.dao.User;
import com.uom.assignment.dao.UserTopic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

/**
 * A {@link Repository} for the {@link UserTopic} entity, which represents the relationship between {@link User}s and {@link Topic}s.
 *
 * Created by jacobfalzon on 11/05/2017.
 */
@Repository
public interface UserTopicRepository extends JpaRepository<UserTopic, Long> {

    /**
     * Retrieve the {@link Set} of effective {@link UserTopic}s for the given {@link UserTopic#topic}.
     * An effective {@link UserTopic} is a {@link Topic} which the {@link User} is currently subscribed to, i.e. {@link UserTopic#effectiveTo} is {@code null}.
     *
     * @param topic the {@link Topic} of the desired {@link UserTopic}s.
     * @return a {@link Set} of effective {@link UserTopic}s for the given {@link UserTopic#topic}.
     */
    Set<UserTopic> findByTopicAndEffectiveToIsNull(Topic topic);

}
