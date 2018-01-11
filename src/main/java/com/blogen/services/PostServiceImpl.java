package com.blogen.services;

import com.blogen.commands.PostCommand;
import com.blogen.commands.mappers.PostCommandMapper;
import com.blogen.domain.Category;
import com.blogen.domain.Post;
import com.blogen.domain.User;
import com.blogen.repositories.CategoryRepository;
import com.blogen.repositories.PostRepository;
import com.blogen.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Service for performing CRUD operations on Posts
 * @author Cliff
 */
@Service
public class PostServiceImpl implements PostService {

    private PostRepository postRepository;
    private UserRepository userRepository;
    private CategoryRepository categoryRepository;

    private PostCommandMapper postCommandMapper = PostCommandMapper.INSTANCE;

    @Autowired
    public PostServiceImpl( PostRepository postRepository, UserRepository userRepository, CategoryRepository categoryRepository ) {
        this.postRepository = postRepository;
        this.userRepository = userRepository;
        this.categoryRepository = categoryRepository;
    }

    @Override
    public List<Post> getAllPosts( User user ) {
        return null;
    }

    @Override
    public List<Post> getAllParentPosts( User user ) {
        return null;
    }

    @Override
    public Post getPost( Long id ) {
        return postRepository.findOne( id );
    }

    @Override
    public Post updatePost( Post oldPost, Post newPost ) {
        return null;
    }

    @Override
    @Transactional
    public void deletePost( Post post ) {
        if ( !post.isParentPost( ) ) {
            //post to delete is a child post, need to get the parent post object and remove the child from it.
            Post parent = post.getParent();
            parent.removeChild( post );
        }
        postRepository.delete( post );
    }

    @Override
    public void deletePostById( Long id ) {
        //todo if we end up using this method, will need to add child post detection logic
        postRepository.delete( id );
    }

    @Override
    public PostCommand savePostCommand( PostCommand pc ) {
        if ( pc.getParentId() == null ) {
            return saveNewParentPostCommand( pc );
        } else {
            return saveNewChildPostCommand( pc );
        }
    }

    @Transactional
    public PostCommand saveNewParentPostCommand( PostCommand pc ) {
        Post detachedPost = postCommandMapper.postCommandToPost( pc );
        //1. get CategoryById
        Category cat = categoryRepository.findOne( detachedPost.getCategory().getId() );
        //2. get UserById
        User user = userRepository.findOne( detachedPost.getUser().getId() );
        //3. set category
        detachedPost.setCategory( cat );
        //4. set user
        detachedPost.setUser( user );

        Post savedPost = postRepository.saveAndFlush( detachedPost );
        return postCommandMapper.postToPostCommand( savedPost );
    }

    @Transactional
    public PostCommand saveNewChildPostCommand( PostCommand pc ) {
        Post detachedPost = postCommandMapper.postCommandToPost( pc );
        //get category
        Category cat = categoryRepository.findOne( detachedPost.getCategory().getId() );
        //get user
        User user = userRepository.findOne( detachedPost.getUser().getId() );
        detachedPost.setCategory( cat );
        detachedPost.setUser( user );
        //get Parent Post
        Post parent = postRepository.findOne( pc.getParentId() );
        parent.addChild( detachedPost );
        Post savedPost = postRepository.saveAndFlush( parent );
        return postCommandMapper.postToPostCommand( savedPost );
    }
}
