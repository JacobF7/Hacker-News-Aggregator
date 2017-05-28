package com.uom.assignment.repository;

import com.uom.assignment.dao.Digest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * A {@link Repository} for the {@link Digest} entity.
 *
 * Created by jacobfalzon on 27/05/2017.
 */
@Repository
public interface DigestRepository extends JpaRepository<Digest, Long> {
}
