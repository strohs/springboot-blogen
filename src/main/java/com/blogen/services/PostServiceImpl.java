package com.blogen.services;

import com.blogen.domain.Post;
import com.blogen.repositories.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    public Post getPost( Long id ) {
        return postRepository.findOne( id );
    }

    @Override
    @Transactional
    public void deletePost( Post post ) {
        if ( !isParentPost( post ) ) {
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

    /**
     * A blogen post is either a parent post or a child of a parent post.
     * check if a post is a parent post. A parent post will have a parent_id == null;
     * @param post - the post to check for parent status
     * @return true if post is a parent post
     */
    private boolean isParentPost( Post post ) {
        return post.getParent() == null;
    }
}
