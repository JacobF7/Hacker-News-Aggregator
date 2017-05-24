package com.uom.assignment.repository;

import com.uom.assignment.dao.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * {@link Repository} for the {@link User} entity.
 *
 * Created by jacobfalzon on 14/04/2017.
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Find a {@link User} with the given {@link User#username}.
     * Note that the {@link User#username} is unique across all {@link User}s.
     *
     * @param username the {@link User#username} of the desired {@link User}.
     * @return an {@link Optional} containing the {@link User} if a {@link User} exists with the given {@code username}, otherwise {@link Optional#empty()}.
     */
    Optional<User> findByUsername(String username);
}
