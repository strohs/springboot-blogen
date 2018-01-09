package com.blogen.services;

import com.blogen.domain.Post;
import com.blogen.domain.User;
import com.blogen.repositories.PostRepository;
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

    @Autowired
    public PostServiceImpl( PostRepository postRepository ) {
        this.postRepository = postRepository;
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


}
