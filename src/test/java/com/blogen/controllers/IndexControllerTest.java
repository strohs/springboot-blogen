package com.blogen.controllers;

import com.blogen.commands.PostCommand;
import com.blogen.domain.Post;
import com.blogen.services.PostService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

/**
 * Unit tests for {@link IndexController}
 *
 * @author Cliff
 */
public class IndexControllerTest {

    IndexController indexController;

    @Mock
    PostService postService;

    MockMvc mockMvc;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks( this );
        indexController = new IndexController( postService );
        mockMvc = MockMvcBuilders.standaloneSetup( indexController ).build();
    }

    @Test
    public void showIndex() throws Exception {
        PostCommand post1 = new PostCommand();
        PostCommand post2 = new PostCommand();
        PostCommand post3 = new PostCommand();
        List<PostCommand> commands = Arrays.asList( post1,post2,post3);

        given( postService.getTenRecentPosts() ).willReturn( commands );

        mockMvc.perform( get( "/") )
                .andExpect( status().isOk() )
                .andExpect( view().name("index") )
                .andExpect( model().attributeExists( "posts" ) );
    }
}