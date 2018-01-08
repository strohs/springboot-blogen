package com.blogen.repositories;

import com.blogen.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author Cliff
 */
public interface UserRepository extends JpaRepository<User,Long> {
}
