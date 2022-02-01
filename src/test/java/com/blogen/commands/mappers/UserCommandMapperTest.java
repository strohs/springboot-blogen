package com.blogen.commands.mappers;

import com.blogen.commands.UserCommand;
import com.blogen.commands.UserPrefsCommand;
import com.blogen.domain.User;
import com.blogen.domain.UserPrefs;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.CoreMatchers.is;


/**
 * Unit Test mapping to/from {@link com.blogen.domain.User} and {@link com.blogen.commands.UserCommand}
 *
 * @author Cliff
 */
public class UserCommandMapperTest {

    private static final Long USER_ID = 22L;
    private static final String USER_NAME = "jdoe";
    private static final String FIRST_NAME = "John";
    private static final String LAST_NAME = "Doe";
    private static final String EMAIL = "doey@super.com";
    private static final String PASSWORD = "secret";
    private static final int CURRENT_PAGE = 2;
    private static final int CURRENT_PAGE_DEFAULT = 1;
    private static final Long USER_PREF_ID = 223L;
    private static final String USER_PREF_AVATAR = "Avatar2.jpg";

    private UserCommandMapper userCommandMapper = UserCommandMapper.INSTANCE;

    @Test
    public void userToUserCommand() {
        UserPrefs up = buildUserPrefs();
        User user = buildUser();
        user.setUserPrefs( up );

        UserCommand uc = userCommandMapper.userToUserCommand( user );

        assertThat( uc.getId(), is( USER_ID) );
        assertThat( uc.getUserName(), is( USER_NAME));
        assertThat( uc.getFirstName(), is(FIRST_NAME) );
        assertThat( uc.getLastName(), is(LAST_NAME) );
        assertThat( uc.getEmail(), is(EMAIL) );
        assertThat( uc.getPassword(), is(PASSWORD) );
        assertThat( uc.getUserPrefs().getId(), is( USER_PREF_ID ));
        assertThat( uc.getUserPrefs().getAvatarImage(), is(USER_PREF_AVATAR ) );

    }

    @Test
    public void userCommandToUser() {
        UserPrefsCommand upc = buildUserPrefsCommand();
        UserCommand uc = buildUserCommand();
        uc.setUserPrefs( upc );

        User user = userCommandMapper.userCommandToUser( uc );

        assertThat( user.getId(), is(USER_ID) );
        assertThat( user.getUserName(), is(USER_NAME) );
        assertThat( user.getFirstName(), is(FIRST_NAME) );
        assertThat( user.getLastName(), is(LAST_NAME) );
        assertThat( user.getEmail(), is(EMAIL) );
        assertThat( user.getPassword(), is(PASSWORD) );
        assertThat( user.getUserPrefs().getId(), is(USER_PREF_ID) );
        assertThat( user.getUserPrefs().getAvatarImage(), is(USER_PREF_AVATAR) );
    }


    private UserPrefs buildUserPrefs() {
        UserPrefs up = new UserPrefs();
        up.setId( USER_PREF_ID );
        up.setAvatarImage( USER_PREF_AVATAR );
        return up;
    }

    private UserPrefsCommand buildUserPrefsCommand() {
        UserPrefsCommand upc = new UserPrefsCommand();
        upc.setId( USER_PREF_ID );
        upc.setAvatarImage( USER_PREF_AVATAR );
        return upc;
    }

    private User buildUser() {
        User user = new User();
        user.setId( USER_ID );
        user.setUserName( USER_NAME );
        user.setFirstName( FIRST_NAME );
        user.setLastName( LAST_NAME );
        user.setEmail( EMAIL );
        user.setPassword( PASSWORD );
        return user;
    }

    private UserCommand buildUserCommand() {
        UserCommand uc = new UserCommand();
        uc.setId( USER_ID );
        uc.setUserName( USER_NAME );
        uc.setFirstName( FIRST_NAME );
        uc.setLastName( LAST_NAME );
        uc.setEmail( EMAIL );
        uc.setPassword( PASSWORD );
        return uc;
    }
}