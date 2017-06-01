package com.uom.assignment.repository;

import com.uom.assignment.dao.Digest;
import com.uom.assignment.dao.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;

/**
 * A {@link Repository} for the {@link Digest} entity.
 *
 * Created by jacobfalzon on 27/05/2017.
 */
@Repository
public interface DigestRepository extends JpaRepository<Digest, Long> {

    /**
     * Retrieve a {@link Set} of {@link Digest}s that were created for a particular {@link User} and where the {@link Digest#creationDate} occurs after {@code start} and before {@code end}.
     *
     * @param user the {@link User} for which the retrieved {@link Digest}s was created.
     * @param start the start date.
     * @param end the end date,
     * @return a {@link Set} of {@link Digest}s that were created for a particular {@link User} and where the {@link Digest#creationDate} occurs after {@code start} and before {@code end}.
     */
    Set<Digest> findByUsersAndCreationDateBetween(User user, Long start, Long end);
}
