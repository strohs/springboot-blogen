package com.blogen.repositories;

import com.blogen.bootstrap.BlogenBootstrap;
import com.blogen.domain.Category;
import com.blogen.domain.Post;
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
 * Integration test for making sure our CRUD operations on {@link com.blogen.domain.Post}(s) are working
 * 
 * Author: Cliff
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class PostRepositoryIT {

    private static final Long PARENT_POST_ID_WITH_CHILDREN = 1L;
    private static final Long CHILD_POST_ID = 2L;
    private static final Long PARENT_POST_ID_NO_CHILDREN = 6L;

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
        Post child = postRepository.findOne( CHILD_POST_ID );

        assertThat( child.isParentPost(), is(false) );
        assertThat( child.getParent(), is( notNullValue()));
    }
}