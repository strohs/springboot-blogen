package com.blogen.repositories;

import com.blogen.domain.Category;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author Cliff
 */
public interface CategoryRepository extends JpaRepository<Category,Long> {

}
