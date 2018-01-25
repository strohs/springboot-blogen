package com.blogen.builders;

import com.blogen.commands.UserCommand;
import com.blogen.commands.UserPrefsCommand;
import com.blogen.commands.UserProfileCommand;
import com.blogen.domain.User;
import com.blogen.domain.UserPrefs;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Helper methods for building various domain objects used in blogen tests
 *
 * @author Cliff
 */
public class Builder {

    public static User buildUser( Long id, String userName, String firstName, String lastName, String email, String password, String encryptePassword ) {
        User user = new User();
        user.setId( id );
        user.setUserName( userName );
        user.setFirstName( firstName );
        user.setLastName( lastName );
        user.setEmail( email );
        user.setPassword( password );
        user.setEncryptedPassword( encryptePassword );
        return user;
    }

    public static UserPrefs buildUserPrefs( Long id, String avatarName ) {
        UserPrefs prefs = new UserPrefs();
        prefs.setId( id );
        prefs.setAvatarImage( avatarName );
        return prefs;
    }

    public static UserCommand buildUserCommand( Long id, String userName, UserPrefsCommand userPrefs ) {
        UserCommand command = new UserCommand();
        command.setId( id );
        command.setUserName( userName );
        command.setUserPrefs( userPrefs );
        return command;
    }

    public static UserProfileCommand buildUserProfileCommand( Long id, String firstName, String lastName, String email,
                                                              String avatarImage, String pw, String pwConfirm ) {
        UserProfileCommand upc = new UserProfileCommand();
        upc.setId( id );
        upc.setFirstName( firstName );
        upc.setLastName( lastName );
        upc.setEmail( email );
        upc.setAvatarImage( avatarImage );
        upc.setPassword( pw );
        upc.setConfirmPassword( pwConfirm );
        return upc;
    }

    public static UserPrefsCommand buildUserPrefsCommand( Long id, String avatarName ) {
        UserPrefsCommand prefs = new UserPrefsCommand();
        prefs.setId( id );
        prefs.setAvatarImage( avatarName );
        return prefs;
    }

    public static List<String> buildAvatarImages( String ... names ) {
        return Arrays.stream( names ).collect( Collectors.toList());
    }
}
