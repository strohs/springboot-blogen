package com.blogen.services;

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
import org.junit.Before;
import org.junit.Test;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Matchers.*;

/**
 * Unit Tests for {@link PostServiceImpl}
 *
 * @author Cliff
 */
public class PostServiceImplTest {

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

    private PostServiceImpl postService;

    @Mock
    private PostRepository postRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PrincipalService principalService;

    @Mock
    private PageRequestBuilder pageRequestBuilder;

    private PostCommandMapper postCommandMapper = PostCommandMapper.INSTANCE;
    private CategoryCommandMapper categoryCommandMapper = CategoryCommandMapper.INSTANCE;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks( this );
        postService = new PostServiceImpl( postRepository, userRepository, categoryRepository, principalService, postCommandMapper,categoryCommandMapper,pageRequestBuilder );
    }

    @Test
    public void shouldReturnTwoParentPosts_when_getAllPostsByUser() {
        //this user has two parent posts
        User user = getUser1();
        Post p1 = getParentPost1();
        Post p3 = getParentPost3();

        List<Post> posts = Arrays.asList( p1,p3 );

        given( postRepository.findAllByUser_IdAndParentNullOrderByCreatedDesc( anyLong() )).willReturn( posts );

        List<PostCommand> commands = postService.getAllPostsByUser( user.getId() );

        then( postRepository ).should(  ).findAllByUser_IdAndParentNullOrderByCreatedDesc( anyLong() );
        assertThat( commands, is(notNullValue()));
        assertThat( commands.size(), is( 2));

    }

    @Test
    public void should_returnTwoParentPostsInBusinessCategory_when_getAllPostsByCategoryForPage() {
        Post p1 = getParentPost1();
        Post p2 = getParentPost2();
        Post p3 = getParentPost3();
        Category cat1 = getCategory1();
        Category cat2 = getCategory2();
        p3.setCategory( getCategory2() );
        List<Post> posts = Arrays.asList( p1,p2 );
        List<Category> categories = Arrays.asList( cat1,cat2 );
        PageRequest pageRequest = new PageRequest( 0,4, Sort.Direction.DESC, "created" );
        Page<Post> page = new PageImpl<Post>( posts );

        given( postRepository.findAllByCategory_IdAndParentNullOrderByCreatedDesc( anyLong(), Matchers.any( Pageable.class ) )).willReturn( page );
        given( categoryRepository.findOne( anyLong() )).willReturn( cat1 );
        given( categoryRepository.findAll() ).willReturn( categories );
        given( pageRequestBuilder.buildPageRequest( anyInt(), Matchers.any( Sort.Direction.class ), anyString()) ).willReturn( pageRequest );

        PageCommand pageCommand = postService.getAllPostsByCategoryForPage( CAT1_ID,0 );

        then( postRepository ).should(  ).findAllByCategory_IdAndParentNullOrderByCreatedDesc( anyLong(), Matchers.any( Pageable.class ) );
        assertThat( pageCommand, is(notNullValue()));
        assertThat( pageCommand.getTotalElements(), is( 2L));
        assertThat( pageCommand.getSelectedCategoryId(), is(CAT1_ID));

    }

    @Test
    public void should_ReturnAllParentPosts_when_getAllPosts() {
        Post p1 = getParentPost1();
        Post p2 = getParentPost2();
        Post p3 = getParentPost3();

        List<Post> posts = Arrays.asList( p1,p2,p3 );

        given( postRepository.findAllByParentNull() ).willReturn( posts );

        //when
        List<PostCommand> commands = postService.getAllPosts();

        then( postRepository ).should().findAllByParentNull();
        assertThat( commands, is( notNullValue() ));
        assertThat( commands.size(), is(3));
    }

    @Test
    public void should_GetOnePost_when_getPost() {
        Post p1 = getParentPost1();

        given( postRepository.findOne( anyLong() )).willReturn( p1 );

        PostCommand pc = postService.getPost( POST1_ID );

        then( postRepository ).should().findOne( anyLong() );
        assertThat( pc, is(notNullValue()));
        assertThat( pc.getId(), is( POST1_ID ));
    }

    //TODO test for when ID is not found

    @Test
    public void should_DeleteParentPost_when_deletePostIsCalledWithParentPostCommand() {
        Post p1 = getParentPost1();
        PostCommand pc = postCommandMapper.postToPostCommand( p1 );

        postService.deletePost( pc );

        then( postRepository ).should().delete( anyLong() );
    }


    @Test
    public void should_DeleteChildPost_when_deletePostIsCalledWithChildPostCommand() {
        Post p1 = getParentPost1();
        Post c1 = getChildPost1();
        p1.addChild( c1 );

        PostCommand pc = postCommandMapper.postToPostCommand( c1 );

        given( postRepository.findOne( p1.getId() )).willReturn( p1 );
        given( postRepository.findOne( c1.getId() )).willReturn( c1 );
        postService.deletePost( pc );

        then( postRepository ).should().delete( c1.getId() );
        then( postRepository ).should().findOne( p1.getId() );
        then( postRepository ).should().findOne( c1.getId() );
    }

    @Test
    public void should_SaveNewPost_when_saveNewParentPostCommand() {
        Post post = getParentPost1();
        Category cat = getCategory1();
        User user1 = getUser1();
        PostCommand pc = postCommandMapper.postToPostCommand( post );
        pc.setId( null );

        given( categoryRepository.findByName( anyString() )).willReturn( cat );
        given( userRepository.findByUserName( anyString() )).willReturn( user1 );
        given( postRepository.saveAndFlush( Matchers.any( Post.class ) )).willReturn( post );
        given( principalService.getPrincipalUserName() ).willReturn( USER_NAME );

        postService.savePostCommand( pc );

        then( postRepository ).should().saveAndFlush( Matchers.any( Post.class) );
        then( categoryRepository).should().findByName( anyString() );
        then( userRepository ).should().findByUserName( anyString() );
    }

    @Test
    public void should_SaveNewChild_when_saveNewChildPostCommand() {
        Post post = getParentPost1();
        Post child = getChildPost1();
        Category cat = getCategory1();
        User user1 = getUser1();
        PostCommand pc = postCommandMapper.postToPostCommand( child );
        pc.setParentId( post.getId() );

        given( categoryRepository.findByName( anyString() )).willReturn( cat );
        given( userRepository.findByUserName( anyString() )).willReturn( user1 );
        given( postRepository.findOne( anyLong() )).willReturn( post );
        given( postRepository.saveAndFlush( Matchers.any( Post.class ) )).willReturn( post );
        given( principalService.getPrincipalUserName() ).willReturn( USER_NAME );

        PostCommand savedCommand = postService.savePostCommand( pc );

        then( postRepository ).should().saveAndFlush( Matchers.any( Post.class) );
        then( categoryRepository).should().findByName( anyString() );
        then( userRepository ).should().findByUserName( anyString() );
        then( postRepository ).should().findOne( anyLong() );
        assertThat( savedCommand.getId(), is(POST1_ID) );
        assertThat( savedCommand.getChildren().size(), is(1));
        assertThat( savedCommand.getChildren().get( 0 ).getId(), is(CHILD1_ID));
    }

    @Test
    public void should_UpdatePostWithNewText_when_updatingExistingPost() {
        Post existingPost = getParentPost1();
        Category cat = getCategory1();
        String newText = "new post text";

        Post updatedPost = getParentPost1();
        updatedPost.setText( newText );
        PostCommand command = postCommandMapper.postToPostCommand( updatedPost );

        given( postRepository.findOne( anyLong() )).willReturn( existingPost );
        given( categoryRepository.findByName( anyString() )).willReturn( cat );
        given( postRepository.saveAndFlush( Matchers.any(Post.class) )).willReturn( updatedPost );

        PostCommand savedCommand = postService.updatePostCommand( command );

        then( postRepository ).should().saveAndFlush( Matchers.any(Post.class) );
        then( categoryRepository ).should().findByName( anyString() );
        then( postRepository ).should().findOne( anyLong() );
        assertThat( savedCommand.getId(), is(POST1_ID) );
        assertThat( savedCommand.getText(), is(newText) );
    }

    @Test(expected = NotFoundException.class)
    public void should_throwNotFoundException_when_updatingNonExistingPost() throws Exception {
        Post existingPost = getParentPost1();
        Category cat = getCategory1();
        String newText = "new post text";

        Post updatedPost = getParentPost1();
        updatedPost.setText( newText );
        //this post does not exist
        updatedPost.setId( 99999L );
        PostCommand command = postCommandMapper.postToPostCommand( updatedPost );

        given( postRepository.findOne( anyLong() )).willReturn( null );
        given( categoryRepository.findByName( anyString() )).willReturn( cat );
        given( postRepository.saveAndFlush( Matchers.any(Post.class) )).willReturn( updatedPost );

        PostCommand savedCommand = postService.updatePostCommand( command );

    }

    @Test
    public void should_getTenPosts_when_getTenRecentPosts() throws Exception {
        Post post1 = getParentPost1();
        Post post2 = getParentPost2();
        Post post3 = getParentPost3();
        Post post4 = getParentPost1();
        Post post5 = getParentPost2();
        Post post6 = getParentPost3();
        Post post7 = getParentPost1();
        Post post8 = getParentPost1();
        Post post9 = getParentPost2();
        Post post10 = getParentPost3();
        List<Post> posts = Arrays.asList( post1,post2,post3,post4,post5,post6,post7,post8,post9,post10 );

        given( postRepository.findTop10ByOrderByCreatedDesc() ).willReturn( posts );

        List<PostCommand> recentPosts = postService.getTenRecentPosts();

        then( postRepository ).should().findTop10ByOrderByCreatedDesc();
        assertThat( recentPosts.size(), is(10));
    }

    @Test
    public void should_returnPageWithTwoPostsContainingSearchString_when_searchPosts() {
        String searchStr = "groovy";
        Post p1 = getParentPost1();
        p1.setText( "this groovy post is first" );
        Post p2 = getParentPost2();
        p2.setText("this is groovy post two");
        Post p3 = getParentPost3();
        p3.setText("this is regular post three");
        List<Post> posts = Arrays.asList( p1,p2 );
        PageRequest pageRequest = new PageRequest( 0,4, Sort.Direction.DESC, "created" );
        Page<Post> page = new PageImpl<Post>( posts );

        given( postRepository.findByTextOrTitleIgnoreCaseContaining( anyString(), Matchers.any( Pageable.class ) )).willReturn( page );
        given( pageRequestBuilder.buildPageRequest( anyInt(), Matchers.any( Sort.Direction.class ), anyString()) ).willReturn( pageRequest );

        SearchResultPageCommand command = postService.searchPosts( searchStr, 0 );

        then( postRepository ).should().findByTextOrTitleIgnoreCaseContaining( anyString(), Matchers.any( Pageable.class ) );
        then( pageRequestBuilder ).should().buildPageRequest( anyInt(), Matchers.any( Sort.Direction.class ), anyString() );
        assertThat( command, is(notNullValue()));
        assertThat( command.getTotalElements(), is( 2L));
        assertThat( command.getPosts().get( 0 ).getText().contains( searchStr ), is(true) );
    }

    @Test
    public void should_returnPageWithNoPosts_when_searchPostsForTextThatDoesNotExist() {
        String searchStr = "shazam";
        Post p1 = getParentPost1();
        p1.setText( "this groovy post is first" );
        Post p2 = getParentPost2();
        p2.setText("this is groovy post two");
        Post p3 = getParentPost3();
        p3.setText("this is regular post three");
        List<Post> posts = new ArrayList<>();
        PageRequest pageRequest = new PageRequest( 0,4, Sort.Direction.DESC, "created" );
        Page<Post> page = new PageImpl<Post>( posts );

        given( postRepository.findByTextOrTitleIgnoreCaseContaining( anyString(), Matchers.any( Pageable.class ) )).willReturn( page );
        given( pageRequestBuilder.buildPageRequest( anyInt(), Matchers.any( Sort.Direction.class ), anyString()) ).willReturn( pageRequest );

        SearchResultPageCommand command = postService.searchPosts( searchStr, 0 );

        then( postRepository ).should().findByTextOrTitleIgnoreCaseContaining( anyString(), Matchers.any( Pageable.class ) );
        then( pageRequestBuilder ).should().buildPageRequest( anyInt(), Matchers.any( Sort.Direction.class ), anyString() );
        assertThat( command, is(notNullValue()));
        assertThat( command.getTotalElements(), is( 0L));
    }


    //HELPER METHODS
    private Post getParentPost1() {
        Post post = new Post();
        post.setParent( null );
        post.setText( POST1_TEXT );
        post.setId( POST1_ID );
        post.setCreated( LocalDateTime.of( 2017,01,01,10,10,10 ) );
        post.setCategory( getCategory1() );
        post.setUser( getUser1() );
        return post;
    }

    private Post getParentPost2() {
        Post post = new Post();
        post.setParent( null );
        post.setText( POST2_TEXT );
        post.setId( POST2_ID );
        post.setCreated( LocalDateTime.of( 2017,01,02,10,10,10 ) );
        post.setCategory( getCategory1() );
        post.setUser( getUser2() );
        return post;
    }

    private Post getParentPost3() {
        Post post = new Post();
        post.setParent( null );
        post.setText( POST3_TEXT );
        post.setId( POST3_ID );
        post.setCreated( LocalDateTime.of( 2017,01,03,10,10,10 ) );
        post.setCategory( getCategory1() );
        post.setUser( getUser1() );
        return post;
    }

    private Post getChildPost1() {
        Post post = new Post();
        post.setText( CHILD1_TEXT );
        post.setId( CHILD1_ID );
        post.setCategory( getCategory1() );
        post.setUser( getUser1() );
        post.setCreated( LocalDateTime.of( 2017,01,04,10,10,10 ) );
        return post;
    }

    private User getUser1() {
        User user = new User();
        user.setId( USER_ID );
        user.setUserName( USER_NAME );
        return user;
    }

    private User getUser2() {
        User user = new User();
        user.setId( USER2_ID );
        user.setUserName( USER2_NAME );
        return user;
    }

    private Category getCategory1() {
        Category category = new Category();
        category.setId( CAT1_ID );
        category.setName( CAT1_NAME );
        return category;
    }

    private Category getCategory2() {
        Category category = new Category();
        category.setId( CAT2_ID );
        category.setName( CAT2_NAME );
        return category;
    }

}