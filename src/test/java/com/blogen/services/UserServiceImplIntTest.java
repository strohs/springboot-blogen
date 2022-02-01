package com.blogen.services;

import com.blogen.commands.UserCommand;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;


/**
 * Integration Tests for {@link UserServiceImpl}
 *
 * @author Cliff
 */
@SpringBootTest
public class UserServiceImplIntTest {

    //john does ID in the user table
    private static final Long JOHN_DOE_ID = 2L;

    @Autowired
    UserService userService;


    @Test
    @Transactional
    public void should_saveFirstName_when_saveUserCommand() {
        UserCommand johnDoeCommand = userService.getUserById( JOHN_DOE_ID );
        johnDoeCommand.setFirstName( "Jane" );
        UserCommand savedCommand = userService.saveUserCommand( johnDoeCommand );

        assertThat( savedCommand.getFirstName(), is("Jane"));
    }

    @Test
    @Transactional
    public void should_saveNewAvatarName_when_saveUserCommand() {
        UserCommand johnDoeCommand = userService.getUserById( JOHN_DOE_ID );
        johnDoeCommand.getUserPrefs().setAvatarImage( "Avatar99.jpg" );

        userService.saveUserCommand( johnDoeCommand );

        UserCommand savedCommand = userService.getUserById( JOHN_DOE_ID );
        assertThat( savedCommand.getUserPrefs().getAvatarImage(), is("Avatar99.jpg") );
    }


}