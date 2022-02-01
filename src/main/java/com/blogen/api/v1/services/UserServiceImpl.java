package com.blogen.api.v1.services;

import com.blogen.api.v1.controllers.UserRestController;
import com.blogen.api.v1.mappers.UserMapper;
import com.blogen.api.v1.model.UserDTO;
import com.blogen.api.v1.model.UserListDTO;
import com.blogen.domain.User;
import com.blogen.domain.UserPrefs;
import com.blogen.exceptions.BadRequestException;
import com.blogen.repositories.UserRepository;
import com.blogen.services.security.EncryptionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Service for RESTful operations on Blogen {@link com.blogen.domain.User}(s)
 *
 * @author Cliff
 */
@Slf4j
@Service("userRestService")
public class UserServiceImpl implements UserService {

    private UserRepository userRepository;
    private EncryptionService encryptionService;
    private UserMapper userMapper;

    private static final String DEFAULT_AVATAR = "avatar0.jpg";

    @Autowired
    public UserServiceImpl( UserRepository userRepository,
                            EncryptionService encryptionService,
                            UserMapper userMapper ) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.encryptionService = encryptionService;
    }

    @Override
    public UserListDTO getAllUsers() {
        List<UserDTO> userDTOS = userRepository.findAll()
                .stream()
                .map( user -> {
                    UserDTO dto = userMapper.userToUserDto( user );
                    dto.setUserUrl( buildUserUrl( user ) );
                    return dto;
                } ).collect( Collectors.toList());
        return new UserListDTO( userDTOS );
    }

    @Override
    public UserDTO getUser( Long id ) {
        User user = validateUserId( id );
        UserDTO userDTO = userMapper.userToUserDto( user );
        userDTO.setUserUrl( buildUserUrl( user ) );
        return userDTO;
    }

    @Override
    public UserDTO createNewUser( UserDTO userDTO ) {
        //required fields: firstName, lastName, email, userName, password
        User user = userMapper.userDtoToUser( userDTO );
        user.setUserPrefs( buildDefaultUserPrefs() );
        user.setEncryptedPassword( encryptionService.encrypt( user.getPassword() ) );
        User savedUser;
        try {
            savedUser = userRepository.save( user );
        } catch ( DataIntegrityViolationException ex ) {
            throw new BadRequestException( "user with userName=" + userDTO.getUserName() + " already exists" );
        }
        UserDTO returnDto = userMapper.userToUserDto( savedUser );
        returnDto.setUserUrl( buildUserUrl( savedUser ) );
        //todo set password to null? and normally use SSL in production to send secure data
        return returnDto;
    }

    @Override
    public UserDTO updateUser( Long id,  UserDTO userDTO ) {
        User user = validateUserId( id );
        //userName cannot be updated and should not be sent
        if ( userDTO.getUserName() != null ) throw new BadRequestException( "userName cannot be updated and should not be sent as part of the request" );
        user = mergeUserDtoToUser( userDTO, user );
        //check if password sent
        user = checkAndEncryptPassword( user );
        User savedUser = userRepository.save( user );
        UserDTO returnDto = userMapper.userToUserDto( savedUser );
        returnDto.setUserUrl( buildUserUrl( savedUser ) );
        return returnDto;
    }

    /**
     * encrypts the user password if it was set on the User object
     * @param user
     * @return the User object with the encryptedPassword field set
     */
    private User checkAndEncryptPassword( User user ) {
        if ( user.getPassword() != null ) {
            user.setEncryptedPassword( encryptionService.encrypt( user.getPassword() ) );
        }
        return user;
    }

    /**
     * make the passed in user id exists in the repository, otherwise an exception is thrown
     * @param id
     * @return the {@link User} corresponding to the passed in ID
     * @throws BadRequestException if the user was not found in the repository
     */
    private User validateUserId( Long id ) throws BadRequestException {
        User user = userRepository.findById( id )
                .orElseThrow( () -> new BadRequestException( "user with id=" + id + " does not exist" ) );
        return user;
    }


    /**
     * build a default user preferences object with default avatar image set
     * @return
     */
    private UserPrefs buildDefaultUserPrefs() {
        UserPrefs userPrefs = new UserPrefs();
        userPrefs.setAvatarImage( DEFAULT_AVATAR );
        return userPrefs;
    }

    /**
     * build the userUrl that gets returned with every response DTO
     * @param user
     * @return
     */
    private String buildUserUrl( User user ) {
        return UserRestController.BASE_URL + "/" + user.getId();
    }

    /**
     * merge non-null fields of UserDTO into the passed in User
     * @param dto
     * @param user
     * @return
     */
    private User mergeUserDtoToUser( UserDTO dto, User user ) {
        if ( dto.getFirstName() != null ) user.setFirstName( dto.getFirstName() );
        if ( dto.getLastName() != null ) user.setLastName( dto.getLastName() );
        if ( dto.getEmail() != null ) user.setEmail( dto.getEmail() );
        if ( dto.getPassword() != null ) user.setPassword( dto.getPassword() );
        //userName cannot be changed once a user is created;
        return user;
    }
}
