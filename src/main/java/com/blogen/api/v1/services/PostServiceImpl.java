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

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Service for performing RESTful CRUD operations on Blogen {@link com.blogen.domain.Post}
 *
 * @author Cliff
 */
@Slf4j
@Service("postRestService")
public class PostServiceImpl implements PostService {

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
        //create a PageRequest
        PageRequest pageRequest = pageRequestBuilder.buildPageRequest( 0, limit, Sort.Direction.DESC,"created" );
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
        Post post = postRepository.findById( id ).orElseThrow( () -> new NotFoundException( "post not found with id:" + id ) );
        return buildReturnDto( post );
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
        Post parentPost = postRepository.findById( parentId ).orElseThrow( () ->
                new BadRequestException( "Post with id " + parentId + " was not found" ) );
        if ( !parentPost.isParentPost() )
            throw new BadRequestException( "Post with id: " + parentId + " is a child post. Cannot create a new child post onto an existing child post" );
        Post childPost = buildNewPost( postDTO );
        parentPost.addChild( childPost );
        Post savedPost = postRepository.saveAndFlush( parentPost );
        return buildReturnDto( savedPost );
    }

    @Override
    public PostDTO saveUpdatePost( Long id, PostDTO postDTO ) {
        Post postToUpdate = postRepository.findById( id ).orElseThrow( () ->
                new BadRequestException( "Post with id " + id + " was not found" ) );
        postToUpdate = mergePostDtoToPost( postToUpdate, postDTO );
        postToUpdate.setCreated( LocalDateTime.now() );
        Post savedPost = postRepository.save( postToUpdate );
        return buildReturnDto( savedPost );
    }

//    @Override
//    public PostDTO patchPost( Long id, PostDTO postDTO ) {
//        Post postToUpdate = postRepository.findOne( id );
//        if ( postToUpdate == null ) throw new NotFoundException( "Post with id " + id + " was not found" );
//        postToUpdate = postMapper.mergePostDtoToPost( postToUpdate, postDTO );
//        Post savedPost = postRepository.save( postToUpdate );
//        return buildReturnDto( savedPost );
//    }

    @Override
    @Transactional
    public void deletePost( Long id ) {
        Post post = postRepository.findById( id ).orElseThrow( () ->
                new BadRequestException( "Post with id " + id + " was not found" ) );
        if ( !post.isParentPost() ) {
            //post to delete is a child post, need to get the parent post object and remove the child from it.
            Post parent = post.getParent();
            parent.removeChild( post );
        }
        postRepository.delete( post );
    }

    /**
     * build a new {@link Post} object, making sure to retrieve existing data from the Category and User repositories
     * @param postDTO
     * @return
     */
    private Post buildNewPost( PostDTO postDTO ) {
        postDTO.setCreated( LocalDateTime.now() );
        Post post = postMapper.postDtoToPost( postDTO );
        Category category = categoryRepository.findByName( postDTO.getCategoryName() );
        if ( category == null ) throw new BadRequestException( "Category not found with name " + postDTO.getCategoryName() );
        User user = userRepository.findByUserName( postDTO.getUserName() );
        if ( user == null ) throw new BadRequestException( "User not found with name " + postDTO.getUserName() );
        post.setCategory( category );
        post.setUser( user );
        return post;
    }

    //helper method that builds a URL String to a particular post
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
     * build a PostDTO object and construct the URLs that get returned in the PostDTO
     * @param post - the Post domain object to convert into a PostDTO
     * @return a PostDTO
     */
    private PostDTO buildReturnDto( Post post ) {
        PostDTO postDTO = postMapper.postToPostDto( post );
        postDTO.setPostUrl( buildPostUrl( post ) );
        if ( post.getChildren() != null ) {
            for ( int i = 0; i < post.getChildren().size(); i++ ) {
                PostDTO childDTO = postDTO.getChildren().get( i );
                Post child = post.getChildren().get( i );
                childDTO.setPostUrl( buildPostUrl( post.getChildren().get( i ) ) );
                childDTO.setParentPostUrl( buildParentPostUrl( child ) );
            }
        }
        return postDTO;
    }

    /**
     * Merge non-null fields of PostDTO into a {@link Post} object
     * @param target Post object to merge fields into
     * @param source PostDTO containing the non-null fields you want to merge
     * @return a Post object containing the merged fields
     */
    private Post mergePostDtoToPost( Post target, PostDTO source ) {
        if ( source.getImageUrl() != null )
            target.setImageUrl( source.getImageUrl() );
        if ( source.getCategoryName() != null ) {
            Category category = validateCategoryName( source.getCategoryName() );
            target.setCategory( category );
        }
        if ( source.getUserName() != null ) {
            User user = validateUserName( source.getUserName() );
            target.setUser( user );
        }
        if ( source.getCreated() != null )
            target.setCreated( source.getCreated() );
        if ( source.getTitle() != null )
            target.setTitle( source.getTitle() );
        if ( source.getText() != null )
            target.setText( source.getText() );
        return target;
    }

    /**
     * validate that a category name exists in the repository
     * @param name category name to search for
     * @return the Category corresponding to the name
     * @throws BadRequestException if the category name does not exist in the repository
     */
    private Category validateCategoryName( String name ) throws BadRequestException {
        Category category = categoryRepository.findByName( name );
        if ( category == null ) throw new BadRequestException( "category with name: " + name + " does not exist" );
        return category;
    }

    /**
     * validate that a userName exists in the repository
     * @param name the username to search for
     * @return the {@link User} corresponding to the name
     * @throws BadRequestException if the username does not exist in the repository
     */
    private User validateUserName( String name ) throws BadRequestException {
        User user = userRepository.findByUserName( name );
        if ( user == null ) throw new BadRequestException( "user with name: " + name + " does not exist" );
        return user;
    }
}
