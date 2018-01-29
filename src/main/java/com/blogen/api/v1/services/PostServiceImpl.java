package com.blogen.api.v1.services;

import com.blogen.api.v1.mappers.PostMapper;
import com.blogen.api.v1.model.PostDTO;
import com.blogen.domain.Post;
import com.blogen.exceptions.NotFoundException;
import com.blogen.repositories.PostRepository;
import com.blogen.services.utils.PageRequestBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Cliff
 */
@Slf4j
@Service("postRestService")
public class PostServiceImpl implements PostService {

    public static final int MAX_PAGE_SIZE = 25;

    private PageRequestBuilder pageRequestBuilder;
    private PostRepository postRepository;
    private PostMapper postMapper;

    @Autowired
    public PostServiceImpl( PageRequestBuilder pageRequestBuilder, PostRepository postRepository, PostMapper postMapper ) {
        this.pageRequestBuilder = pageRequestBuilder;
        this.postRepository = postRepository;
        this.postMapper = postMapper;
    }

    @Override
    public List<PostDTO> getPosts( int size ) {
        //set the number of posts to retrieve
        int pageSize = size <= 0 ? MAX_PAGE_SIZE : size;
        //create a pageable
        PageRequest pageRequest = pageRequestBuilder.buildPageRequest( 0, pageSize, Sort.Direction.DESC,"created" );
        //retrieve the posts
        Page<Post> page = postRepository.findAllByOrderByCreatedDesc( pageRequest );
        List<PostDTO> postDTOS = new ArrayList<>();
        page.forEach( post -> postDTOS.add( postMapper.postToPostDto( post ) ) );
        return postDTOS;
    }

    @Override
    public PostDTO getPost( Long id ) {
        Post post = postRepository.findOne( id );
        if ( post == null ) throw new NotFoundException( "post not found with id:" + id );
        return postMapper.postToPostDto( post );
    }

    @Override
    public PostDTO createNewPost( PostDTO postDTO ) {
        Post post = postMapper.postDtoToPost( postDTO );
        //todo should we be sending id as part of post data
        //todo should we add a post_url field
        //set id to null to force creation of a new post
        post.setId( null );
        //user id required or possibly userName
        //category id required or possibly categoryName
        //title required
        //text required
        //imageUrl optional
        return null;
    }

    @Override
    public PostDTO createNewChildPost( Long parentId, PostDTO postDTO ) {
        //get parentId

        return null;
    }

    @Override
    public PostDTO saveUpdatePost( Long id, PostDTO postDTO ) {
        return null;
    }

    @Override
    public PostDTO patchPost( Long id, PostDTO postDTO ) {
        return null;
    }

    @Override
    public void deletePost( Long id ) {

    }
}
