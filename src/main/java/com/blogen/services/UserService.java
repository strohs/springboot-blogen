package com.blogen.services;

import com.blogen.commands.UserCommand;
import com.blogen.commands.UserProfileCommand;
import com.blogen.domain.User;

import java.util.List;

/**
 * Data Access methods needed to work with Blogen Users
 *
 * @author Cliff
 */
public interface UserService {

    UserCommand getUserById( Long id );

    UserCommand getUserByUserName( String userName );

    UserProfileCommand getUserProfileByUserName( String userName );

    User findByUserName( String name );

    User saveUser( User user );

    List<UserCommand> getUserByNameLike( String name );

    List<UserCommand> getAllUsers();

    UserCommand saveUserCommand( UserCommand command );

    UserProfileCommand saveUserProfileCommand( UserProfileCommand command );

    void savePassword( String userName, UserProfileCommand command );
}
