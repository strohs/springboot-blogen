package com.blogen.services;

import com.blogen.commands.UserCommand;

import java.util.List;

/**
 * Data Access methods needed to work with Blogen Users
 *
 * @author Cliff
 */
public interface UserService {

    UserCommand getUserById( Long id );

    List<UserCommand> getUserByNameLike( String name );

    List<UserCommand> getAllUsers();
}
