package com.blogen.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * MockMVC integration tests for PostController
 *
 * @author Cliff
 */
@SpringBootTest
@WebAppConfiguration
public class PostControllerIntTest {

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
    private static final String ADMIN_NAME = "admin";
    private static final String ADMIN_PW   = "adminpassword";
    private static final Long   ADMIN_ID   = 1L;
    private static final String ROLE_USER  = "USER";
    private static final String ROLE_ADMIN = "ADMIN";
    private final SimpleGrantedAuthority AUTH_ADMIN = new SimpleGrantedAuthority( "ADMIN" );
    private final SimpleGrantedAuthority AUTH_USER  = new SimpleGrantedAuthority( "USER" );

    @BeforeEach
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks( this );
        mockMvc = MockMvcBuilders
                .webAppContextSetup( webCtx )
                .apply( springSecurity() )
                .build();
    }

    @Test
    public void should_showAllPosts() throws Exception {
        
        mockMvc.perform( get("/posts").with( user( USER1_NAME ).password( USER1_PW ).authorities( AUTH_USER )) )
                .andExpect( status().is3xxRedirection() )
                .andExpect( view().name( "redirect:/posts/show?cat=0&page=0" ) );
    }

    @Test
    public void should_showPostsByPage() throws Exception {

        mockMvc.perform( get("/posts/show?cat=1&page=0").with( user(USER1_NAME).password( USER1_PW ).authorities( AUTH_USER )) )
                .andExpect( status().isOk() )
                .andExpect( view().name( "mainPosts" ) )
                .andExpect( model().attributeExists( "postCommand" ) )
                .andExpect( model().attributeExists( "user" ) )
                .andExpect( model().attributeExists( "page" ) );
    }

    @Test
    public void should_returnNoPosts_when_showingPageThatDoesNotExist() throws Exception {

        mockMvc.perform( get("/posts/show?cat=0&page=9999").with( user(USER1_NAME).password( USER1_PW ).authorities( AUTH_USER )) )
                .andExpect( status().isOk() )
                .andExpect( model().attributeExists( "postCommand" ) )
                .andExpect( model().attributeExists( "user" ) )
                .andExpect( model().attributeExists( "page" ) )
                .andExpect( model().attribute( "page", hasProperty( "posts", hasSize( 0 ) ) ) );

    }

    @Test
    public void should_return4xxError_when_requestingCategoryThatDoesNotExist() throws Exception {
        mockMvc.perform( get("/posts/show?cat=9999&page=1").with( user(USER1_NAME).password( USER1_PW ).authorities( AUTH_USER )) )
                .andExpect( status().is4xxClientError() )
                .andExpect( view().name( "404error" ) );

    }

    @Test
    @Transactional
    @Rollback
    public void should_deletePost_when_PostIdIsValid_AndUserWhoCreatedPostIsLoggedIn() throws Exception {

        mockMvc.perform( get("/posts/14/delete").with( user( USER2_NAME ).password( USER2_PW ).authorities( AUTH_USER ) ) )
                .andExpect( status().is3xxRedirection() )
                .andExpect( view().name( "redirect:/posts" ) );

    }

    @Test
    @Transactional
    @Rollback
    public void should_deletePost_when_adminIsPrincipalAndDeletesThePost() throws Exception {

        mockMvc.perform( get("/posts/14/delete").with( user( ADMIN_NAME ).password( ADMIN_PW ).authorities( AUTH_ADMIN ) ) )
                .andExpect( status().is3xxRedirection() )
                .andExpect( view().name( "redirect:/posts" ) );

    }

    @Test
    @Transactional
    @Rollback
    public void should_throw4xxException_when_userWhoDidNotCreateThePostTriesToDeleteIt() throws Exception {
        //user1 did NOT create post with id 14
        mockMvc.perform( get("/posts/14/delete").with( user( USER1_NAME ).password( USER1_PW ).authorities( AUTH_USER ) ) )
                .andExpect( status().is4xxClientError() );
    }

    @Test
    @Transactional
    @Rollback
    @WithMockUser( username = USER2_NAME, password = USER2_PW, authorities = {"USER"})
    public void should_updatedPostDataAndRedirect_when_userThatCreatedPostUpdatesIt() throws Exception {
        //the current page being viewed
        String page = "0";
        mockMvc.perform( post("/posts/update")
                .param( "page", page )
                .param( "id","11" )
                .param( "userName",USER2_NAME )
                .param( "title","title" )
                .param("text","updated text")
                .param("imageUrl","http://text.com")
                .param("categoryName","Business")
        )
                .andExpect( status().is3xxRedirection() )
                .andExpect( view().name( "redirect:/posts/show?cat=0&page="+page ) );
    }

    @Test
    @Transactional
    @Rollback
    @WithMockUser( username = ADMIN_NAME, password = ADMIN_PW, authorities = {"ADMIN"})
    public void should_allowAdminToEditPost_when_updatingExistingPostId() throws Exception {
        //the current page being viewed
        String page = "0";
        mockMvc.perform( post("/posts/update")
                .param( "page", page )
                .param( "id","14" )
                .param( "userName",ADMIN_NAME )
                .param( "title","title" )
                .param("text","updated text")
                .param("imageUrl","http://text.com")
                .param("categoryName","Business")
        )
                .andExpect( status().is3xxRedirection() )
                .andExpect( view().name( "redirect:/posts/show?cat=0&page="+page ) );
    }

    @Test
    public void should_throw4xxException_when_userWhoDidNotCreateThePostTriesUpdateIt() throws Exception {
        //user1 did NOT create post with id 14
        mockMvc.perform( post("/posts/update")
                .param( "id","1" )
                .param( "userName",USER2_NAME )
                .param( "title","title" )
                .param("text","updated text")
                .param("imageUrl","http://text.com")
                .param("categoryName","Business")
                .with( user( USER1_NAME ).password( USER1_PW ).authorities( AUTH_USER ) ) )
                .andExpect( status().is4xxClientError() );
    }
}
