package com.blogen.services;

import com.blogen.commands.UserCommand;
import com.blogen.commands.UserPrefsCommand;
import com.blogen.commands.mappers.UserCommandMapper;
import com.blogen.domain.User;
import com.blogen.domain.UserPrefs;
import com.blogen.repositories.UserRepository;
import com.blogen.services.security.EncryptionService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Matchers.anyString;

/**
 * UnitTests for {@link UserServiceImpl}
 *
 * @author Cliff
 */
public class UserServiceImplTest {

    private static final Long   USER1_ID = 1L;
    private static final Long   USER2_ID = 2L;
    private static final Long   USER3_ID = 3L;
    private static final String USER1_USERNAME = "johndoe";
    private static final String USER2_USERNAME = "janedoe";
    private static final String USER3_USERNAME = "william456";
    private static final Long   USER_PREFS_ID = 55L;
    private static final String USER_PREFS_AVATAR = "avatar1.jpg";
    private static final String ENCRYPTED_PASSWORD = "encrypted";

    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private EncryptionService encryptionService;


    private UserCommandMapper userCommandMapper = UserCommandMapper.INSTANCE;


    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks( this );
        userService = new UserServiceImpl( userRepository, userCommandMapper, encryptionService );
    }

    @Test
    public void whenGetUserById_shouldReturnOneUser() {
        User user = buildUser( USER1_ID, USER1_USERNAME );

        given( userRepository.findOne( anyLong() ) ).willReturn( user );

        UserCommand command = userService.getUserById( USER1_ID );

        then( userRepository ).should().findOne( anyLong() );
        assertThat( command.getId(), is( USER1_ID) );
    }

    @Test
    public void whenGetUserByNameLike_shouldReturnTwoUsers_containingString_doe() {
        User user1 = buildUser( USER1_ID, USER1_USERNAME );
        User user2 = buildUser( USER2_ID, USER2_USERNAME );
        String likeName = "doe";
        List<User> users = Arrays.asList( user1, user2 );

        given( userRepository.findByUserNameIgnoreCaseContaining( anyString() ) ).willReturn( users );

        List<UserCommand> commands = userService.getUserByNameLike( likeName );

        then( userRepository ).should().findByUserNameIgnoreCaseContaining( anyString() );
        assertThat( commands.size(), is(2));
        assertThat( commands.get(0).getUserName().contains( likeName ), is(true) );
        assertThat( commands.get(1).getUserName().contains( likeName ), is(true) );
    }

    //TODO test case for when user name like, is NOT found, should throw exception

    @Test
    public void whenGetAllUsers_shouldReturnThreeUsers() {
        User user1 = buildUser( USER1_ID, USER1_USERNAME );
        User user2 = buildUser( USER2_ID, USER2_USERNAME );
        User user3 = buildUser( USER3_ID, USER3_USERNAME );
        List<User> users = Arrays.asList( user1, user2, user3 );

        given( userRepository.findAll() ).willReturn( users );

        List<UserCommand> commands = userService.getAllUsers();

        then( userRepository ).should().findAll();
        assertThat( commands.size(), is(3));
        
    }

    @Test
    public void whenSaveUserCommand_shouldSaveUser() throws Exception {
        UserPrefsCommand upc = buildUserPrefsCommand( USER_PREFS_ID, USER_PREFS_AVATAR );
        UserCommand commandToSave = buildUserCommand( USER1_ID, USER3_USERNAME, upc );
        User savedUser = buildUser( USER1_ID, USER3_USERNAME );
        savedUser.setUserPrefs( buildUserPrefs( USER_PREFS_ID, USER_PREFS_AVATAR ) );
        User fetchedUser = buildUser( USER1_ID, USER1_USERNAME );

        given( userRepository.save( any( User.class ) )).willReturn( savedUser );
        given( userRepository.findOne( anyLong() )).willReturn( fetchedUser );
        given( encryptionService.encrypt( anyString() )).willReturn( ENCRYPTED_PASSWORD );

        UserCommand savedCommand = userService.saveUserCommand( commandToSave );

        then( userRepository).should().save( any( User.class ) );
        assertThat( savedCommand.getId(), is( USER1_ID ));
        assertThat( savedCommand.getUserName(), is(USER3_USERNAME) );
        assertThat( savedCommand.getUserPrefs().getId(), is( USER_PREFS_ID) );
        assertThat( savedCommand.getUserPrefs().getAvatarImage(),is(USER_PREFS_AVATAR) );

    }


    private User buildUser( Long id, String userName ) {
        User user = new User();
        user.setId( id );
        user.setUserName( userName );
        return user;
    }

    private UserPrefs buildUserPrefs( Long id, String avatarName ) {
        UserPrefs prefs = new UserPrefs();
        prefs.setId( id );
        prefs.setAvatarImage( avatarName );
        return prefs;
    }

    private UserCommand buildUserCommand( Long id, String userName, UserPrefsCommand userPrefs ) {
        UserCommand command = new UserCommand();
        command.setId( id );
        command.setUserName( userName );
        command.setUserPrefs( userPrefs );
        return command;
    }

    private UserPrefsCommand buildUserPrefsCommand( Long id, String avatarName ) {
        UserPrefsCommand prefs = new UserPrefsCommand();
        prefs.setId( id );
        prefs.setAvatarImage( avatarName );
        return prefs;
    }
}