package com.uom.assignment.repository;

import com.uom.assignment.dao.Topic;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * {@link Repository} for the {@link Topic} entity.
 *
 * Created by jacobfalzon on 06/05/2017.
 */
@Repository
public interface TopicRepository extends JpaRepository<Topic, Long> {

    /**
     * Find a {@link Topic} with the given {@link Topic#name}.
     * Note that the {@link Topic#name} is unique across all {@link Topic}s.
     *
     * @param name the {@link Topic#name} of the desired {@link Topic}.
     * @return an {@link Optional} containing the {@link Topic} if a {@link Topic} exists with the given {@code name}, otherwise {@link Optional#empty()}.
     */
    Optional<Topic> findByName(String name);

}
