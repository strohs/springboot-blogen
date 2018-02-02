package com.blogen.services;

import com.blogen.api.v1.model.PostDTO;
import com.blogen.commands.PageCommand;
import com.blogen.commands.PostCommand;
import com.blogen.commands.SearchResultPageCommand;
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

    //get all posts for a userName
    List<PostCommand> getAllPostsByUserName( String userName );

    //get all blogen posts
    List<PostCommand> getAllPosts();

    //get a Post by ID
    PostCommand getPost( Long id );

    //get all posts for the specified category and page number
    PageCommand getAllPostsByCategoryForPage( Long categoryId, int pageNum );

    //get all posts for the user id and category id and for the specified page number
    PageCommand getAllPostsByUserAndCategoryForPage( Long userId, Long categoryId, int pageNum );

    //save a new Post (parent or child)
    PostCommand savePostCommand( PostCommand command );

    //update an existing post
    PostCommand updatePostCommand( PostCommand command );

    //search Post.text and Post.title for the specified searchStr
    SearchResultPageCommand searchPosts( String searchStr, int pageNum );

    //get the ten most recent posts made
    List<PostCommand> getTenRecentPosts();

    //delete a post
    void deletePost( PostCommand command );

}
