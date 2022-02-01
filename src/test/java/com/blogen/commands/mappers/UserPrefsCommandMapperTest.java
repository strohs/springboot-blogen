package com.blogen.commands.mappers;

import com.blogen.commands.UserPrefsCommand;
import com.blogen.domain.UserPrefs;
import org.junit.jupiter.api.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Unit Test mappings to/from UserPrefs and UserPrefsCommand
 *
 * @author Cliff
 */
public class UserPrefsCommandMapperTest {

    private static final Long PREF_ID = 1L;
    private static final String PREF_AVATAR_NAME = "Avatar5.jpg";

    private UserPrefsCommandMapper userPrefsCommandMapper =  UserPrefsCommandMapper.INSTANCE;


    @Test
    public void userPrefsToUserPrefsCommand() {
        UserPrefs up = new UserPrefs();
        up.setId( PREF_ID );
        up.setAvatarImage( PREF_AVATAR_NAME );

        UserPrefsCommand upc = userPrefsCommandMapper.userPrefsToUserPrefsCommand( up );

        assertThat( upc.getId(), is( PREF_ID ));
        assertThat( upc.getAvatarImage(), is(PREF_AVATAR_NAME) );

    }

    @Test
    public void userPrefsCommandToUserPrefs() {
        UserPrefsCommand upc = new UserPrefsCommand();
        upc.setId( PREF_ID );
        upc.setAvatarImage( PREF_AVATAR_NAME );

        UserPrefs up = userPrefsCommandMapper.userPrefsCommandToUserPrefs( upc );

        assertThat( up.getId(), is( PREF_ID ) );
        assertThat( up.getAvatarImage(), is( PREF_AVATAR_NAME ) );
    }
}