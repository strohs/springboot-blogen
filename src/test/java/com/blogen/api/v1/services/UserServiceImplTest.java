package com.blogen.api.v1.services;

import com.blogen.api.v1.controllers.UserRestController;
import com.blogen.api.v1.mappers.UserMapper;
import com.blogen.api.v1.model.UserDTO;
import com.blogen.api.v1.model.UserListDTO;
import com.blogen.builders.Builder;
import com.blogen.domain.User;
import com.blogen.exceptions.BadRequestException;
import com.blogen.repositories.UserRepository;
import com.blogen.services.security.EncryptionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;

/**
 * Unit Tests for the User REST Service
 * @author Cliff
 */
public class UserServiceImplTest {

    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private EncryptionService encryptionService;

    private UserMapper userMapper = UserMapper.INSTANCE;

    User user1;
    String user1Url;
    User user2;
    String user2Url;
    User newUser;
    String newUserUrl;
    User updatedUser1;
    UserDTO newUserDTO;

    @BeforeEach
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks( this );
        userService = new UserServiceImpl( userRepository, encryptionService, userMapper );
        user1 = Builder.buildUser( 1L, "johndoe", "John","Doe", "jdoe@gmail.com","","123abc");
        updatedUser1 = Builder.buildUser( 1L, "johndoe", "John","Doe", "jdoe@gmail.com","","123abc");
        user1Url = UserRestController.BASE_URL + "/1";
        user2 = Builder.buildUser( 2L, "mgill", "Maggy","McGill", "mags@hotmail.com","","123abc");
        user2Url = UserRestController.BASE_URL + "/2";
        newUserDTO = new UserDTO( "new","user","newuser","newby@zing.org","password",null );
        newUser = Builder.buildUser( 3L, "newuser","new","user","newby@zing.com",null ,"123abc");
        newUserUrl = UserRestController.BASE_URL + "/3";
    }

    @Test
    public void should_returnTwoUsers_when_getAllUsers() {
        List<User> userList = Arrays.asList( user1,user2 );

        given( userRepository.findAll() ).willReturn( userList );

        UserListDTO userListDTO = userService.getAllUsers();

        then( userRepository ).should().findAll();
        assertThat( userListDTO, is( notNullValue() ));
        assertThat( userListDTO.getUsers().size(), is( 2) );
        assertThat( userListDTO.getUsers().get( 0 ).getUserUrl(), is( user1Url) );
    }

    @Test
    public void should_getUser1_when_getUserWithValidId() {
        given( userRepository.findById(anyLong())).willReturn( Optional.of( user1 ) );

        UserDTO userDTO = userService.getUser( 1L );

        then( userRepository ).should().findById(anyLong());
        assertThat( userDTO, is( notNullValue()) );
        assertThat( userDTO.getUserUrl(), is(user1Url) );
    }

    @Test
    public void should_throwBadRequestException_whenGetUserWithInvalidId() {
        given( userRepository.findById(anyLong())).willReturn( Optional.empty() );

        assertThrows(BadRequestException.class, () -> userService.getUser( 234L ));

//        then( userRepository )
//                .should()
//                .findById(anyLong());
    }

    @Test
    public void should_createNewUser_when_createNewUser_withValidUserDTO() {

        given( userRepository.save( any(User.class) )).willReturn( newUser );
        given( encryptionService.encrypt( anyString() )).willReturn( "123abc" );

        UserDTO userDTO = userService.createNewUser( newUserDTO );

        then( userRepository ).should().save( any(User.class) );
        then( encryptionService ).should().encrypt( anyString() );
        assertThat( userDTO, is( notNullValue() ) );
        assertThat( userDTO.getFirstName(), is( newUser.getFirstName()) );
        assertThat( userDTO.getLastName(), is( newUser.getLastName()) );
        assertThat( userDTO.getUserName(), is( newUser.getUserName()) );
        assertThat( userDTO.getEmail(), is( newUser.getEmail()) );
        assertThat( userDTO.getPassword(), is( newUser.getPassword()) );
        assertThat( userDTO.getUserUrl(), is( newUserUrl) );
    }

    @Test
    public void should_throwBadRequestException_when_userNameAlreadyExists() {
        given( userRepository.save( any(User.class) )).willThrow( DataIntegrityViolationException.class );

        assertThrows( BadRequestException.class, () -> userService.createNewUser( newUserDTO ));

        //then( userRepository ).should().save( any(User.class) );
    }

    @Test
    public void should_updateFirstName_when_updateUser() {
        newUserDTO = new UserDTO();
        newUserDTO.setFirstName( "NewFirstName" );
        updatedUser1.setFirstName( "NewFirstName" );
        
        given( userRepository.findById(anyLong()) ).willReturn( Optional.of( user1 ) );
        given( userRepository.save( any(User.class) )).willReturn( updatedUser1 );

        UserDTO savedDTO = userService.updateUser( 1L, newUserDTO );

        then( userRepository ).should().save( any( User.class ) );
        assertThat( savedDTO.getFirstName(), is( newUserDTO.getFirstName()) );
        assertThat( savedDTO.getLastName(), is( user1.getLastName()) );
    }
}