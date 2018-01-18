package com.blogen.controllers;

import com.blogen.commands.CategoryCommand;
import com.blogen.commands.PageCommand;
import com.blogen.commands.PostCommand;
import com.blogen.commands.UserCommand;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
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
 * MockMVC tests for PostController
 *
 * 
 * @author Cliff
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@WebAppConfiguration
public class PostControllerTest {

    @Autowired
    PostController controller;

    @Autowired
    WebApplicationContext webCtx;

    private MockMvc mockMvc;

    private static final String USER_NAME = "lizreed";
    private static final Long   USER_ID   = 5L;
    private static final String USER_PW   = "password";
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
    public void showAllPosts() throws Exception {
        
        mockMvc.perform( get("/posts").with( user(USER_NAME).password( USER_PW ).roles(ROLE_USER)) )
                .andExpect( status().isOk() )
                .andExpect( view().name( "userPosts" ) )
                .andExpect( model().attributeExists( "postCommand" ) )
                .andExpect( model().attribute( "postCommand", hasProperty("userId", is( USER_ID ))))
                .andExpect( model().attributeExists( "user" ) )
                .andExpect( model().attributeExists( "page" ) );
    }


}
