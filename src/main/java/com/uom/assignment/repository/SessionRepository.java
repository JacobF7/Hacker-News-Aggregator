package com.uom.assignment.repository;

import com.uom.assignment.dao.Session;
import com.uom.assignment.dao.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * {@link Repository} for the {@link Session} entity.
 *
 * Created by jacobfalzon on 14/04/2017.
 */
@Repository
public interface SessionRepository extends JpaRepository<Session, Long> {

    /**
     * Find all the {@link Session}s with the given {@link User#id}.
     *
     * @param id the {@link User#id} of the desired {@link Session}.
     * @return a {@link List} of {@link Session}s having the specified {@link User#id}.
     */
    List<Session> findByUserId(Long id);

    /**
     * Find a {@link Session} with the given {@link Session#token}.
     * Note that the {@link Session#token} is unique across all {@link Session}s.
     *
     * @param token the {@link Session#token} of the desired {@link Session}.
     * @return an {@link Optional} containing the {@link Session} if a {@link Session} exists with the given {@code token}, otherwise {@link Optional#empty()}.
     */
    Optional<Session> findByToken(String token);

    /**
     * Delete a {@link Session} with the given {@link Session#token}.
     * Note that the {@link Session#token} is unique across all {@link Session}s.
     *
     * @param token the {@link Session#token} of the {@link Session} to be deleted.
     * @return the number of deleted {@link Session}s. However, since the {@link Session#token} is unique, at most only one {@link Session} should be deleted.
     */
    Integer deleteByToken(String token);
}
