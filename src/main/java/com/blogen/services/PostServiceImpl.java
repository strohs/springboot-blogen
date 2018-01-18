package com.blogen.services;

import com.blogen.commands.CategoryCommand;
import com.blogen.commands.PageCommand;
import com.blogen.commands.PostCommand;
import com.blogen.commands.mappers.CategoryCommandMapper;
import com.blogen.commands.mappers.PostCommandMapper;
import com.blogen.domain.Category;
import com.blogen.domain.Post;
import com.blogen.domain.User;
import com.blogen.repositories.CategoryRepository;
import com.blogen.repositories.PostRepository;
import com.blogen.repositories.UserRepository;

import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service for performing CRUD operations on Posts
 * @author Cliff
 */
@Log4j
@Service
public class PostServiceImpl implements PostService {

    //number of parent posts to display on the posts.html page
    @Value( "${blogen.posts.per.page}" )
    private int POSTS_PER_PAGE;

    private PostRepository postRepository;
    private UserRepository userRepository;
    private CategoryRepository categoryRepository;

    private PostCommandMapper postCommandMapper;
    private CategoryCommandMapper categoryCommandMapper;

    @Autowired
    public PostServiceImpl( PostRepository postRepository, UserRepository userRepository,
                            CategoryRepository categoryRepository, PostCommandMapper postCommandMapper,
                            CategoryCommandMapper categoryCommandMapper ) {
        this.postRepository = postRepository;
        this.userRepository = userRepository;
        this.categoryRepository = categoryRepository;
        this.postCommandMapper = postCommandMapper;
        this.categoryCommandMapper = categoryCommandMapper;
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
    public PageCommand getAllPostsForPage( int pageNum ) {
        //build the PageRequest for the requested page
        PageRequest pageRequest = new PageRequest( pageNum, POSTS_PER_PAGE, Sort.Direction.DESC, "created" );
        Page<Post> page = postRepository.findAllByParentNullOrderByCreatedDesc( pageRequest );
        List<PostCommand> commands = new ArrayList<>();
        page.forEach( (Post p) -> commands.add( postCommandMapper.postToPostCommand( p )));
        List<CategoryCommand> categoryCommands = categoryRepository.findAll()
                .stream()
                .map( categoryCommandMapper::categoryToCategoryCommand )
                .collect( Collectors.toList());

        return new PageCommand( pageNum, page.getTotalPages(), page.getTotalElements(), commands, categoryCommands );
    }

    @Override
    @Transactional
    public PageCommand getAllPostsByUserForPage( Long id, int pageNum ) {
        //build the PageRequest for the requested page
        PageRequest pageRequest = new PageRequest( pageNum, POSTS_PER_PAGE, Sort.Direction.DESC, "created" );
        Page<Post> page = postRepository.findAllByUser_IdAndParentNull( id, pageRequest );
        //build the List of PostCommands
        List<PostCommand> commands = new ArrayList<>();
        page.forEach( (Post p) -> commands.add( postCommandMapper.postToPostCommand( p )));
        //build the list of Categories
        List<CategoryCommand> categoryCommands = categoryRepository.findAll()
                .stream()
                .map( categoryCommandMapper::categoryToCategoryCommand )
                .collect( Collectors.toList());

        return new PageCommand( pageNum, page.getTotalPages(), page.getTotalElements(), commands, categoryCommands );
    }



    @Override
    @Transactional
    public void deletePost( PostCommand pc ) {
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
    public PostCommand savePostCommand( PostCommand pc ) {
        if ( isParentPost( pc ) ) {
            return saveNewParentPostCommand( pc );
        } else {
            return saveNewChildPostCommand( pc );
        }
    }

    @Transactional
    public PostCommand saveNewParentPostCommand( PostCommand pc ) {
        Post detachedPost = postCommandMapper.postCommandToPost( pc );
        //1. get CategoryById
        Category cat = categoryRepository.findByName( detachedPost.getCategory().getName() );
        //2. get UserById
        User user = userRepository.findOne( detachedPost.getUser().getId() );
        //3. set category
        detachedPost.setCategory( cat );
        //4. set user
        detachedPost.setUser( user );

        Post savedPost = postRepository.saveAndFlush( detachedPost );
        return postCommandMapper.postToPostCommand( savedPost );
    }

    /**
     * saves the PostCommand to the DB.
     * @param pc The PostCommand to save. It is assumed to be a child Post
     * @return the Parent PostCommand object for the child that was just saved
     */
    @Transactional
    public PostCommand saveNewChildPostCommand( PostCommand pc ) {
        Post detachedPost = postCommandMapper.postCommandToPost( pc );
        //get user
        User user = userRepository.findOne( detachedPost.getUser().getId() );
        detachedPost.setUser( user );
        //get Parent Post
        Post parent = postRepository.findOne( pc.getParentId() );
        //get category - child posts inherit the parents category
        Category cat = categoryRepository.findByName( parent.getCategory().getName() );
        detachedPost.setCategory( cat );
        parent.addChild( detachedPost );
        Post savedPost = postRepository.saveAndFlush( parent );
        //the parent PostCommand of the child that was saved is returned
        return postCommandMapper.postToPostCommand( savedPost );
    }

    @Override
    @Transactional
    public PostCommand updatePostCommand( PostCommand pc ) {
        Post detachedPost = postCommandMapper.postCommandToPost( pc );
        Post postToUpdate = postRepository.findOne( pc.getId() );
        //merge the updated fields from the web form
        mergePosts( detachedPost, postToUpdate );
        postToUpdate.setCategory( categoryRepository.findByName( pc.getCategoryName() ));

        Post savedPost = postRepository.saveAndFlush( postToUpdate );
        return postCommandMapper.postToPostCommand( savedPost );
    }

    /**
     *
     * @return the ten most recent posts made
     */
    @Override
    public List<PostCommand> getTenRecentPosts() {
        List<Post> recentPosts = postRepository.findTop10ByOrderByCreatedDesc();
        return recentPosts.stream().map( postCommandMapper::postToPostCommand ).collect( Collectors.toList());
    }

    /**
     * helper method for merging the fields of a post that can be changed on a web-form
     * @param source
     * @param target
     */
    private void mergePosts( Post source, Post target ) {
        target.setImageUrl( source.getImageUrl() );
        target.setTitle( source.getTitle() );
        target.setText( source.getText() );;
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
