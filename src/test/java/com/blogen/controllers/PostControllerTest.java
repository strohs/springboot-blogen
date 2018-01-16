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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * MockMVC tests for PostController
 *
 * NOTE: could not get MockMvcBuilders.standAloneSetup to work with Thymleaf 3. Kept throwing Circular Path errors.
 * Possible bug in thymeleaf/spring boot view resolvers. Had to resort to using full on WebApplicationContext setup.
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


    MockMvc mockMvc;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks( this );
        mockMvc = MockMvcBuilders.webAppContextSetup( webCtx ).build();
    }

    @Test
    public void showAllPosts() throws Exception {
        PageCommand page = new PageCommand();
        UserCommand user = new UserCommand();
        CategoryCommand category = new CategoryCommand();
        category.setId( 1L );
        category.setName( "Category" );
        Long userId = 2L;
        List<CategoryCommand> categories = new ArrayList<>();
        PostCommand post1 = new PostCommand();
        PostCommand post2 = new PostCommand();
        List<PostCommand> posts = Arrays.asList( post1,post2 );
        page.setCategories( categories );
        page.setPosts( posts );

//        given( postService.getAllPostsByUserForPage( anyLong(),anyInt() ) ).willReturn( page );
//        given( categoryService.getAllCategories() ).willReturn( categories );
//        //todo this will prob be removed with Spring Security
//        given( userService.getUserByUserName( anyString() )).willReturn( user );

        mockMvc.perform( get("/posts") )
                .andExpect( status().isOk() )
                .andExpect( view().name( "posts" ) )
                .andExpect( model().attributeExists( "postCommand" ) )
                .andExpect( model().attributeExists( "user" ) )
                .andExpect( model().attributeExists( "page" ) );

    }


}
