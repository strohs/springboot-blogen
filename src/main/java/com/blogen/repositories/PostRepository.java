package com.blogen.repositories;

import com.blogen.domain.Post;
import com.blogen.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 *
 * @author Cliff
 */
public interface PostRepository extends JpaRepository<Post,Long> {

    /**
     * find all parent posts. Parent posts have a PARENT_ID = NULL in the database.
     * @return a List containing all parent posts.
     */
   List<Post> findAllByParentNull();

    /**
     * find all parent posts CREATED by a user with the specified user.id
     * @param userId - user.id to search for
     * @return a List of parent posts, posted by the specified user id
     */
   List<Post> findAllByUser_IdAndParentNullOrderByCreatedDesc( Long userId );
}
