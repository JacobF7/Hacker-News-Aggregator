package com.uom.assignment.repository;

import com.uom.assignment.dao.Topic;
import com.uom.assignment.dao.User;
import com.uom.assignment.dao.UserTopic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * A {@link Repository} for the {@link UserTopic} entity, which represents the relationship between {@link User}s and {@link Topic}s.
 *
 * Created by jacobfalzon on 11/05/2017.
 */
@Repository
public interface UserTopicRepository extends JpaRepository<UserTopic, Long> {

}
