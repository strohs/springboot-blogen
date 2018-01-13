package com.blogen.services;

import com.blogen.commands.UserCommand;
import com.blogen.commands.UserPrefsCommand;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;


/**
 * Integration Tests for {@link UserServiceImpl}
 *
 * @author Cliff
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class UserServiceImplIT {

    //john does ID in the user table
    private static final Long JOHN_DOE_ID = 2L;

    @Autowired
    UserService userService;



    @Test
    @Transactional
    public void saveUserCommand_shouldSaveFirstName() {
        UserCommand johnDoeCommand = userService.getUserById( JOHN_DOE_ID );
        johnDoeCommand.setFirstName( "Jane" );
        UserCommand savedCommand = userService.saveUserCommand( johnDoeCommand );

        assertThat( savedCommand.getFirstName(), is("Jane"));

    }

    @Test
    @Transactional
    public void saveUserCommand_shouldSaveNewAvatarName() {
        UserCommand johnDoeCommand = userService.getUserById( JOHN_DOE_ID );
        johnDoeCommand.getUserPrefs().setAvatarImage( "Avatar99.jpg" );

        userService.saveUserCommand( johnDoeCommand );

        UserCommand savedCommand = userService.getUserById( JOHN_DOE_ID );
        assertThat( savedCommand.getUserPrefs().getAvatarImage(), is("Avatar99.jpg") );

    }


}