package com.blogen.services;

import com.blogen.api.v1.model.PostDTO;
import com.blogen.commands.PostCommand;
import com.blogen.domain.Post;
import com.blogen.domain.User;

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

    //save a new Post (parent or child)
    PostCommand savePostCommand( PostCommand command );

    //update an existing post
    PostCommand updatePostCommand( PostCommand command );

    //delete a post
    void deletePost( PostCommand command );

}
