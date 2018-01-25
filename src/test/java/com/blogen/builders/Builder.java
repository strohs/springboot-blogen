package com.blogen.builders;

import com.blogen.commands.*;
import com.blogen.domain.Category;
import com.blogen.domain.Post;
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

    public static User buildUser( Long id, String userName, String firstName, String lastName, String email, String password, String encryptedPassword ) {
        User user = new User();
        user.setId( id );
        user.setUserName( userName );
        user.setFirstName( firstName );
        user.setLastName( lastName );
        user.setEmail( email );
        user.setPassword( password );
        user.setEncryptedPassword( encryptedPassword );
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

    public static UserCommand buildUserCommand( Long id, String username, String firstname, String lastname, String email, String password ) {
        UserCommand uc = new UserCommand();
        uc.setId( id );
        uc.setUserName( username );
        uc.setFirstName( firstname );
        uc.setLastName( lastname );
        uc.setEmail( email );
        uc.setPassword( password );;
        return uc;
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

    public static Post buildPost( Long id, String title, String text, String image, Category cat, User user, Post parent ) {
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

    public static PostCommand buildPostCommand( Long id, String title, String text, String image, String catName, Long userId, String userName, String avatarName, Long parentId ) {
        PostCommand pc = new PostCommand();
        pc.setId( id );
        pc.setTitle( title );
        pc.setText( text );
        pc.setImageUrl( image );
        pc.setCategoryName( catName );
        pc.setUserId( userId );
        pc.setUserAvatar( avatarName );
        pc.setUserName( userName );
        pc.setParentId( parentId );
        return pc;
    }

    public static Category buildCategory( Long id, String name ) {
        Category cat = new Category();
        cat.setId( id );
        cat.setName( name );
        return cat;
    }

    public static CategoryCommand buildCategoryCommand( Long id, String name ) {
        CategoryCommand cc = new CategoryCommand();
        cc.setId( id );
        cc.setName( name );
        return cc;
    }



}
