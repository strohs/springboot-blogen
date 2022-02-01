package com.blogen.api.v1.mappers;

import com.blogen.api.v1.model.UserDTO;
import com.blogen.domain.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * Unit Test mappings between {@link com.blogen.domain.User} and {@link com.blogen.api.v1.model.UserDTO}
 *
 * @author Cliff
 */
public class UserMapperTest {

    private static final Long ID = 11L;
    private static final String FIRSTNAME = "John";
    private static final String LASTNAME = "Doe";
    private static final String USERNAME = "doey156";
    private static final String EMAIL = "jdoe@cox.net";
    private static final String PASSWORD = "sEcReT";
    User user;

    private UserMapper userMapper = UserMapper.INSTANCE;

    @BeforeEach
    public void setUp() throws Exception {
        user = new User();
        user.setId( ID );
        user.setFirstName( FIRSTNAME );
        user.setLastName( LASTNAME );
        user.setUserName( USERNAME );
        user.setEmail( EMAIL );
        user.setPassword( PASSWORD );
    }

    @Test
    public void userToUserDto() {
        //given a user

        //when
        UserDTO userDTO = userMapper.userToUserDto( user );

        //then
        assertThat( userDTO.getFirstName(), equalTo( FIRSTNAME ) );
        assertThat( userDTO.getLastName(), equalTo( LASTNAME ) );
        assertThat( userDTO.getUserName(), equalTo( USERNAME ) );
        assertThat( userDTO.getEmail(), equalTo( EMAIL ) );
        assertThat( userDTO.getPassword(), equalTo( PASSWORD ) );
    }

    @Test
    public void userDtoToUser() {
        //given
        UserDTO userDTO = new UserDTO( FIRSTNAME,LASTNAME,USERNAME,EMAIL,PASSWORD, null );

        //when
        User user = userMapper.userDtoToUser( userDTO );

        //then
        assertThat( user.getFirstName(), equalTo( FIRSTNAME ) );
        assertThat( user.getLastName(), equalTo( LASTNAME ) );
        assertThat( user.getUserName(), equalTo( USERNAME ) );
        assertThat( user.getEmail(), equalTo( EMAIL ) );
        assertThat( user.getPassword(), equalTo( PASSWORD ) );
    }

    @Test
    public void userDtoToUser_withNullId_shouldSetUserIdToNull() {
        //given
        UserDTO userDTO = new UserDTO( FIRSTNAME,LASTNAME,USERNAME,EMAIL,PASSWORD,null );
        

        //when
        User user = userMapper.userDtoToUser( userDTO );

        //then
        assertNotNull( user );
        assertThat( user.getId(), is( nullValue() ));
    }
}