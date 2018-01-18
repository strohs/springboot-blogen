package com.blogen.repositories;

import com.blogen.domain.Post;
import com.blogen.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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

    /**
     * get all parent posts for the specified userId, using the pageable to control the page of results returned
     * @param userId
     * @param pageable
     * @return a lists of posts made by the specified user id to be displayed on the pageable
     */
   Page<Post> findAllByUser_IdAndParentNull( Long userId, Pageable pageable );

    /**
     *
     * @return the 10 most recent posts made
     */
   List<Post> findTop10ByOrderByCreatedDesc();
}
