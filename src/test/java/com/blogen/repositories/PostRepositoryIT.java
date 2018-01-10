package com.blogen.repositories;

import com.blogen.bootstrap.BlogenBootstrap;
import com.blogen.domain.Category;
import com.blogen.domain.Post;
import com.blogen.domain.User;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.*;

/**
 * Integration test for making sure our CRUD operations on {@link com.blogen.domain.Post}(s) are working.
 * Want to make sure child posts are being created and deleted correctly
 * 
 * Author: Cliff
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class PostRepositoryIT {

    private static final Long PARENT_POST_ID_WITH_CHILDREN = 1L;
    private static final Long CHILD1_POST_ID = 2L;
    private static final Long CHILD2_POST_ID = 3L;
    private static final Long CHILD3_POST_ID = 4L;
    private static final Long CHILD4_POST_ID = 5L;

    private static final Long PARENT_POST_ID_NO_CHILDREN = 6L;

    private static final Long PARENT_POST_ID_TWO_CHILDREN = 14L;
    private static final Long CHILD5_POST_ID = 15L;
    private static final Long CHILD6_POST_ID = 16L;

    @Autowired
    UserRepository userRepository;

    @Autowired
    UserPrefsRepository userPrefsRepository;

    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    PostRepository postRepository;

    @Before
    public void setUp() throws Exception {
//        System.out.println("Bootstrapping data...");
//        //bootstrap test data using our Bootstrap class
//        BlogenBootstrap bootstrap = new BlogenBootstrap( categoryRepository, userRepository, userPrefsRepository, postRepository );
//        bootstrap.initData();
//        System.out.println("Bootstrapping done...");
    }

    @Test
    public void findAllParentPosts() {
        int PARENT_POST_TOTAL = 6;
        List<Post> parentPosts = postRepository.findAllByParentNull();

        assertNotNull( parentPosts );
        assertThat( parentPosts.size(), is( PARENT_POST_TOTAL ) );
    }

    @Test
    @Transactional
    public void parentPostShouldHaveFourChildPosts() {
        //this post should have four children
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
    public void parentPostShouldHaveNoChildPosts() {
        int CHILD_COUNT = 0;

        Post parent = postRepository.findOne( PARENT_POST_ID_NO_CHILDREN );
        assertNotNull( parent );
        assertThat( parent.getChildren().size(), is( CHILD_COUNT) );

    }

    @Test
    public void isParentPostShouldReturnTrue() {

        Post parent = postRepository.findOne( PARENT_POST_ID_WITH_CHILDREN );
        assertThat( parent.isParentPost(), is( true ));
    }

    @Test
    public void isParentPostShouldReturnFalse() {
        Post child = postRepository.findOne( CHILD1_POST_ID );

        assertThat( child.isParentPost(), is(false) );
        assertThat( child.getParent(), is( notNullValue()));
    }

    @Test
    @Transactional
    public void deletingParentPostShouldDeleteAllChildren() {
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
    public void deletingChildPostShouldNotDeleteParent() {
        Post parent = postRepository.findOne( PARENT_POST_ID_WITH_CHILDREN );
        Post child = postRepository.findOne( CHILD1_POST_ID );

        parent.removeChild( child );
        postRepository.delete( child );

        assertNotNull( parent );
        assertThat( parent.getChildren().size(), is(3) );
    }

    @Test
    @Transactional
    public void deletingAllChildPostShouldNotDeleteParent() {
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
    public void addingAChildPostToAParentPostWithChildrenShouldSucceed() {
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
    public void addingChildPostToParentPostWithoutChildrenShouldSucceed() {
        Category cat = categoryRepository.findOne( 3L );
        User user = userRepository.findOne( 1L );
        Post child = buildPost( user, cat, "child text","http://foo.com" );

        Post parent = postRepository.findOne( PARENT_POST_ID_NO_CHILDREN);
        parent.addChild( child );
        postRepository.saveAndFlush( parent );

        parent = postRepository.findOne( PARENT_POST_ID_NO_CHILDREN );
        assertThat( parent.getChildren().size(), is(1) );
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