package com.blogen.services;

import com.blogen.commands.PostCommand;
import com.blogen.commands.mappers.PostCommandMapper;
import com.blogen.domain.Category;
import com.blogen.domain.Post;
import com.blogen.domain.User;
import com.blogen.repositories.CategoryRepository;
import com.blogen.repositories.PostRepository;
import com.blogen.repositories.UserRepository;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Service for performing CRUD operations on Posts
 * @author Cliff
 */
@Log4j
@Service
public class PostServiceImpl implements PostService {

    private PostRepository postRepository;
    private UserRepository userRepository;
    private CategoryRepository categoryRepository;

    private PostCommandMapper postCommandMapper;

    @Autowired
    public PostServiceImpl( PostRepository postRepository, UserRepository userRepository,
                            CategoryRepository categoryRepository, PostCommandMapper postCommandMapper ) {
        this.postRepository = postRepository;
        this.userRepository = userRepository;
        this.categoryRepository = categoryRepository;
        this.postCommandMapper = postCommandMapper;
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
