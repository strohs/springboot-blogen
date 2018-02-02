package com.blogen.services;

import com.blogen.commands.CategoryCommand;
import com.blogen.commands.PageCommand;
import com.blogen.commands.PostCommand;
import com.blogen.commands.SearchResultPageCommand;
import com.blogen.commands.mappers.CategoryCommandMapper;
import com.blogen.commands.mappers.PostCommandMapper;
import com.blogen.domain.Category;
import com.blogen.domain.Post;
import com.blogen.domain.User;
import com.blogen.exceptions.NotFoundException;
import com.blogen.repositories.CategoryRepository;
import com.blogen.repositories.PostRepository;
import com.blogen.repositories.UserRepository;
import com.blogen.services.utils.PageRequestBuilder;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service for performing CRUD operations on Posts
 *
 * @author Cliff
 */
@Log4j
@Service
public class PostServiceImpl implements PostService {


    private static final String ALL_CATEGORIES = "All Categories";

    private PostRepository postRepository;
    private UserRepository userRepository;
    private CategoryRepository categoryRepository;
    private PrincipalService principalService;
    private PageRequestBuilder pageRequestBuilder;

    private PostCommandMapper postCommandMapper;
    private CategoryCommandMapper categoryCommandMapper;

    @Autowired
    public PostServiceImpl( PostRepository postRepository, UserRepository userRepository,
                            CategoryRepository categoryRepository, PrincipalService principalService, PostCommandMapper postCommandMapper,
                            CategoryCommandMapper categoryCommandMapper, PageRequestBuilder pageRequestBuilder ) {
        this.postRepository = postRepository;
        this.userRepository = userRepository;
        this.categoryRepository = categoryRepository;
        this.principalService = principalService;
        this.postCommandMapper = postCommandMapper;
        this.categoryCommandMapper = categoryCommandMapper;
        this.pageRequestBuilder = pageRequestBuilder;
    }



    @Override
    @Transactional
    public List<PostCommand> getAllPostsByUser( Long id ) {
        List<Post> posts = postRepository.findAllByUser_IdAndParentNullOrderByCreatedDesc( id );
        return posts.stream()
                .map( (Post p) -> postCommandMapper.postToPostCommand( p ))
                .collect( Collectors.toList());
    }

    @Override
    @Transactional
    public List<PostCommand> getAllPostsByUserName( String userName ) {
        List<Post> posts = postRepository.findAllByUser_userNameOrderByCreatedDesc( userName );
        return posts.stream()
                .map( (Post p) -> postCommandMapper.postToPostCommand( p ) )
                .collect( Collectors.toList());
    }

    @Override
    @Transactional
    public List<PostCommand> getAllPosts() {
        List<Post> posts = postRepository.findAllByParentNull();
        return posts.stream()
                .map( (Post p) -> postCommandMapper.postToPostCommand( p ) )
                .collect( Collectors.toList());
    }

    @Override
    public PostCommand getPost( Long id ) {
        Post post = postRepository.findOne( id );
        return postCommandMapper.postToPostCommand( post );
    }


    @Override
    @Transactional
    public PageCommand getAllPostsByCategoryForPage( Long categoryId, int pageNum ) {
        //build the PageRequest for the requested page
        PageRequest pageRequest = pageRequestBuilder.buildPostPageRequest( pageNum, Sort.Direction.DESC, "created" );
        PageCommand pageCommand = new PageCommand();
        Page<Post> page;

        if (categoryId == 0 ) {
            //all categories selected
            page = postRepository.findAllByParentNullOrderByCreatedDesc( pageRequest );
            pageCommand.setSelectedCategoryName( ALL_CATEGORIES );
        } else {
            //a specific category id was selected on the page
            page = postRepository.findAllByCategory_IdAndParentNullOrderByCreatedDesc( categoryId, pageRequest );
            Category selectedCategory = categoryRepository.findOne( categoryId );
            if ( selectedCategory == null )
                throw new NotFoundException( "Category not found with id " + categoryId );
            pageCommand.setSelectedCategoryName( selectedCategory.getName() );
        }
        //build the pageCommand objects
        List<PostCommand> postCommands = new ArrayList<>();
        page.forEach( (Post p) -> postCommands.add( postCommandMapper.postToPostCommand( p )));

        //get the list of categories to display on the web-page
        List<CategoryCommand> categoryCommands = categoryRepository.findAll()
                .stream()
                .map( categoryCommandMapper::categoryToCategoryCommand )
                .collect( Collectors.toList());

        //build the PageCommand
        pageCommand.setSelectedCategoryId( categoryId );
        pageCommand.setRequestedPage( pageNum );
        pageCommand.setTotalPages( page.getTotalPages() );
        pageCommand.setTotalElements( page.getTotalElements() );
        pageCommand.setPosts( postCommands );
        pageCommand.setCategories( categoryCommands );

        return pageCommand;
    }

    @Override
    @Transactional
    public PageCommand getAllPostsByUserAndCategoryForPage( Long userId, Long categoryId, int pageNum ) {
        //build the PageRequest for the requested page
        PageRequest pageRequest = pageRequestBuilder.buildPostPageRequest( pageNum, Sort.Direction.DESC, "created" );
        PageCommand pageCommand = new PageCommand();
        Page<Post> page;

        if (categoryId == 0 ) {
            //all categories selected
            page = postRepository.findAllByUser_IdAndParentNull( userId, pageRequest );
            pageCommand.setSelectedCategoryName( ALL_CATEGORIES );
        } else {
            //a specific category id was selected on the page
            page = postRepository.findAllByUser_IdAndCategory_IdAndParentNull( userId, categoryId, pageRequest );
            Category selectedCategory = categoryRepository.findOne( categoryId );
            if ( selectedCategory == null )
                throw new NotFoundException( "Category not found with id " + categoryId );
            pageCommand.setSelectedCategoryName( selectedCategory.getName() );

        }
        //build the PostCommand objects
        List<PostCommand> postCommands = new ArrayList<>();
        page.forEach( (Post p) -> postCommands.add( postCommandMapper.postToPostCommand( p )));

        //get the list of categories to display on the web-page
        List<CategoryCommand> categoryCommands = categoryRepository.findAll()
                .stream()
                .map( categoryCommandMapper::categoryToCategoryCommand )
                .collect( Collectors.toList());

        //build the PageCommand
        pageCommand.setSelectedCategoryId( categoryId );
        pageCommand.setRequestedPage( pageNum );
        pageCommand.setTotalPages( page.getTotalPages() );
        pageCommand.setTotalElements( page.getTotalElements() );
        pageCommand.setPosts( postCommands );
        pageCommand.setCategories( categoryCommands );

        return pageCommand;
    }


    /**
     * delete the post represented by the passed in PostCommand object
     *
     * NOTE: only admins OR the user that created the post can delete it
     * @param pc {@link PostCommand} containing details of the post to be deleted
     */
    @Override
    @Transactional
    @PreAuthorize( "hasAuthority('ADMIN') || #pc.userName == authentication.name" )
    public void deletePost( PostCommand pc ) {
        //check if post exists
        if ( isParentPost( pc ) ) {
            //delete the parent post
            postRepository.delete( pc.getId() );
        } else {
            //post to delete is a child post, need to get the parent post object and remove the child from it.
            Post parent = postRepository.findOne( pc.getParentId() );
            Post child = postRepository.findOne( pc.getId() );
            parent.removeChild( child );
            postRepository.delete( child.getId() );
        }
    }


    @Override
    @Transactional
    public PostCommand savePostCommand( PostCommand pc ) {
        if ( isParentPost( pc ) ) {
            return saveNewParentPostCommand( pc );
        } else {
            return saveNewChildPostCommand( pc );
        }
    }

    public PostCommand saveNewParentPostCommand( PostCommand pc ) {
        //get the username of the currently logged in principal
        String userName = principalService.getPrincipalUserName();
        //get user details
        User user = userRepository.findByUserName( userName );

        Post detachedPost = postCommandMapper.postCommandToPost( pc );

        //get CategoryById
        Category cat = categoryRepository.findByName( detachedPost.getCategory().getName() );

        //set category
        detachedPost.setCategory( cat );
        //set user
        detachedPost.setUser( user );

        Post savedPost = postRepository.saveAndFlush( detachedPost );
        return postCommandMapper.postToPostCommand( savedPost );
    }

    /**
     * saves the PostCommand to the DB.
     * @param pc The PostCommand to save. It is assumed to be a child Post
     * @return the Parent PostCommand object for the child that was just saved
     */
    public PostCommand saveNewChildPostCommand( PostCommand pc ) {
        //get the username of the currently logged in principal
        String userName = principalService.getPrincipalUserName();
        //get user details
        User user = userRepository.findByUserName( userName );

        Post detachedPost = postCommandMapper.postCommandToPost( pc );

        detachedPost.setUser( user );
        //get Parent Post
        Post parent = postRepository.findOne( pc.getParentId() );
        //get category - NOTE: child posts inherit the parents category
        Category cat = categoryRepository.findByName( parent.getCategory().getName() );
        detachedPost.setCategory( cat );
        parent.addChild( detachedPost );
        Post savedPost = postRepository.saveAndFlush( parent );
        //the parent PostCommand of the child that was saved is returned
        return postCommandMapper.postToPostCommand( savedPost );
    }

    @Override
    @Transactional
    @PreAuthorize( "hasAuthority('ADMIN') || #pc.userName == authentication.name" )
    public PostCommand updatePostCommand( PostCommand pc ) {
        Post detachedPost = postCommandMapper.postCommandToPost( pc );
        Post postToUpdate = postRepository.findOne( pc.getId() );
        if ( postToUpdate == null )
            throw new NotFoundException( "Post does not exist with id:" + pc.getId() );

        detachedPost.setCategory( categoryRepository.findByName( pc.getCategoryName() ));
        postToUpdate.setCreated( LocalDateTime.now() );
        //merge the updated fields from the web form
        mergePosts( detachedPost, postToUpdate );
        Post savedPost = postRepository.saveAndFlush( postToUpdate );
        return postCommandMapper.postToPostCommand( savedPost );
    }

    @Override
    public SearchResultPageCommand searchPosts( String searchStr, int pageNum ) {
        //build page request that returns results sorted by created date in descending order
        PageRequest pageRequest = pageRequestBuilder.buildPostPageRequest( pageNum, Sort.Direction.DESC,"created" );
        SearchResultPageCommand command = new SearchResultPageCommand();

        Page<Post> page = postRepository.findByTextOrTitleIgnoreCaseContaining( searchStr.toLowerCase(), pageRequest );

        //build PostCommand list
        List<PostCommand> postCommands = new ArrayList<>();
        page.forEach( (Post p) -> postCommands.add( postCommandMapper.postToPostCommand( p )));

        //build the SearchResultPageCommand
        command.setSearchStr( searchStr );
        command.setPosts( postCommands );
        command.setRequestedPage( pageNum );
        command.setTotalElements( page.getTotalElements() );
        command.setTotalPages( page.getTotalPages() );
        return command;
    }

    /**
     * Get ten recently created posts
     * @return the ten most recent posts made
     */
    @Override
    public List<PostCommand> getTenRecentPosts() {
        List<Post> recentPosts = postRepository.findTop10ByOrderByCreatedDesc();
        return recentPosts.stream().map( postCommandMapper::postToPostCommand ).collect( Collectors.toList());
    }

    /**
     * helper method for merging the fields of a post that can be changed on a web-form
     * @param source Post properties we are merging from
     * @param target Post properties we are merging to
     */
    private void mergePosts( Post source, Post target ) {
        target.setImageUrl( source.getImageUrl() );
        target.setTitle( source.getTitle() );
        target.setText( source.getText() );
        target.setCategory( source.getCategory() );
    }


    /**
     * Tests this post to see if it is a parent post. A parent post will have a parentId = null, otherwise this is
     * a child post.
     * @return true if this is a parent post, else return false (indicating a child post)
     */
    private boolean isParentPost( PostCommand command ) {
        return command.getParentId() == null;
    }
}
