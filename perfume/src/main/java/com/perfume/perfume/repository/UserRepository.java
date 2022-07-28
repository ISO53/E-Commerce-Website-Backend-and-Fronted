package com.perfume.perfume.repository;

import com.perfume.perfume.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 *
 * @author ISO53
 */
@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
}
