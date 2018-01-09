package com.blogen.repositories;

import com.blogen.domain.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 *
 * @author Cliff
 */
public interface PostRepository extends JpaRepository<Post,Long> {

   //find all parent posts
   List<Post> findAllByParentNull();
}
