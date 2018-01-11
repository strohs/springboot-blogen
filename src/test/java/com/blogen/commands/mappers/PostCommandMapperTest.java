package com.blogen.commands.mappers;

import com.blogen.commands.CategoryCommand;
import com.blogen.commands.PostCommand;
import com.blogen.commands.UserCommand;
import com.blogen.commands.UserPrefsCommand;
import com.blogen.domain.Category;
import com.blogen.domain.Post;
import com.blogen.domain.User;
import com.blogen.domain.UserPrefs;
import org.junit.Ignore;
import org.junit.Test;

import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.number.OrderingComparison.lessThan;
import static org.junit.Assert.*;

/**
 * Unit Test mappings from {@link com.blogen.domain.Post} to {@link com.blogen.commands.PostCommand}
 *
 * @author Cliff
 */
public class PostCommandMapperTest {

    private static final int CURRENT_PAGE = 2;
    private static final int DEFAULT_PAGE = 1;
    private static final Long USER_PREF_ID = 23L;
    private static final String USER_PREF_AVATAR = "Avatar3.jpg";

    private static final Long CAT_ID = 22L;
    private static final String CAT_NAME = "My Category";

    private static final Long USER_ID = 1L;
    private static final String USER_NAME = "userName1";
    private static final String USER_FIRST_NAME = "firstname";
    private static final String USER_LAST_NAME = "lastname";
    private static final String USER_EMAIL = "email@hotmail.org";
    private static final String USER_PASSWORD = "sEcReT";

    private static final Long POST_ID = 10L;
    private static final Long POST_PARENT_ID = 5L;
    private static final String POST_TITLE = "Title1";
    private static final String POST_TEXT = "Some text";
    private static final String POST_IMAGE_URL = "http://foo.com";
    private static final Long CHILD_ID = 11L;
    private static final Long CHILD_PARENT_ID = 10L;
    private static final String CHILD_TITLE = "Child Title1";
    private static final String CHILD_TEXT = "Child Some text";
    private static final String CHILD_IMAGE_URL = "http://child.foo.com";

    private PostCommandMapper postCommandMapper = PostCommandMapper.INSTANCE;


    @Test
    public void postToPostCommand() {
        Category cat = buildCategory( CAT_ID, CAT_NAME );
        UserPrefs up = buildUserPrefs( USER_PREF_ID, USER_PREF_AVATAR );
        User user = buildUser( USER_ID, USER_NAME, USER_FIRST_NAME, USER_LAST_NAME, USER_EMAIL,USER_PASSWORD );
        user.setUserPrefs( up );
        Post parent = buildPost( POST_ID, POST_TITLE, POST_TEXT, POST_IMAGE_URL, cat, user, null);
        Post child = buildPost( CHILD_ID, CHILD_TITLE, CHILD_TEXT, CHILD_IMAGE_URL, cat, user, parent );
        parent.addChild( child );

        PostCommand postCommand = postCommandMapper.postToPostCommand( parent );

        assertThat( postCommand.getId(), is(POST_ID) );
        assertThat( postCommand.getCategoryId(), is( CAT_ID) );
        assertThat( postCommand.getUserId(), is( USER_ID ));
        assertThat( postCommand.getUserName(), is(USER_NAME ));
        assertThat( postCommand.getImageUrl(), is(POST_IMAGE_URL));
        assertThat( postCommand.getParentId(), is( nullValue() ));
        assertThat( postCommand.getText(), is(POST_TEXT));
        assertThat( postCommand.getTitle(), is(POST_TITLE));
        assertThat( postCommand.getChildren().size(), is(1) );
        assertThat( postCommand.getChildren().get( 0 ).getId(), is(CHILD_ID));
        assertThat( postCommand.getChildren().get( 0 ).getParentId(), is(POST_ID));
        assertThat( postCommand.getChildren().get( 0 ).getTitle(), is(CHILD_TITLE));
        assertThat( postCommand.getChildren().get( 0 ).getText(), is(CHILD_TEXT));
        assertThat( postCommand.getChildren().get( 0 ).getCategoryId(), is(CAT_ID));
        assertThat( postCommand.getChildren().get( 0 ).getUserId(), is(USER_ID));
        assertThat( postCommand.getChildren().get( 0 ).getUserName(), is(USER_NAME) );
    }

    @Test
    public void parentPostCommandToPost_shouldMapCorrectly() {
        CategoryCommand cc = buildCategoryCommand( CAT_ID, CAT_NAME );
        PostCommand pc = buildPostCommand( POST_ID, POST_TITLE, POST_TEXT, POST_IMAGE_URL, CAT_ID, USER_ID, USER_NAME, null );

        Post post = postCommandMapper.postCommandToPost( pc );

        assertThat( post.getId(), is(POST_ID));
        assertThat( post.getParent(), is( nullValue()) );
        //Post.User will be null
        assertThat( post.getText(), is(POST_TEXT) );
        assertThat( post.getTitle(), is(POST_TITLE) );
        assertThat( post.getImageUrl(), is(POST_IMAGE_URL) );
        assertThat( post.getCategory().getId(), is( CAT_ID) );
        assertThat( post.getUser().getUserName(), is(USER_NAME) );
        assertThat( post.getUser().getId(), is(USER_ID) );
        assertThat( post.getChildren().size(), is(0));
    }

    @Test
    @Ignore
    //child posts and parent posts map the same way...for now
    public void childPostCommandToPost_shouldMapCorrectly() {
        CategoryCommand cc = buildCategoryCommand( CAT_ID, CAT_NAME );
        PostCommand childPc = buildPostCommand( POST_ID, POST_TITLE, POST_TEXT, POST_IMAGE_URL, CAT_ID, USER_ID, USER_NAME, POST_PARENT_ID );

        Post child = postCommandMapper.postCommandToPost( childPc );
        assertThat( child.getId(), is(POST_ID));
        assertThat( child.getParent(), is( nullValue() ) );
        assertThat( child.getImageUrl(), is(POST_IMAGE_URL) );
        assertThat( child.getCategory().getId(), is(CAT_ID));
        assertThat( child.getUser().getId(), is(USER_ID));
        assertThat( child.getUser().getUserName(), is(USER_NAME) );
        assertThat( child.getTitle(), is(POST_TITLE) );
        assertThat( child.getText(), is(POST_TEXT) );
    }


    private Post buildPost( Long id, String title, String text, String image, Category cat, User user, Post parent ) {
        Post post = new Post();
        post.setId( id );
        post.setTitle( title );
        post.setText( text );
        post.setImageUrl( image );
        post.setCategory( cat );
        post.setUser( user );
        post.setParent( parent );
        return post;
    }

    private PostCommand buildPostCommand( Long id, String title, String text, String image, Long catId, Long userId, String userName, Long parentId ) {
        PostCommand pc = new PostCommand();
        pc.setId( id );
        pc.setTitle( title );
        pc.setText( text );
        pc.setImageUrl( image );
        pc.setCategoryId( catId );
        pc.setUserId( userId );
        pc.setUserName( userName );
        pc.setParentId( parentId );
        return pc;
    }

    private Category buildCategory( Long id, String name ) {
        Category cat = new Category();
        cat.setId( id );
        cat.setName( name );
        return cat;
    }

    private CategoryCommand buildCategoryCommand( Long id, String name ) {
        CategoryCommand cc = new CategoryCommand();
        cc.setId( id );
        cc.setName( name );
        return cc;
    }

    private UserPrefs buildUserPrefs( Long id, String avatar ) {
        UserPrefs up = new UserPrefs();
        up.setId( id );
        up.setAvatarImage( avatar );
        return up;
    }

    private UserPrefsCommand buildUserPrefsCommand( Long id, String avatar ) {
        UserPrefsCommand upc = new UserPrefsCommand();
        upc.setId( id );
        upc.setAvatarImage( avatar );
        return upc;
    }

    private User buildUser( Long id, String username, String firstname, String lastname, String email, String password ) {
        User user = new User();
        user.setId( id );
        user.setUserName( username );
        user.setFirstName( firstname );
        user.setLastName( lastname );
        user.setEmail( email );
        user.setPassword( password );
        return user;
    }

    private UserCommand buildUserCommand( Long id, String username, String firstname, String lastname, String email, String password ) {
        UserCommand uc = new UserCommand();
        uc.setId( id );
        uc.setUserName( username );
        uc.setFirstName( firstname );
        uc.setLastName( lastname );
        uc.setEmail( email );
        uc.setPassword( password );;
        return uc;
    }
}