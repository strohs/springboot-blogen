package com.blogen.controllers;

import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

/**
 * Unit tests for {@link IndexController}
 *
 * @author Cliff
 */
public class IndexControllerTest {

    IndexController indexController;

    MockMvc mockMvc;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks( this );

        indexController = new IndexController();
        mockMvc = MockMvcBuilders.standaloneSetup( indexController ).build();
    }

    @Test
    public void showIndex() throws Exception {

        mockMvc.perform( get( "/") )
                .andExpect( status().isOk() )
                .andExpect( view().name("index") );
    }
}