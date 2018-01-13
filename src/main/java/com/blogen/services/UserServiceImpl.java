package com.blogen.services;

import com.blogen.commands.UserCommand;
import com.blogen.commands.mappers.UserCommandMapper;
import com.blogen.domain.User;
import com.blogen.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Service for CRUD operations on Blogen users
 *
 * @author Cliff
 */
@Service
public class UserServiceImpl implements UserService {

    private UserRepository userRepository;
    private UserCommandMapper userCommandMapper;

    @Autowired
    public UserServiceImpl( UserRepository userRepository, UserCommandMapper userCommandMapper ) {
        this.userRepository = userRepository;
        this.userCommandMapper = userCommandMapper;
    }

    /**
     * get a user by their id
     * @param id the user id to search for
     * @return a {@link UserCommand} containing the details of the user
     */
    @Override
    public UserCommand getUserById( Long id ) {
        return userCommandMapper.userToUserCommand( userRepository.findOne( id ) );
    }

    /**
     * get all users with a userName field containing the substring passed in the 'name' parameter
     * @param name a substring containing the characters to search for in the userName field
     * @return a List of {@link UserCommand} objects with userName(s) matching the name parameter
     */
    @Override
    public List<UserCommand> getUserByNameLike( String name ) {
        List<User> users = userRepository.findByUserNameIgnoreCaseContaining( name );
        return users.stream()
                .map( userCommandMapper::userToUserCommand )
                .collect( Collectors.toList());
    }

    /**
     * get all users of Blogen
     * @return a List of {@link UserCommand}
     */
    @Override
    public List<UserCommand> getAllUsers() {
        List<User> users = userRepository.findAll();
        return users.stream()
                .map( userCommandMapper::userToUserCommand )
                .collect( Collectors.toList());
    }
}