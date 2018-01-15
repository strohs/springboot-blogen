package com.blogen.services;

import com.blogen.commands.PostCommand;
import com.blogen.commands.mappers.CategoryCommandMapper;
import com.blogen.commands.mappers.PostCommandMapper;
import com.blogen.domain.Category;
import com.blogen.domain.Post;
import com.blogen.domain.User;
import com.blogen.repositories.CategoryRepository;
import com.blogen.repositories.PostRepository;
import com.blogen.repositories.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Matchers.anyString;

/**
 * Unit Tests for {@link PostServiceImpl}
 *
 * @author Cliff
 */
public class PostServiceImplTest {

    private static final Long   CAT1_ID   = 3L;
    private static final String CAT1_NAME = "Business";
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

    private PostCommandMapper postCommandMapper = PostCommandMapper.INSTANCE;
    private CategoryCommandMapper categoryCommandMapper = CategoryCommandMapper.INSTANCE;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks( this );
        postService = new PostServiceImpl( postRepository, userRepository, categoryRepository, postCommandMapper,categoryCommandMapper );
    }

    @Test
    public void getAllPostsByUser_shouldReturnTwoParentPostsMadeByUser1() {
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
    public void getAllPosts_shouldReturnAllParentPosts() {
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
    public void getPost() {
        Post p1 = getParentPost1();

        given( postRepository.findOne( anyLong() )).willReturn( p1 );

        PostCommand pc = postService.getPost( POST1_ID );

        then( postRepository ).should().findOne( anyLong() );
        assertThat( pc, is(notNullValue()));
        assertThat( pc.getId(), is( POST1_ID ));
    }

    //TODO test for when ID is not found

    @Test
    public void deleteParentPost_shouldDeleteTheParent() {
        Post p1 = getParentPost1();
        PostCommand pc = postCommandMapper.postToPostCommand( p1 );

        postService.deletePost( pc );

        then( postRepository ).should().delete( anyLong() );
    }

    @Test
    public void deleteChildPost_shouldDeleteChild() {
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
    public void saveNewParentPostCommand_ShouldSaveNewPost() {
        Post post = getParentPost1();
        Category cat = getCategory();
        User user1 = getUser1();
        PostCommand pc = postCommandMapper.postToPostCommand( post );
        pc.setId( null );

        given( categoryRepository.findByName( anyString() )).willReturn( cat );
        given( userRepository.findOne( anyLong() )).willReturn( user1 );
        given( postRepository.saveAndFlush( Matchers.any( Post.class ) )).willReturn( post );

        postService.savePostCommand( pc );

        then( postRepository ).should().saveAndFlush( Matchers.any( Post.class) );
        then( categoryRepository).should().findByName( anyString() );
        then( userRepository ).should().findOne( anyLong() );
    }

    @Test
    public void saveNewChildPostCommand_ShouldSaveNewChild() {
        Post post = getParentPost1();
        Post child = getChildPost1();
        Category cat = getCategory();
        User user1 = getUser1();
        PostCommand pc = postCommandMapper.postToPostCommand( child );
        pc.setParentId( post.getId() );

        given( categoryRepository.findByName( anyString() )).willReturn( cat );
        given( userRepository.findOne( anyLong() )).willReturn( user1 );
        given( postRepository.findOne( anyLong() )).willReturn( post );
        given( postRepository.saveAndFlush( Matchers.any( Post.class ) )).willReturn( post );

        PostCommand savedCommand = postService.savePostCommand( pc );

        then( postRepository ).should().saveAndFlush( Matchers.any( Post.class) );
        then( categoryRepository).should().findByName( anyString() );
        then( userRepository ).should().findOne( anyLong() );
        then( postRepository ).should().findOne( anyLong() );
        assertThat( savedCommand.getId(), is(POST1_ID) );
        assertThat( savedCommand.getChildren().size(), is(1));
        assertThat( savedCommand.getChildren().get( 0 ).getId(), is(CHILD1_ID));
    }

    @Test
    public void updatePostCommand_shouldUpdatePostWithNewText() {
        Post existingPost = getParentPost1();
        Category cat = getCategory();
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




    //HELPER METHODS

    private Post getParentPost1() {
        Post post = new Post();
        post.setParent( null );
        post.setText( POST1_TEXT );
        post.setId( POST1_ID );
        post.setCreated( LocalDateTime.of( 2017,01,01,10,10,10 ) );
        post.setCategory( getCategory() );
        post.setUser( getUser1() );
        return post;
    }

    private Post getParentPost2() {
        Post post = new Post();
        post.setParent( null );
        post.setText( POST2_TEXT );
        post.setId( POST2_ID );
        post.setCreated( LocalDateTime.of( 2017,01,02,10,10,10 ) );
        post.setCategory( getCategory() );
        post.setUser( getUser2() );
        return post;
    }

    private Post getParentPost3() {
        Post post = new Post();
        post.setParent( null );
        post.setText( POST3_TEXT );
        post.setId( POST3_ID );
        post.setCreated( LocalDateTime.of( 2017,01,03,10,10,10 ) );
        post.setCategory( getCategory() );
        post.setUser( getUser1() );
        return post;
    }

    private Post getChildPost1() {
        Post post = new Post();
        post.setText( CHILD1_TEXT );
        post.setId( CHILD1_ID );
        post.setCategory( getCategory() );
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

    private Category getCategory() {
        Category category = new Category();
        category.setId( CAT1_ID );
        category.setName( CAT1_NAME );
        return category;
    }

}