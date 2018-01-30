package com.blogen.api.v1.services;

import com.blogen.api.v1.controllers.PostRestController;
import com.blogen.api.v1.mappers.PostMapper;
import com.blogen.api.v1.model.PostDTO;
import com.blogen.api.v1.model.PostListDTO;
import com.blogen.domain.Category;
import com.blogen.domain.Post;
import com.blogen.domain.User;
import com.blogen.exceptions.BadRequestException;
import com.blogen.exceptions.NotFoundException;
import com.blogen.repositories.CategoryRepository;
import com.blogen.repositories.PostRepository;
import com.blogen.repositories.UserRepository;
import com.blogen.services.utils.PageRequestBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Cliff
 */
@Slf4j
@Service("postRestService")
public class PostServiceImpl implements PostService {

    public static final int MAX_PAGE_SIZE = 5;

    private PageRequestBuilder pageRequestBuilder;
    private PostRepository postRepository;
    private CategoryRepository categoryRepository;
    private UserRepository userRepository;
    private PostMapper postMapper;

    @Autowired
    public PostServiceImpl( PageRequestBuilder pageRequestBuilder, PostRepository postRepository,
                            CategoryRepository categoryRepository, UserRepository userRepository, PostMapper postMapper ) {
        this.pageRequestBuilder = pageRequestBuilder;
        this.postRepository = postRepository;
        this.categoryRepository = categoryRepository;
        this.userRepository = userRepository;
        this.postMapper = postMapper;
    }

    @Override
    public PostListDTO getPosts( int limit ) {
        //set the number of posts to retrieve
        int pageSize = limit <= 0 ? MAX_PAGE_SIZE : limit;
        //create a PageRequest
        PageRequest pageRequest = pageRequestBuilder.buildPageRequest( 0, pageSize, Sort.Direction.DESC,"created" );
        //retrieve the posts
        Page<Post> page = postRepository.findAllByParentNullOrderByCreatedDesc( pageRequest );
        List<PostDTO> postDTOS = new ArrayList<>();
        page.forEach( post -> {
            PostDTO dto = buildReturnDto( post );
            postDTOS.add( dto );
        } );
        return new PostListDTO( postDTOS );
    }

    @Override
    public PostDTO getPost( Long id ) {
        Post post = postRepository.findOne( id );
        if ( post == null ) throw new NotFoundException( "post not found with id:" + id );
        return postMapper.postToPostDto( post );
    }

    @Override
    @Transactional
    //creates a new Parent Post
    public PostDTO createNewPost( PostDTO postDTO ) {
        Post post = buildNewPost( postDTO );
        Post savedPost = postRepository.save( post );
        return buildReturnDto( savedPost );
    }

    @Override
    @Transactional
    public PostDTO createNewChildPost( Long parentId, PostDTO postDTO ) {
        Post parentPost = postRepository.findOne( parentId );
        if ( parentPost == null ) throw new NotFoundException( "Post with id " + parentId + " was not found" );
        Post childPost = buildNewPost( postDTO );
        parentPost.addChild( childPost );
        Post savedPost = postRepository.save( parentPost );
        return buildReturnDto( savedPost );
    }

    @Override
    public PostDTO saveUpdatePost( Long id, PostDTO postDTO ) {
        Post postToUpdate = postRepository.findOne( id );
        if ( postToUpdate == null ) throw new NotFoundException( "Post with id " + id + " was not found" );
        //any child posts set in dto should be ignored, may want to throw exception in the future
        postDTO.getChildren().clear();
        //todo may need to check postDTO for required fields
        postToUpdate = postMapper.updatePostFromDTO( postDTO, postToUpdate );
        Post savedPost = postRepository.save( postToUpdate );
        return buildReturnDto( savedPost );
    }

    @Override
    public PostDTO patchPost( Long id, PostDTO postDTO ) {
        Post postToUpdate = postRepository.findOne( id );
        if ( postToUpdate == null ) throw new NotFoundException( "Post with id " + id + " was not found" );
        postToUpdate = postMapper.mergePostDtoToPost( postToUpdate, postDTO );
        Post savedPost = postRepository.save( postToUpdate );
        return buildReturnDto( savedPost );
    }

    @Override
    @Transactional
    public void deletePost( Long id ) {
        Post post = postRepository.findOne( id );
        if ( post == null ) throw new NotFoundException( "Post with id " + id + " was not found" );
        postRepository.delete( post );
    }

    /**
     * build a new {@link Post} object, making sure to retrieve existing data from the Category and User repositories
     * @param postDTO
     * @return
     */
    private Post buildNewPost( PostDTO postDTO ) {
        Post post = postMapper.postDtoToPost( postDTO );
        Category category = categoryRepository.findByName( postDTO.getCategoryName() );
        if ( category == null ) throw new BadRequestException( "Category not found with name " + postDTO.getCategoryName() );
        User user = userRepository.findByUserName( postDTO.getUserName() );
        if ( user == null ) throw new BadRequestException( "User not found with name " + postDTO.getUserName() );
        post.setCategory( category );
        post.setUser( user );
        return post;
    }

    private String buildPostUrl( Post post ) {
        return PostRestController.BASE_URL + "/" + post.getId();
    }
    private String buildParentPostUrl( Post post ) {
        String url = null;
        //if the post is a child post, set the parent URL
        if ( !post.isParentPost() ) url = PostRestController.BASE_URL + "/" + post.getParent().getId();
        return url;
    }

    /**
     * build a PostDTO and the URLs that get returned in the PostDTO
     * @param post
     * @return
     */
    private PostDTO buildReturnDto( Post post ) {
        PostDTO postDTO = postMapper.postToPostDto( post );
        postDTO.setPostUrl( buildPostUrl( post ) );
        for ( int i = 0; i < post.getChildren().size(); i++ ) {
            PostDTO childDTO = postDTO.getChildren().get( i );
            Post child = post.getChildren().get( i );
            childDTO.setPostUrl( buildPostUrl( post.getChildren().get( i ) ) );
            childDTO.setParentPostUrl( buildParentPostUrl( child ) );
        }
        return postDTO;
    }
}
