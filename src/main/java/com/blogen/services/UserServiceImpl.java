package com.blogen.services;

import com.blogen.commands.UserCommand;
import com.blogen.commands.UserProfileCommand;
import com.blogen.commands.mappers.UserCommandMapper;
import com.blogen.commands.mappers.UserProfileCommandMapper;
import com.blogen.domain.User;
import com.blogen.repositories.UserRepository;
import com.blogen.services.security.EncryptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    private UserProfileCommandMapper userProfileCommandMapper;
    private EncryptionService encryptionService;

    @Autowired
    public UserServiceImpl( UserRepository userRepository, UserCommandMapper userCommandMapper,
                            UserProfileCommandMapper userProfileCommandMapper, EncryptionService encryptionService ) {
        this.userRepository = userRepository;
        this.userCommandMapper = userCommandMapper;
        this.userProfileCommandMapper = userProfileCommandMapper;
        this.encryptionService = encryptionService;
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
     * gets a user by their userName
     * @param userName the userName used to retrieve a {@link User}
     * @return UserCommand containing the user properties
     */
    @Override
    public UserCommand getUserByUserName( String userName ) {
        return userCommandMapper.userToUserCommand( userRepository.findByUserName( userName ) );
    }

    /**
     * gets a {@link User} by their username
     *
     * @param name - username to search for
     * @return a {@link User} having the specified username
     */
    @Override
    public User findByUserName( String name ) {
        return userRepository.findByUserName( name );
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

    /**
     * saves a {@link User} to the underlying datastore. This method is primarily called when setting up new users
     *
     * @param user the user to save
     * @return the saved user
     */
    @Override
    public User saveUser( User user ) {
        if ( user.getPassword() != null )
            user.setEncryptedPassword( encryptionService.encrypt(  user.getPassword() ) );
        User savedUser = userRepository.save( user );
        return savedUser;
    }

    /**
     * save the updated user information stored in the passed in {@link UserCommand}
     *
     * @param command - contains the user data to be saved
     * @return a UserCommand object containing the saved data
     */
    @Override
    @Transactional
    public UserCommand saveUserCommand( UserCommand command ) {
        User userToSave = userCommandMapper.userCommandToUser( command );
        User fetchedUser = userRepository.findOne( command.getId() );
        mergeUsers( userToSave, fetchedUser );
        if( userToSave.getPassword() != null )
            fetchedUser.setEncryptedPassword( encryptionService.encrypt( userToSave.getPassword() ) );

        User savedUser = userRepository.save( fetchedUser );
        return userCommandMapper.userToUserCommand( savedUser );
    }

    /**
     * Returns a UserProfileCommand object using the userName parameter to look up the corresponding {@link User}
     *
     * @param userName
     * @return A {@link UserProfileCommand} containing mapped properties from a {@link User}
     */
    @Override
    public UserProfileCommand getUserProfileByUserName( String userName ) {
        User user = userRepository.findByUserName( userName );
        
        UserProfileCommand upc = userProfileCommandMapper.userToUserProfileCommand( user );
        return upc;
    }

    /**
     * saves the UserProfileCommand properties to the database. NOTE that passwords are not saved here, call
     * savePassword() separately to save those
     *
     * @param command
     * @return a UserProfileCommand representing the saved data
     */
    @Override
    public UserProfileCommand saveUserProfileCommand( UserProfileCommand command ) {
        User userToSave = userRepository.findOne( command.getId() );
        //merge fields from UserProfileCommand to User
        userToSave.setFirstName( command.getFirstName() );
        userToSave.setLastName( command.getLastName() );
        userToSave.setEmail( command.getEmail() );
        userToSave.getUserPrefs().setAvatarImage( command.getAvatarImage() );
        User savedUser = userRepository.save( userToSave );
        return userProfileCommandMapper.userToUserProfileCommand( savedUser );
    }

    /**
     * Saves the password from the UserProfileCommand into the database. The password is encrypted first and
     * stored in the {@link User}.encryptedPassword property.
     *
     * @param command
     * @return
     */
    @Override
    @PreAuthorize( "#command.userName == authentication.name" )
    public UserProfileCommand savePassword( UserProfileCommand command ) {
        User userToSave = userRepository.findByUserName( command.getUserName() );
        String encryptedPassword = encryptionService.encrypt( command.getPassword() );
        userToSave.setEncryptedPassword( encryptedPassword );
        User savedUser = userRepository.save( userToSave );
        return userProfileCommandMapper.userToUserProfileCommand( savedUser );
    }

    /**
     * merge some of the source user properties into target user
     * @param source
     * @param target
     */
    private void mergeUsers( User source, User target ) {
        target.setUserName( source.getUserName() );
        target.setPassword( source.getPassword() );
        target.setEmail( source.getEmail() );
        target.setFirstName( source.getFirstName() );
        target.setLastName( source.getLastName() );
        target.setUserPrefs( source.getUserPrefs() );
        //note: Roles are left untouched...for now

    }
}
