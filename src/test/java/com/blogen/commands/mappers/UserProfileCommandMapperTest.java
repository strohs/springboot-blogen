package com.blogen.commands.mappers;

import com.blogen.commands.UserProfileCommand;
import com.blogen.domain.User;
import com.blogen.domain.UserPrefs;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

/**
 * @author Cliff
 */
public class UserProfileCommandMapperTest {

    private static final Long USER_ID = 22L;
    private static final String USER_NAME = "jdoe";
    private static final String FIRST_NAME = "John";
    private static final String LAST_NAME = "Doe";
    private static final String EMAIL = "doey@super.com";
    private static final String PASSWORD = "secret";
    private static final String CONFIRM_PASSWORD = "secret";
    private static final Long USER_PREF_ID = 223L;
    private static final String USER_PREF_AVATAR = "Avatar2.jpg";

    private UserProfileCommandMapper userProfileCommandMapper = UserProfileCommandMapper.INSTANCE;

    @Test
    public void userToUserProfileCommand() {
        UserPrefs up = buildUserPrefs();
        User user = buildUser();
        user.setUserPrefs( up );

        UserProfileCommand command = userProfileCommandMapper.userToUserProfileCommand( user );

        assertThat( command.getId(), is( USER_ID) );
        assertThat( command.getFirstName(), is(FIRST_NAME) );
        assertThat( command.getLastName(), is(LAST_NAME) );
        assertThat( command.getPassword(), is(PASSWORD) );
        assertThat( command.getEmail(), is(EMAIL) );
        assertThat( command.getAvatarImage(), is(USER_PREF_AVATAR ) );

    }

    @Test
    public void userProfileCommandToUser() {
        UserProfileCommand command = buildUserProfileCommand();

        User user = userProfileCommandMapper.userProfileCommandToUser( command );

        assertThat( user.getId(), is(USER_ID) );
        assertThat( user.getFirstName(), is(FIRST_NAME) );
        assertThat( user.getLastName(), is(LAST_NAME) );
        assertThat( user.getEmail(), is(EMAIL) );
        assertThat( user.getPassword(), is(PASSWORD) );
        assertThat( user.getUserPrefs().getAvatarImage(), is(USER_PREF_AVATAR) );
    }


    private UserPrefs buildUserPrefs() {
        UserPrefs up = new UserPrefs();
        up.setId( USER_PREF_ID );
        up.setAvatarImage( USER_PREF_AVATAR );
        return up;
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

    private UserProfileCommand buildUserProfileCommand() {
        UserProfileCommand uc = new UserProfileCommand();
        uc.setId( USER_ID );
        uc.setFirstName( FIRST_NAME );
        uc.setLastName( LAST_NAME );
        uc.setEmail( EMAIL );
        uc.setPassword( PASSWORD );
        uc.setAvatarImage( USER_PREF_AVATAR );
        return uc;
    }
}
