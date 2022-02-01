package com.blogen.services;

import com.blogen.commands.UserPrefsCommand;
import com.blogen.commands.mappers.UserPrefsCommandMapper;
import com.blogen.domain.UserPrefs;
import com.blogen.repositories.UserPrefsRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

/**
 * Unit Tests for {@link UserPrefsServiceImpl}
 * @author Cliff
 */
public class UserPrefsServiceImplTest {

    private static final Long   USER_ID = 22L;
    private static final String USER_NAME = "johndoe";
    private static final Long   USER_PREFS_ID = 2L;
    private static final String AVATAR_IMAGE_NAME = "avatar1.jpg";

    //system under test
    private UserPrefsService userPrefsService;

    @Mock
    private UserPrefsRepository userPrefsRepository;

    private UserPrefsCommandMapper userPrefsCommandMapper = UserPrefsCommandMapper.INSTANCE;



    @BeforeEach
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks( this );
        userPrefsService = new UserPrefsServiceImpl( userPrefsCommandMapper, userPrefsRepository );
    }

    @Test
    public void shouldGetUserPrefs_whenGetUserPrefsByUserId_isCalledWithUserId() {
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