package com.blogen.controllers;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.is;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for {@link UserProfileController}
 *
 * @author Cliff
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@WebAppConfiguration
public class UserProfileControllerIT {

    private static final String AVATAR1          = "avatar1.jpg";
    private static final String AVATAR2          = "avatar2.jpg";
    private static final int    TOTAL_AVATARS    = 7;
    private static final String USER1_ID         = "2";
    private static final String USER2_ID         = "3";
    private static final String USER1_FIRSTNAME  = "john";
    private static final String USER1_LASTNAME   = "doe";
    private static final String USER1_USERNAME   = "johndoe";
    private static final String USER2_USERNAME   = "mgill";
    private static final String USER1_EMAIL      = "doey@gmail.com";
    private static final String USER1_PW         = "password";
    private static final String USER1_PW_CONFIRM = "password";

    @Autowired
    private UserProfileController controller;

    @Autowired
    private WebApplicationContext webCtx;

    private MockMvc mockMvc;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks( this );
        mockMvc = MockMvcBuilders
                .webAppContextSetup( webCtx )
                .apply( springSecurity() )
                .build();
    }

    @Test
    @WithMockUser( username = USER1_USERNAME, password = USER1_PW, authorities = {"USER"})
    public void should_showProfileForAuthenticatedUser_when_showProfile() throws Exception {

        mockMvc.perform( get("/profile") )
                .andExpect( status().isOk() )
                .andExpect( view().name("profile") )
                .andExpect( model().attributeExists( "user" ) )
                .andExpect( model().attribute( "user", hasProperty("userName", is( USER1_USERNAME )) ));
    }

    @Test
    @Rollback
    @WithMockUser( username = USER1_USERNAME, authorities = {"USER"})
    public void should_saveUserProfileAndRedirectToProfilePage() throws Exception {

        mockMvc.perform( post( "/profile" )
                .contentType( MediaType.APPLICATION_FORM_URLENCODED )
                .param("id", USER1_ID )
                .param("userName",USER1_USERNAME )
                .param( "firstName",USER1_FIRSTNAME )
                .param( "lastName", USER1_LASTNAME )
                .param( "email", USER1_EMAIL )
                .param( "avatarImage",AVATAR1 )
        )
                .andExpect( status().is3xxRedirection() )
                .andExpect( view().name( "redirect:/profile" ) )
                .andExpect( flash().attributeExists( "successMessage" ) );
    }

    @Test
    @WithMockUser( username = USER1_USERNAME, authorities = {"USER"})
    public void should_haveBindingError_when_requiredFieldIsMissing() throws Exception {
        //avatarImage is required and will not be posted
        mockMvc.perform( post( "/profile" )
                .contentType( MediaType.APPLICATION_FORM_URLENCODED )
                .param("id", USER1_ID )
                .param("userName",USER1_USERNAME )
                .param( "firstName",USER1_FIRSTNAME )
                .param( "lastName", USER1_LASTNAME )
                .param( "email", USER1_EMAIL )
                //.param( "avatarImage",AVATAR1 )
        )
                .andExpect( status().isOk() )
                .andExpect( view().name( "profile" ) )
                .andExpect( model().hasErrors() );
    }

    @Test
    @Rollback
    @WithMockUser( username = USER1_USERNAME, authorities = {"USER"})
    public void should_savePasswordAndRedirectToProfilePage_when_savePasswordIsCorrect() throws Exception {
        mockMvc.perform( post( "/profile/password" )
                        .contentType( MediaType.APPLICATION_FORM_URLENCODED )
                        .param("id", USER1_ID )
                        .param("userName",USER1_USERNAME )
                        .param( "firstName",USER1_FIRSTNAME )
                        .param( "lastName", USER1_LASTNAME )
                        .param( "email", USER1_EMAIL )
                        .param( "avatarImage",AVATAR1 )
                        .param( "password",USER1_PW )
                        .param( "confirmPassword", USER1_PW_CONFIRM )
        )
                .andExpect( status().is3xxRedirection() )
                .andExpect( view().name( "redirect:/profile" ) )
                .andExpect( flash().attributeExists( "successMessage" ) );
    }



    @Test
    @WithMockUser( username = USER1_USERNAME, authorities = {"USER"})
    public void should_setNoMatchPasswordError_when_passwordsDoNotMatch() throws Exception {
        mockMvc.perform( post( "/profile/password" )
                .contentType( MediaType.APPLICATION_FORM_URLENCODED )
                .param("id", USER1_ID )
                .param("userName",USER1_USERNAME )
                .param( "firstName",USER1_FIRSTNAME )
                .param( "lastName", USER1_LASTNAME )
                .param( "email", USER1_EMAIL )
                .param( "avatarImage",AVATAR1 )
                .param( "password",USER1_PW )
                .param( "confirmPassword", "nomatchpassword" )
        )
                .andExpect( status().isOk() )
                .andExpect( view().name( "profile" ) )
                .andExpect( model().attributeHasFieldErrorCode( "user","password","nomatch.password" ) )
                .andExpect( model().attributeExists( "passwordError" ) );
    }

    @Test
    @WithMockUser( username = USER1_USERNAME, authorities = {"USER"})
    public void should_setPasswordSizeError_when_passwordIsIncorrectLength() throws Exception {
        mockMvc.perform( post( "/profile/password" )
                .contentType( MediaType.APPLICATION_FORM_URLENCODED )
                .param("id", USER1_ID )
                .param("userName",USER1_USERNAME )
                .param( "firstName",USER1_FIRSTNAME )
                .param( "lastName", USER1_LASTNAME )
                .param( "email", USER1_EMAIL )
                .param( "avatarImage",AVATAR1 )
                .param( "pass",USER1_PW )
                .param( "confirmPassword", "pass" )
        )
                .andExpect( status().isOk() )
                .andExpect( view().name( "profile" ) )
                .andExpect( model().attributeHasFieldErrorCode( "user","confirmPassword","size.password" ) )
                .andExpect( model().attributeExists( "passwordError" ) );
    }

    @Test
    @WithMockUser( username = USER1_USERNAME, authorities = {"USER"})
    public void should_throw4xxError_when_loggedInUserTriesToChangeAnotherUsersPassword() throws Exception {
        //user1 us logged in and tries to set users2 password
        mockMvc.perform( post( "/profile/password" )
                .contentType( MediaType.APPLICATION_FORM_URLENCODED )
                .param("id", USER2_ID )
                .param("userName",USER2_USERNAME )
                .param( "firstName",USER1_FIRSTNAME )
                .param( "lastName", USER1_LASTNAME )
                .param( "email", USER1_EMAIL )
                .param( "avatarImage",AVATAR1 )
                .param( "password",USER1_PW )
                .param( "confirmPassword", USER1_PW_CONFIRM )
        )
                .andExpect( status().is4xxClientError() );
    }
}