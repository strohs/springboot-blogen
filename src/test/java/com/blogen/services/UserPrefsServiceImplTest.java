package com.blogen.services;

import com.blogen.commands.UserPrefsCommand;
import com.blogen.commands.mappers.UserPrefsCommandMapper;
import com.blogen.domain.User;
import com.blogen.domain.UserPrefs;
import com.blogen.repositories.UserPrefsRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Matchers.anyLong;

/**
 * Unit Tests for {@link UserPrefsServiceImpl}
 * @author Cliff
 */
public class UserPrefsServiceImplTest {

    private static final Long   USER_ID = 22L;
    private static final String USER_NAME = "johndoe";
    private static final Long   USER_PREFS_ID = 2L;
    private static final String AVATAR_IMAGE_NAME = "avatar1.jpg";


    UserPrefsService userPrefsService;

    @Mock
    UserPrefsRepository userPrefsRepository;

    UserPrefsCommandMapper userPrefsCommandMapper = UserPrefsCommandMapper.INSTANCE;



    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks( this );
        userPrefsService = new UserPrefsServiceImpl( userPrefsCommandMapper, userPrefsRepository );
    }

    @Test
    public void shouldGetUserPrefs_whenGetUserPrefs_isCalledWithUserId() {
        UserPrefs prefs = new UserPrefs();
        prefs.setId( USER_PREFS_ID );
        prefs.setAvatarImage( AVATAR_IMAGE_NAME );

        given( userPrefsRepository.findByUser_Id( anyLong() )).willReturn( prefs );

        UserPrefsCommand command = userPrefsService.getUserPrefsByUserId( USER_ID );

        then( userPrefsRepository ).should().findByUser_Id( anyLong() );
        assertThat( command.getId(), is( USER_PREFS_ID ) );
        assertThat( command.getAvatarImage(), is( AVATAR_IMAGE_NAME ));

    }

}