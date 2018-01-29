package com.blogen.api.v1.services;

import com.blogen.api.v1.model.PostDTO;
import com.blogen.api.v1.model.PostListDTO;

import java.util.List;

/**
 * Service interface for REST methods that operate on Blogen {@link com.blogen.domain.Post}(s)
 *
 * @author Cliff
 */
public interface PostService {

    /**
     * get 'size' amount of Post(s)
     * @param limit the number of Posts to retrieve. If <= 0 then by default 5 posts will be retrieved
     * @return a List of PostDTO ordered by Post.created date. The most recent posts made will be at the beginning of
     * the list
     */
    PostListDTO getPosts( int limit);

    /**
     * get a specific post by its ID
     *
     * @param id The id of the post to retrieve. If the id refers to a parent post, then the parent post and its
     *           children will be returned. If the ID refers to a child post, then only that child post will be returned
     * @return
     */
    PostDTO getPost(Long id);


    /**
     * Creates a new Parent Post. Any PostDTO.children sent will be ignored.
     *
     * @param postDTO contains post data to create. Any
     * @return a {@link PostDTO} representing the newly created post
     */
    PostDTO createNewPost( PostDTO postDTO );

    /**
     * Creates a new child post. The child post will be associated with the Parent Post represented by the parentId
     * @param parentId id of the parent post, under which this child will be created
     * @return a {@link PostDTO} representing the newly created child post
     */
    PostDTO createNewChildPost( Long parentId, PostDTO postDTO );

    /**
     * Saves/updates an existing Post
     * @param id the id of the Post to update
     * @param postDTO data to update the post with
     * @return a {@link PostDTO} containing the newly updated fields
     */
    PostDTO saveUpdatePost( Long id, PostDTO postDTO );

    /**
     * updates specific fields of a Post
     * @param id id of the Post to update
     * @param postDTO contains the fields to update. Only fields you want to update should be set
     * @return a {@link PostDTO} containing the newly updated post data
     */
    PostDTO patchPost( Long id, PostDTO postDTO );

    /**
     * Delete the post with the parentId
     * @param id ID of the post to delete
     */
    void deletePost( Long id );


}
