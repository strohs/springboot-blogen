package com.blogen.repositories;

import com.blogen.domain.UserPrefs;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 *
 * User: Cliff
 */
public interface UserPrefsRepository extends JpaRepository<UserPrefs,Long> {

}
