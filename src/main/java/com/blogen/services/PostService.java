package com.blogen.services;

import com.blogen.api.v1.model.PostDTO;
import com.blogen.commands.PageCommand;
import com.blogen.commands.PostCommand;
import com.blogen.domain.Post;
import com.blogen.domain.User;
import org.springframework.data.domain.PageRequest;

import java.util.List;

/**
 * Service for CRUD operations on Blogen posts
 * @author Cliff
 */
public interface PostService {

    //get all posts CREATED by a user (including child posts)
    List<PostCommand> getAllPostsByUser( Long id );

    //get all blogen posts
    List<PostCommand> getAllPosts();

    //get a Post by ID
    PostCommand getPost( Long id );

    //get all posts for the specified category and page number
    PageCommand getAllPostsByCategoryForPage( Long categoryId, int page );

    //get all posts for the user id and category id and for the specified page number
    PageCommand getAllPostsByUserAndCategoryForPage( Long userId, Long categoryId, int page );

    //save a new Post (parent or child)
    PostCommand savePostCommand( PostCommand command );

    //update an existing post
    PostCommand updatePostCommand( PostCommand command );

    //get the ten most recent posts made
    List<PostCommand> getTenRecentPosts();

    //delete a post
    void deletePost( PostCommand command );

}
