package com.blogen.repositories;

import com.blogen.domain.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @author Cliff
 */
public interface PostRepository extends JpaRepository<Post,Long> {


}
