package com.blogen.services;

import com.blogen.domain.Post;
import com.blogen.domain.User;

import java.util.List;

/**
 * Service for CRUD operations on Blogen posts
 * @author Cliff
 */
public interface PostService {

    //get ALL posts made by a user (including child posts)
    List<Post> getAllPosts( User user );

    //get all parent posts for a user
    List<Post> getAllParentPosts( User user );

    //get a Post by ID
    Post getPost( Long id );

    //update a Post with data from the newPost
    Post updatePost( Post oldPost, Post newPost );

    //delete a Post, shouldn't matter if parent or child post
    void deletePost( Post post );

    //delete a specific post by id
    void deletePostById( Long id );
}
