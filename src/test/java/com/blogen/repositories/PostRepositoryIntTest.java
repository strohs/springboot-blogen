package com.blogen.repositories;

import com.blogen.commands.PostCommand;
import com.blogen.commands.mappers.PostCommandMapper;
import com.blogen.domain.Category;
import com.blogen.domain.Post;
import com.blogen.domain.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * Integration test for making sure our CRUD operations on {@link com.blogen.domain.Post}(s) are working.
 * Mainly want to make sure child posts are being created and deleted correctly
 * 
 * Author: Cliff
 */
@SpringBootTest
public class PostRepositoryIntTest {
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

    @BeforeEach
    public void setUp() throws Exception {}

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

        Post parent = postRepository.findById( PARENT_POST_ID_WITH_CHILDREN ).get();
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

        Post parent = postRepository.findById( PARENT_POST_ID_NO_CHILDREN ).get();
        assertNotNull( parent );
        assertThat( parent.getChildren().size(), is( CHILD_COUNT) );

    }

    @Test
    public void should_returnTrue_when_ifPostIsAParentPost() {

        Post parent = postRepository.findById( PARENT_POST_ID_WITH_CHILDREN ).get();
        assertThat( parent.isParentPost(), is( true ));
    }

    @Test
    public void should_returnFalse_ifPostIsAChildPost() {
        Post child = postRepository.findById( CHILD1_POST_ID ).get();

        assertThat( child.isParentPost(), is(false) );
        assertThat( child.getParent(), is( notNullValue()));
    }

    @Test
    @Transactional
    public void should_deleteParentPostAndAllItsChildPosts() {
        Post parent = postRepository.findById( PARENT_POST_ID_WITH_CHILDREN ).get();

        postRepository.delete( parent );
        //re-select parent to make sure it is null
        Optional<Post> deletedParent = postRepository.findById( PARENT_POST_ID_WITH_CHILDREN );
        Optional<Post> child1 = postRepository.findById( CHILD1_POST_ID );
        Optional<Post> child2 = postRepository.findById( CHILD2_POST_ID );
        Optional<Post> child3 = postRepository.findById( CHILD3_POST_ID );
        Optional<Post> child4 = postRepository.findById( CHILD4_POST_ID );
        assertFalse( deletedParent.isPresent() );
        assertFalse( child1.isPresent() );
        assertFalse( child2.isPresent() );
        assertFalse( child3.isPresent() );
        assertFalse( child4.isPresent() );
    }

    @Test
    @Transactional
    public void should_notDeleteParentPost_when_deletingAChildPost() {
        Post parent = postRepository.findById( PARENT_POST_ID_WITH_CHILDREN ).get();
        Post child = postRepository.findById( CHILD1_POST_ID ).get();

        parent.removeChild( child );
        postRepository.delete( child );

        assertNotNull( parent );
        assertThat( parent.getChildren().size(), is(3) );
    }

    @Test
    @Transactional
    public void should_notDeleteParentPost_when_deletingAllChildPosts() {
        Post parent = postRepository.findById( PARENT_POST_ID_TWO_CHILDREN ).get();
        Post child1 = postRepository.findById( CHILD5_POST_ID ).get();
        Post child2 = postRepository.findById( CHILD6_POST_ID ).get();

        parent.removeChild( child1 );
        parent.removeChild( child2 );
        postRepository.delete( child1 );
        postRepository.delete( child2 );
        postRepository.saveAndFlush( parent );

        parent = postRepository.findById( PARENT_POST_ID_TWO_CHILDREN ).get();
        assertNotNull( parent );
        assertThat( parent.getChildren().size(), is(0) );
    }

    @Test
    @Transactional
    public void should_succeed_when_addingOneChildPostToAParentPostWithChildren() {
        Category cat = categoryRepository.findById( 3L ).get();
        User user = userRepository.findById( 1L ).get();
        Post child = buildPost( user, cat, "child text","http://foo.com" );

        Post parent = postRepository.findById( PARENT_POST_ID_WITH_CHILDREN ).get();
        parent.addChild( child );
        postRepository.saveAndFlush( parent );

        parent = postRepository.findById( PARENT_POST_ID_WITH_CHILDREN ).get();
        assertThat( parent.getChildren().size(), is(5) );
    }

    @Test
    @Transactional
    public void should_succeed_when_addingChildPostToParentPostWithoutChildren() {
        Category cat = categoryRepository.findById( 3L ).get();
        User user = userRepository.findById( 1L ).get();
        Post child = buildPost( user, cat, "child text","http://foo.com" );

        Post parent = postRepository.findById( PARENT_POST_ID_NO_CHILDREN).get();
        parent.addChild( child );
        postRepository.saveAndFlush( parent );

        parent = postRepository.findById( PARENT_POST_ID_NO_CHILDREN ).get();
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
        User user = userRepository.findById( detachedPost.getUser().getId() ).get();
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
        User user = userRepository.findById( detachedPost.getUser().getId() ).get();
        detachedPost.setCategory( cat );
        detachedPost.setUser( user );
        //get Parent Post
        Post parent = postRepository.findById( postCommand.getParentId() ).get();
        parent.addChild( detachedPost );
        postRepository.saveAndFlush( parent );

        Post savedPost = postRepository.findById( 17L ).get();

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