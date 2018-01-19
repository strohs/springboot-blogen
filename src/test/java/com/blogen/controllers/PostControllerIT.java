package com.blogen.controllers;

import com.blogen.commands.CategoryCommand;
import com.blogen.commands.PageCommand;
import com.blogen.commands.PostCommand;
import com.blogen.commands.UserCommand;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyLong;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * MockMVC integration tests for PostController
 *
 * 
 * @author Cliff
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@WebAppConfiguration
public class PostControllerIT {

    @Autowired
    PostController controller;

    @Autowired
    WebApplicationContext webCtx;

    private MockMvc mockMvc;

    private static final String USER1_NAME = "lizreed";
    private static final Long   USER1_ID   = 5L;
    private static final String USER1_PW   = "password";
    private static final String USER2_NAME = "mgill";
    private static final Long   USER2_ID   = 3L;
    private static final String USER2_PW   = "password";
    private static final String ROLE_USER = "USER";

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks( this );
        mockMvc = MockMvcBuilders
                .webAppContextSetup( webCtx )
                .apply( springSecurity() )
                .build();
    }

    @Test
    public void should_showAllPosts() throws Exception {
        
        mockMvc.perform( get("/posts").with( user(USER1_NAME).password( USER1_PW ).roles(ROLE_USER)) )
                .andExpect( status().isOk() )
                .andExpect( view().name( "userPosts" ) )
                .andExpect( model().attributeExists( "postCommand" ) )
                .andExpect( model().attribute( "postCommand", hasProperty("userId", is( USER1_ID ))))
                .andExpect( model().attributeExists( "user" ) )
                .andExpect( model().attributeExists( "page" ) );
    }

    @Test
    @Transactional
    @Rollback
    public void should_deletePost_when_PostIdIsValid_AndUserWhoCreatedPostIsLoggedIn() throws Exception {

        mockMvc.perform( get("/posts/14/delete").with( user( USER2_NAME ).password( USER2_PW ).roles( ROLE_USER ) ) )
                .andExpect( status().is3xxRedirection() )
                .andExpect( view().name( "redirect:/posts" ) );

    }

    @Test
    public void should_throw4xxException_when_userWhoDidNotCreateThePostTriesToDeleteIt() throws Exception {
        //user1 did NOT create post with id 14
        mockMvc.perform( get("/posts/14/delete").with( user( USER1_NAME ).password( USER1_PW ).roles( ROLE_USER ) ) )
                .andExpect( status().is4xxClientError() );

    }


}
