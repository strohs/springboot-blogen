package com.blogen.repositories;

import com.blogen.commands.PostCommand;
import com.blogen.commands.mappers.PostCommandMapper;
import com.blogen.domain.Category;
import com.blogen.domain.Post;
import com.blogen.domain.User;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.*;

/**
 * Integration test for making sure our CRUD operations on {@link com.blogen.domain.Post}(s) are working.
 * Mainly want to make sure child posts are being created and deleted correctly
 * 
 * Author: Cliff
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class PostRepositoryIT {
    private static final int PARENT_POST_TOTAL = 16;

    private static final Long PARENT_POST_ID_WITH_CHILDREN = 1L;
    private static final Long CHILD1_POST_ID = 2L;
    private static final Long CHILD2_POST_ID = 3L;
    private static final Long CHILD3_POST_ID = 4L;
    private static final Long CHILD4_POST_ID = 5L;

    private static final Long PARENT_POST_ID_NO_CHILDREN = 6L;

    private static final Long PARENT_POST_ID_TWO_CHILDREN = 14L;
    private static final Long CHILD5_POST_ID = 15L;
    private static final Long CHILD6_POST_ID = 16L;

    private static final Long USER_WITH_THREE_PARENT_POSTS = 3L;

    @Autowired
    UserRepository userRepository;

    @Autowired
    UserPrefsRepository userPrefsRepository;

    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    PostRepository postRepository;

    PostCommandMapper postCommandMapper = PostCommandMapper.INSTANCE;

    @Before
    public void setUp() throws Exception {

    }

    @Test
    public void should_findAllParentPosts_when_findAllParentPosts() {

        List<Post> parentPosts = postRepository.findAllByParentNull();

        assertNotNull( parentPosts );
        assertThat( parentPosts.size(), is( PARENT_POST_TOTAL ) );
    }

    @Test
    @Transactional
    public void should_findFourChildPosts() {

        int CHILD_COUNT = 4;

        Post parent = postRepository.findOne( PARENT_POST_ID_WITH_CHILDREN );
        assertNotNull( parent );
        assertThat( parent.getChildren().size(), is( CHILD_COUNT) );
        assertThat( parent.getChildren().get( 0 ).getParent().getId(), is( PARENT_POST_ID_WITH_CHILDREN ));
        assertThat( parent.getChildren().get( 1 ).getParent().getId(), is( PARENT_POST_ID_WITH_CHILDREN ));
        assertThat( parent.getChildren().get( 2 ).getParent().getId(), is( PARENT_POST_ID_WITH_CHILDREN ));
        assertThat( parent.getChildren().get( 3 ).getParent().getId(), is( PARENT_POST_ID_WITH_CHILDREN ));
    }

    @Test
    @Transactional
    public void should_findParentPostWithNoChildPosts() {
        int CHILD_COUNT = 0;

        Post parent = postRepository.findOne( PARENT_POST_ID_NO_CHILDREN );
        assertNotNull( parent );
        assertThat( parent.getChildren().size(), is( CHILD_COUNT) );

    }

    @Test
    public void should_returnTrue_when_ifPostIsAParentPost() {

        Post parent = postRepository.findOne( PARENT_POST_ID_WITH_CHILDREN );
        assertThat( parent.isParentPost(), is( true ));
    }

    @Test
    public void should_returnFalse_ifPostIsAChildPost() {
        Post child = postRepository.findOne( CHILD1_POST_ID );

        assertThat( child.isParentPost(), is(false) );
        assertThat( child.getParent(), is( notNullValue()));
    }

    @Test
    @Transactional
    public void should_deleteParentPostAndAllItsChildPosts() {
        Post parent = postRepository.findOne( PARENT_POST_ID_WITH_CHILDREN );

        postRepository.delete( parent );
        //re-select parent to make sure it is null
        parent = postRepository.findOne( PARENT_POST_ID_WITH_CHILDREN );
        Post child1 = postRepository.findOne( CHILD1_POST_ID );
        Post child2 = postRepository.findOne( CHILD2_POST_ID );
        Post child3 = postRepository.findOne( CHILD3_POST_ID );
        Post child4 = postRepository.findOne( CHILD4_POST_ID );
        assertNull( parent );
        assertNull( child1 );
        assertNull( child2 );
        assertNull( child3 );
        assertNull( child4 );
    }

    @Test
    @Transactional
    public void should_notDeleteParentPost_when_deletingAChildPost() {
        Post parent = postRepository.findOne( PARENT_POST_ID_WITH_CHILDREN );
        Post child = postRepository.findOne( CHILD1_POST_ID );

        parent.removeChild( child );
        postRepository.delete( child );

        assertNotNull( parent );
        assertThat( parent.getChildren().size(), is(3) );
    }

    @Test
    @Transactional
    public void should_notDeleteParentPost_when_deletingAllChildPosts() {
        Post parent = postRepository.findOne( PARENT_POST_ID_TWO_CHILDREN );
        Post child1 = postRepository.findOne( CHILD5_POST_ID );
        Post child2 = postRepository.findOne( CHILD6_POST_ID );

        parent.removeChild( child1 );
        parent.removeChild( child2 );
        postRepository.delete( child1 );
        postRepository.delete( child2 );
        postRepository.saveAndFlush( parent );

        parent = postRepository.findOne( PARENT_POST_ID_TWO_CHILDREN );
        assertNotNull( parent );
        assertThat( parent.getChildren().size(), is(0) );
    }

    @Test
    @Transactional
    public void should_succeed_when_addingOneChildPostToAParentPostWithChildren() {
        Category cat = categoryRepository.findOne( 3L );
        User user = userRepository.findOne( 1L );
        Post child = buildPost( user, cat, "child text","http://foo.com" );

        Post parent = postRepository.findOne( PARENT_POST_ID_WITH_CHILDREN );
        parent.addChild( child );
        postRepository.saveAndFlush( parent );

        parent = postRepository.findOne( PARENT_POST_ID_WITH_CHILDREN );
        assertThat( parent.getChildren().size(), is(5) );
    }

    @Test
    @Transactional
    public void should_succeed_when_addingChildPostToParentPostWithoutChildren() {
        Category cat = categoryRepository.findOne( 3L );
        User user = userRepository.findOne( 1L );
        Post child = buildPost( user, cat, "child text","http://foo.com" );

        Post parent = postRepository.findOne( PARENT_POST_ID_NO_CHILDREN);
        parent.addChild( child );
        postRepository.saveAndFlush( parent );

        parent = postRepository.findOne( PARENT_POST_ID_NO_CHILDREN );
        assertThat( parent.getChildren().size(), is(1) );
    }

    @Test
    @Transactional
    public void should_findThreeParentPosts_when_findAllParentPostsByUserId() {
        List<Post> posts = postRepository.findAllByUser_IdAndParentNullOrderByCreatedDesc( USER_WITH_THREE_PARENT_POSTS );

        assertNotNull( posts );
        assertThat( posts.size(), is( 3 ) );
        assertThat( posts.get( 0 ).getUser().getId(), is( USER_WITH_THREE_PARENT_POSTS) );
        posts.forEach( System.out::println );
    }

    @Test
    @Transactional
    public void saveNewParentPost() {
        PostCommand postCommand = new PostCommand();
        postCommand.setParentId( null );
        postCommand.setUserId( 2L );
        postCommand.setUserName( "johndoe" );
        postCommand.setCategoryName( "Business" );
        postCommand.setImageUrl( "http://pexels.com" );
        postCommand.setText( "new post text" );
        postCommand.setTitle( "New Title" );

        Post detachedPost = postCommandMapper.postCommandToPost( postCommand );
        Category cat = categoryRepository.findByName( detachedPost.getCategory().getName() );
        User user = userRepository.findOne( detachedPost.getUser().getId() );
        detachedPost.setCategory( cat );
        detachedPost.setUser( user );
        Post savedPost = postRepository.save( detachedPost );
        System.out.println("New post id:" + savedPost.getId() );

        assertNotNull( savedPost );
        assertThat( savedPost.getId(), is( notNullValue()));
        assertThat( savedPost.getParent(), is(nullValue()));
        assertThat( savedPost.getText(), is("new post text") );
        assertThat( savedPost.getTitle(), is("New Title"));
        assertThat( savedPost.getImageUrl(), is("http://pexels.com"));
        assertThat( savedPost.getUser().getId(), is(2L));
        assertThat( savedPost.getUser().getUserName(), is("johndoe"));
        assertThat( savedPost.getCategory().getName(), is("Business"));
    }

    @Test
    @Transactional
    public void saveChildPost() {
        Long userId = 5L;
        Long parentId = 17L;
        Long categoryId = 3L;
        String imageUrl = "http://lorempixel.com";
        String postText = "child post text";
        String postTitle = "Child Title";
        String catName = "Business";
        PostCommand postCommand = new PostCommand();
        postCommand.setParentId( parentId );
        postCommand.setUserId( userId );
        postCommand.setCategoryName( catName );
        postCommand.setImageUrl( imageUrl );
        postCommand.setText( postText );
        postCommand.setTitle( postTitle );

        Post detachedPost = postCommandMapper.postCommandToPost( postCommand );
        assertThat( detachedPost.getId(), is( nullValue()));
        Category cat = categoryRepository.findByName( detachedPost.getCategory().getName() );
        User user = userRepository.findOne( detachedPost.getUser().getId() );
        detachedPost.setCategory( cat );
        detachedPost.setUser( user );
        //get Parent Post
        Post parent = postRepository.findOne( postCommand.getParentId() );
        parent.addChild( detachedPost );
        postRepository.saveAndFlush( parent );

        Post savedPost = postRepository.findOne( 17L );

        assertThat( savedPost.getChildren().size(), is(1));
        assertThat( savedPost.getChildren().get( 0 ).getId(), is( notNullValue()) );
        assertThat( savedPost.getChildren().get(0).getUser().getId(), is(userId) );
        assertThat( savedPost.getChildren().get(0).getCategory().getName(), is(catName) );
        assertThat( savedPost.getChildren().get(0).getParent().getId(), is(parentId) );
        assertThat( savedPost.getChildren().get(0).getText(), is(postText) );

    }


    //helper for creating a post
    private Post buildPost( User user, Category cat, String text, String image ) {
        Post child = new Post();
        child.setCategory( cat );
        child.setText( text );
        child.setImageUrl( image );
        child.setUser( user );
        return child;
    }

    
}