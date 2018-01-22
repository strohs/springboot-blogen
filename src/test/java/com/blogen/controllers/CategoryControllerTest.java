package com.blogen.controllers;

import com.blogen.commands.CategoryCommand;
import com.blogen.commands.CategoryPageCommand;
import com.blogen.services.CategoryService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

/**
 * Unit Tests for Category Controller
 *
 * @author Cliff
 */
public class CategoryControllerTest {

    private CategoryController controller;

    @Mock
    private CategoryService categoryService;

    MockMvc mockMvc;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks( this );
        controller = new CategoryController( categoryService );

        mockMvc = MockMvcBuilders.standaloneSetup( controller )
                .setControllerAdvice( new ControllerExceptionHandler() )
                .build();
    }

    @Test
    public void should_showCategoriesByPage() throws Exception {
        CategoryCommand cc1 = buildCategoryCommand( 1L, "Category 1" );
        CategoryCommand cc2 = buildCategoryCommand( 2L,"Category 2" );
        CategoryPageCommand pageCommand = buildCategoryPageCommand( 0,1, Arrays.asList(cc1,cc2) );

        given( categoryService.getAllCategories( anyInt() )).willReturn( pageCommand );

        mockMvc.perform( get("/admin/categories/show?page=0") )
                .andExpect( status().isOk() )
                .andExpect( view().name( "/admin/categories" ) )
                .andExpect( model().attributeExists( "page" ) )
                .andExpect( model().attributeExists( "categoryCommand" ) );

    }

    @Test
    public void should_show400ErrorPage_when_malformedPageParameter() throws Exception {
        //malformed page
        String page = "55ls";
        CategoryCommand cc1 = buildCategoryCommand( 1L, "Category 1" );
        CategoryCommand cc2 = buildCategoryCommand( 2L,"Category 2" );
        CategoryPageCommand pageCommand = buildCategoryPageCommand( 0,1, Arrays.asList(cc1,cc2) );

        given( categoryService.getAllCategories( anyInt() )).willReturn( pageCommand );

        mockMvc.perform( get("/admin/categories/show?page=" + page ) )
                .andExpect( status().isBadRequest() )
                .andExpect( view().name( "400error" ) );
    }

    @Test
    public void should_postCategoryDataAndRedirect_when_addNewCategory() throws Exception {
        CategoryCommand savedCommand = buildCategoryCommand( 3L,"New Category" );

        given( categoryService.addCategoryByCategoryCommand( any(CategoryCommand.class) ) ).willReturn( savedCommand );

        mockMvc.perform( post("/admin/categories")
            .contentType( MediaType.APPLICATION_FORM_URLENCODED )
            .param( "id","3" )
            .param("name","New Category")
        )
            .andExpect( status().is3xxRedirection() )
            .andExpect( view().name( "redirect:/admin/categories/show?page=0" ) );
    }


    private CategoryCommand buildCategoryCommand( Long id, String name ) {
        CategoryCommand command = new CategoryCommand();
        command.setId( id );
        command.setName( name );
        return command;
    }

    private CategoryPageCommand buildCategoryPageCommand( int pageNum, int totalPages, List<CategoryCommand> categories ) {
        CategoryPageCommand command = new CategoryPageCommand();
        command.setCategories( categories );
        command.setRequestedPage( pageNum );
        command.setTotalPages( totalPages );
        return command;
    }
}
