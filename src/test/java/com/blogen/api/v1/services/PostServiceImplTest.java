package com.blogen.api.v1.services;

import com.blogen.api.v1.mappers.PostMapper;
import com.blogen.api.v1.model.PostDTO;
import com.blogen.builders.Builder;
import com.blogen.domain.Category;
import com.blogen.domain.Post;
import com.blogen.domain.User;
import com.blogen.repositories.PostRepository;
import com.blogen.services.utils.PageRequestBuilder;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

/**
 * Unit Tests for REST PostServiceImpl
 * @author Cliff
 */
public class PostServiceImplTest {

    PostServiceImpl postService;

    @Mock
    PostRepository postRepository;

    @Mock
    PageRequestBuilder pageRequestBuilder;

    PostMapper postMapper  = PostMapper.INSTANCE;

    private static final Long   CAT1_ID   = 1L;
    private static final String CAT1_NAME = "Business";
    private static final Long   CAT2_ID   = 4L;
    private static final String CAT2_NAME = "Tech Gadgets";
    private static final Long   USER_ID   = 1L;
    private static final String USER_NAME = "johnny";
    private static final Long   USER2_ID   = 122L;
    private static final String USER2_NAME = "maggie";
    private static final Long   POST1_ID = 5L;
    private static final String POST1_TEXT = "post1 text";
    private static final Long   POST2_ID = 10L;
    private static final String POST2_TEXT = "post2 text";
    private static final Long   POST3_ID = 15L;
    private static final String POST3_TEXT = "post3 text";
    private static final Long   CHILD1_ID = 6L;
    private static final String CHILD1_TEXT = "child1 text";

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks( this );
        postService = new PostServiceImpl( pageRequestBuilder, postRepository, postMapper );
    }

    @Test
    public void should_getOnePost_when_getPostSizeIsOne() {
        int size = 1;
        Category cat1 = Builder.buildCategory( CAT1_ID, CAT1_NAME );
        User user1 = Builder.buildUser( USER_ID, USER_NAME, null , null,null,null,null  );
        Post post1 = Builder.buildPost( POST1_ID, null, POST1_TEXT, null, cat1, user1, null );
        List<Post> posts = new ArrayList<>();
        posts.add( post1 );
        PageRequest pageRequest = new PageRequest( 0,size,Sort.Direction.DESC,"category" );
        Page<Post> page = new PageImpl<Post>( posts );

        given( pageRequestBuilder.buildPageRequest( anyInt(), anyInt(), any( Sort.Direction.class ), anyString() )).willReturn( pageRequest );
        given( postRepository.findAllByOrderByCreatedDesc( any(Pageable.class) )).willReturn( page );

        List<PostDTO> postDTOS = postService.getPosts( size );

        then( pageRequestBuilder ).should().buildPageRequest( anyInt(),anyInt(),any(Sort.Direction.class), anyString() );
        then( postRepository).should().findAllByOrderByCreatedDesc( any( Pageable.class ) );
        assertThat( postDTOS.size(), is(1));
    }

    @Test
    public void should_getTwoPosts_when_getPostsSizeIsLessThanEqualToZero() {
        int size = -1;
        Category cat1 = Builder.buildCategory( CAT1_ID, CAT1_NAME );
        User user1 = Builder.buildUser( USER_ID, USER_NAME, null , null,null,null,null  );
        Post post1 = Builder.buildPost( POST1_ID, null, POST1_TEXT, null, cat1, user1, null );
        Post post2 = Builder.buildPost( POST2_ID, null, POST2_TEXT, null, cat1, user1, null );
        List<Post> posts = Arrays.asList( post1, post2 );
        PageRequest pageRequest = new PageRequest( 0,25,Sort.Direction.DESC,"category" );
        Page<Post> page = new PageImpl<Post>( posts );

        given( pageRequestBuilder.buildPageRequest( anyInt(), anyInt(), any( Sort.Direction.class ), anyString() )).willReturn( pageRequest );
        given( postRepository.findAllByOrderByCreatedDesc( any(Pageable.class) )).willReturn( page );
        ArgumentCaptor<Integer> argumentCaptor = ArgumentCaptor.forClass( Integer.class );

        List<PostDTO> postDTOS = postService.getPosts( size );

        verify( pageRequestBuilder ).buildPageRequest( anyInt(),argumentCaptor.capture(),any(Sort.Direction.class),anyString() );
        assertThat( argumentCaptor.getValue(), is(PostServiceImpl.MAX_PAGE_SIZE) );
        then( postRepository).should().findAllByOrderByCreatedDesc( any( Pageable.class ) );
        assertThat( postDTOS.size(), is(2));
    }

    @Test
    public void getPost() {
    }

    @Test
    public void createNewPost() {
    }

    @Test
    public void createNewChildPost() {
    }

    @Test
    public void saveUpdatePost() {
    }

    @Test
    public void patchPost() {
    }

    @Test
    public void deletePost() {
    }
}