package com.blogen.services;

import com.blogen.builders.Builder;
import com.blogen.commands.UserCommand;
import com.blogen.commands.UserPrefsCommand;
import com.blogen.commands.UserProfileCommand;
import com.blogen.commands.mappers.UserCommandMapper;
import com.blogen.commands.mappers.UserProfileCommandMapper;
import com.blogen.domain.User;
import com.blogen.domain.UserPrefs;
import com.blogen.repositories.UserRepository;
import com.blogen.services.security.EncryptionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;

/**
 * UnitTests for {@link UserServiceImpl}
 *
 * @author Cliff
 */
public class UserServiceImplTest {

    private static final Long   USER1_ID = 1L;
    private static final Long   USER2_ID = 2L;
    private static final Long   USER3_ID = 3L;
    private static final String USER1_USERNAME  = "johndoe";
    private static final String USER1_FIRSTNAME = "john";
    private static final String USER1_LASTNANE  = "doe";
    private static final String USER1_EMAIL     = "jd@yahoo.com";
    private static final String USER2_USERNAME = "janedoe";
    private static final String USER3_USERNAME = "william456";
    private static final Long   USER_PREFS_ID = 55L;
    private static final String USER_PREFS_AVATAR = "avatar1.jpg";
    private static final String PASSWORD = "secret";
    private static final String ENCRYPTED_PASSWORD = "3he4n5c6rypted";

    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private EncryptionService encryptionService;


    private UserCommandMapper userCommandMapper = UserCommandMapper.INSTANCE;
    private UserProfileCommandMapper userProfileCommandMapper = UserProfileCommandMapper.INSTANCE;


    @BeforeEach
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks( this );
        userService = new UserServiceImpl( userRepository, userCommandMapper, userProfileCommandMapper, encryptionService );
    }

    @Test
    public void should_returnOneUser_when_getUserById() {
        User user = Builder.buildUser( USER1_ID, USER1_USERNAME, USER1_FIRSTNAME, USER1_LASTNANE, USER1_EMAIL, PASSWORD, ENCRYPTED_PASSWORD );

        given( userRepository.findById(anyLong()) ).willReturn( Optional.of(user) );

        UserCommand command = userService.getUserById( USER1_ID );

        then( userRepository ).should().findById(anyLong());
        assertThat( command.getId(), is( USER1_ID) );
    }

    @Test
    public void should_ReturnTwoUsersContainingString_doe_when_getUserByNameLike() {
        User user1 = Builder.buildUser( USER1_ID, USER1_USERNAME, USER1_FIRSTNAME, USER1_LASTNANE, USER1_EMAIL, PASSWORD, ENCRYPTED_PASSWORD );
        User user2 = Builder.buildUser( USER2_ID, USER2_USERNAME, USER1_FIRSTNAME, USER1_LASTNANE, USER1_EMAIL, PASSWORD, ENCRYPTED_PASSWORD );
        String likeName = "doe";
        List<User> users = Arrays.asList( user1, user2 );

        given( userRepository.findByUserNameIgnoreCaseContaining( anyString() ) ).willReturn( users );

        List<UserCommand> commands = userService.getUserByNameLike( likeName );

        then( userRepository ).should().findByUserNameIgnoreCaseContaining( anyString() );
        assertThat( commands.size(), is(2));
        assertThat( commands.get(0).getUserName().contains( likeName ), is(true) );
        assertThat( commands.get(1).getUserName().contains( likeName ), is(true) );
    }
    

    @Test
    public void should_returnThreeUsers_when_GetAllUsers() {
        User user1 = Builder.buildUser( USER1_ID, USER1_USERNAME, USER1_FIRSTNAME, USER1_LASTNANE, USER1_EMAIL, PASSWORD, ENCRYPTED_PASSWORD );
        User user2 = Builder.buildUser( USER2_ID, USER2_USERNAME, USER1_FIRSTNAME, USER1_LASTNANE, USER1_EMAIL, PASSWORD, ENCRYPTED_PASSWORD );
        User user3 = Builder.buildUser( USER3_ID, USER3_USERNAME, USER1_FIRSTNAME, USER1_LASTNANE, USER1_EMAIL, PASSWORD, ENCRYPTED_PASSWORD );
        List<User> users = Arrays.asList( user1, user2, user3 );

        given( userRepository.findAll() ).willReturn( users );

        List<UserCommand> commands = userService.getAllUsers();

        then( userRepository ).should().findAll();
        assertThat( commands.size(), is(3));
        
    }

    @Test
    public void should_SaveUser_when_SaveUserCommand() throws Exception {
        UserPrefsCommand upc = Builder.buildUserPrefsCommand( USER_PREFS_ID, USER_PREFS_AVATAR );
        UserCommand commandToSave = Builder.buildUserCommand( USER1_ID, USER3_USERNAME, upc );
        User savedUser = Builder.buildUser( USER1_ID, USER3_USERNAME, USER1_FIRSTNAME, USER1_LASTNANE, USER1_EMAIL, PASSWORD, ENCRYPTED_PASSWORD );
        savedUser.setUserPrefs( Builder.buildUserPrefs( USER_PREFS_ID, USER_PREFS_AVATAR ) );
        User fetchedUser = Builder.buildUser( USER1_ID, USER1_USERNAME, USER1_FIRSTNAME, USER1_LASTNANE, USER1_EMAIL, PASSWORD, ENCRYPTED_PASSWORD );

        given( userRepository.save( any( User.class ) )).willReturn( savedUser );
        given( userRepository.findById( anyLong() )).willReturn( Optional.of(fetchedUser) );
        given( encryptionService.encrypt( anyString() )).willReturn( ENCRYPTED_PASSWORD );

        UserCommand savedCommand = userService.saveUserCommand( commandToSave );

        then( userRepository).should().save( any( User.class ) );
        assertThat( savedCommand.getId(), is( USER1_ID ));
        assertThat( savedCommand.getUserName(), is(USER3_USERNAME) );
        assertThat( savedCommand.getUserPrefs().getId(), is( USER_PREFS_ID) );
        assertThat( savedCommand.getUserPrefs().getAvatarImage(),is(USER_PREFS_AVATAR) );

    }

    @Test
    public void should_saveUserProfile_when_saveUserProfileCommand() {
        UserProfileCommand upc = Builder.buildUserProfileCommand( USER_PREFS_ID, USER1_FIRSTNAME, USER1_LASTNANE, USER1_EMAIL, USER_PREFS_AVATAR, PASSWORD, PASSWORD);
        UserPrefs userPrefs = Builder.buildUserPrefs( USER1_ID, USER_PREFS_AVATAR );
        User savedUser = Builder.buildUser( USER1_ID, USER1_USERNAME, USER1_FIRSTNAME, USER1_LASTNANE, USER1_EMAIL, PASSWORD, ENCRYPTED_PASSWORD );
        savedUser.setUserPrefs( userPrefs );
        User fetchedUser = Builder.buildUser( USER1_ID, USER1_USERNAME, USER1_FIRSTNAME, USER1_LASTNANE, USER1_EMAIL, PASSWORD, ENCRYPTED_PASSWORD );
        fetchedUser.setUserPrefs( userPrefs );

        given( userRepository.save( any( User.class) )).willReturn( savedUser );
        given( userRepository.findById(anyLong())).willReturn( Optional.of(fetchedUser) );

        UserProfileCommand savedCommand = userService.saveUserProfileCommand( upc );

        then( userRepository ).should().save( any( User.class ) );
        assertThat( savedCommand.getId(), is(USER1_ID) );
        assertThat( savedCommand.getFirstName(), is(USER1_FIRSTNAME) );
        assertThat( savedCommand.getLastName(), is(USER1_LASTNANE ) );
        assertThat( savedCommand.getEmail(), is(USER1_EMAIL) );
        assertThat( savedCommand.getAvatarImage(), is(USER_PREFS_AVATAR) );
    }

    @Test
    public void should_encryptAndSavePassword_when_savePassword() {
        UserProfileCommand upc = Builder.buildUserProfileCommand( USER_PREFS_ID, USER1_FIRSTNAME, USER1_LASTNANE, USER1_EMAIL, USER_PREFS_AVATAR, PASSWORD, PASSWORD);
        upc.setUserName( USER1_USERNAME );
        User savedUser = Builder.buildUser( USER1_ID, USER1_USERNAME, USER1_FIRSTNAME, USER1_LASTNANE, USER1_EMAIL, null, ENCRYPTED_PASSWORD );
        User fetchedUser = Builder.buildUser( USER1_ID, USER1_USERNAME, USER1_FIRSTNAME, USER1_LASTNANE, USER1_EMAIL, null, ENCRYPTED_PASSWORD );

        given( encryptionService.encrypt( anyString() )).willReturn( ENCRYPTED_PASSWORD );
        given( userRepository.save( any( User.class) )).willReturn( savedUser );
        given( userRepository.findByUserName( anyString() )).willReturn( fetchedUser );

        userService.savePassword( upc );

        then( userRepository ).should().findByUserName( anyString() );
        then( encryptionService ).should().encrypt( anyString() );
        then( userRepository ).should().save( any(User.class) );
        assertThat( savedUser.getEncryptedPassword(), is(ENCRYPTED_PASSWORD));
    }

}