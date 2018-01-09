package com.blogen.api.v1.mapper;

import com.blogen.api.v1.model.PostDTO;
import com.blogen.domain.Category;
import com.blogen.domain.Post;
import com.blogen.domain.User;
import org.junit.Before;
import org.junit.Test;

import java.time.Instant;
import java.time.LocalDateTime;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.*;

/**
 * Unit Test for MapStruct mapping between Post and PostDTO
 * @author Cliff
 */
public class PostMapperTest {

    private static final Long    CAT1_ID = 10L;
    private static final Long    CAT2_ID = 12L;
    private static final String  CAT1_NAME = "Health & Wellness";
    private static final String  CAT2_NAME = "Business";

    private static final Long    USER1_ID = 20L;
    private static final String  USER1_USERNAME = "jdoe";
    private static final Long    USER2_ID = 22L;
    private static final String  USER2_USERNAME = "wallace";

    private static final Long     PARENT_POST_ID = 1L;
    private static final LocalDateTime PARENT_POST_CREATED = LocalDateTime.of( 2017,1,1,10,10,10 );
    private static final String   PARENT_POST_CREATED_FORMAT = "Sun Jan 01, 2017 10:10 AM";
    private static final String   PARENT_POST_TEXT = "sample text for blogen";
    private static final String   PARENT_POST_IMAGE_URL = "http://pexels.com/technics/400/200/1";

    private static final Long    CHILD1_POST_ID   = 5L;
    private static final String  CHILD1_POST_TEXT = "sample child text";
    private static final String  CHILD1_POST_IMAGE_URL = null;
    private static final LocalDateTime CHILD1_POST_CREATED = LocalDateTime.of( 2017,1,1,10,15,15 );
    private static final String  CHILD1_POST_CREATED_FORMAT = "Sun Jan 01, 2017 10:15 AM";

    private PostMapper postMapper = PostMapper.INSTANCE;

    private Post parent;

    @Before
    public void setUp() throws Exception {
        Category cat1 = new Category();
        cat1.setId( CAT1_ID );
        cat1.setName( CAT1_NAME );

        Category cat2 = new Category();
        cat2.setId( CAT2_ID );
        cat2.setName( CAT2_NAME );

        User user1 = new User();
        user1.setId( USER1_ID );
        user1.setUserName( USER1_USERNAME );

        User user2 = new User();
        user2.setId( USER2_ID );
        user2.setUserName( USER2_USERNAME );

        parent = new Post();
        parent.setId( PARENT_POST_ID );
        parent.setCategory( cat1 );
        parent.setUser( user1 );
        parent.setParent( null );
        parent.setText( PARENT_POST_TEXT );
        parent.setImageUrl( PARENT_POST_IMAGE_URL );
        parent.setCreated( PARENT_POST_CREATED );

        Post child1 = new Post();
        child1.setId( CHILD1_POST_ID );
        child1.setText( CHILD1_POST_TEXT );
        child1.setImageUrl( CHILD1_POST_IMAGE_URL );
        child1.setCreated( CHILD1_POST_CREATED );
        child1.setCategory( cat1 );
        child1.setUser( user2 );

        parent.addChild( child1 );

    }

    @Test
    public void postToPostDto() {

        //when
        PostDTO postDTO = postMapper.postToPostDto( parent );
        System.out.println( postDTO );
        System.out.println( postDTO.isParentPost() );

        //then
        assertNotNull( postDTO );
        assertThat( postDTO.getId(), is( PARENT_POST_ID ) );
        assertThat( postDTO.getCategory().getId(), is(CAT1_ID) );
        assertThat( postDTO.getCategory().getName(), is(CAT1_NAME) );
        assertThat( postDTO.getText(), is( PARENT_POST_TEXT) );
        assertThat( postDTO.getImageUrl(), is( PARENT_POST_IMAGE_URL) );
        assertThat( postDTO.getCreated(), is( PARENT_POST_CREATED_FORMAT) );
        assertThat( postDTO.getUserId(), is( USER1_ID) );
        assertThat( postDTO.getUserName(), is( USER1_USERNAME) );
        assertThat( postDTO.isParentPost(), is( true ) );
        assertThat( postDTO.getChildren().size(), is( 1 ) );

        //test child posts
        PostDTO child1 = postDTO.getChildren().get( 0 );
        assertThat( child1.isParentPost(), is( false ) );
        assertThat( child1.getId(), is(CHILD1_POST_ID) );
        assertThat( child1.getParentId(), is( PARENT_POST_ID ) );
        assertThat( child1.getCreated(), is(CHILD1_POST_CREATED_FORMAT) );
        assertThat( child1.getText(), is( CHILD1_POST_TEXT) );
        assertThat( child1.getImageUrl(), is( nullValue()) );
        assertThat( child1.getUserId(), is( USER2_ID ) );
        assertThat( child1.getUserName(), is( USER2_USERNAME) );
        assertThat( child1.getCategory().getId(), is( CAT1_ID) );
        assertThat( child1.getCategory().getName(), is(CAT1_NAME) );
        assertThat( child1.getChildren().size(), is(0) );

    }


}