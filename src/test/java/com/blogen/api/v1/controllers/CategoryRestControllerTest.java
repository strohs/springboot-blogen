package com.blogen.api.v1.controllers;

import com.blogen.api.v1.model.CategoryDTO;
import com.blogen.api.v1.model.CategoryListDTO;
import com.blogen.api.v1.services.CategoryService;
import com.blogen.api.v1.validators.CategoryDtoValidator;
import com.blogen.exceptions.BadRequestException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;

import static com.blogen.api.v1.controllers.AbstractRestControllerTest.asJsonString;
import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Integration Tests for CategoryRestController
 * @author Cliff
 */
@WebMvcTest(controllers = {CategoryRestController.class})
@Import(CategoryDtoValidator.class)
public class CategoryRestControllerTest {

    @MockBean
    @Qualifier("categoryRestService")
    private CategoryService categoryService;

    @Autowired
    MockMvc mockMvc;

    private CategoryDTO catDto_1;
    private CategoryDTO catDto_2;
    private CategoryDTO newCatDto;

    @BeforeEach
    public void setUp() throws Exception {
        catDto_1 = new CategoryDTO( "Category1", CategoryRestController.BASE_URL + "/1" );
        catDto_2 = new CategoryDTO( "Category2",null );
        newCatDto = new CategoryDTO( "Category2",CategoryRestController.BASE_URL + "/2" );
    }

    @Test
    public void should_getAllCategoriesAndReturnOK_when_getAllCategories() throws Exception {
        CategoryListDTO categoryListDTO = new CategoryListDTO( Arrays.asList( catDto_1, catDto_2) );

        given( categoryService.getAllCategories() ).willReturn( categoryListDTO );

        mockMvc.perform( get( CategoryRestController.BASE_URL ) )
                .andExpect( status().isOk() )
                .andExpect( jsonPath( "$.categories", hasSize(2) ) );
    }

    @Test
    public void should_getOneCategoryWithCategoryUrl_when_getCategory() throws Exception {

        given( categoryService.getCategory( anyLong() )).willReturn( catDto_1 );

        mockMvc.perform( get( CategoryRestController.BASE_URL + "/1") )
                .andExpect( status().isOk() )
                .andExpect( jsonPath( "$.categoryUrl", is( catDto_1.getCategoryUrl() ) ) );
    }

    @Test
    public void should_returnHttpBadRequest_when_getCategoryWithInvalidId() throws Exception {

        given( categoryService.getCategory( anyLong() )).willThrow( new BadRequestException( "invalid category id" ) );

        mockMvc.perform( get( CategoryRestController.BASE_URL + "/56234") )
                .andExpect( status().isBadRequest() )
                .andExpect( jsonPath( "$.globalError[0].message", is(notNullValue()) ) );
    }

    @Test
    public void should_returnCreatedAndSetCategoryURL_when_createNewCategory() throws Exception {
        given( categoryService.createNewCategory( any(CategoryDTO.class) )).willReturn( newCatDto );

        mockMvc.perform( post( CategoryRestController.BASE_URL )
                .contentType( MediaType.APPLICATION_JSON )
                .content( asJsonString( catDto_2 ) )
        )
                .andExpect( status().isCreated() )
                .andExpect( jsonPath( "$.name", is("Category2") ) )
                .andExpect( jsonPath( "$.categoryUrl", is(newCatDto.getCategoryUrl()) ) );
    }

    @Test
    public void should_returnUnprocessableEntity_when_createNewCategory_withInvalidRequestParam() throws Exception {
        catDto_1.setName( null );

        given( categoryService.createNewCategory( any(CategoryDTO.class ) )).willReturn( newCatDto );

        mockMvc.perform( post( CategoryRestController.BASE_URL )
                .contentType( MediaType.APPLICATION_JSON )
                .content( asJsonString( catDto_1 ) )
        )
                .andExpect( status().isUnprocessableEntity() )
                .andExpect( jsonPath( "$.fieldError[0].message", not( isEmptyOrNullString() ) ) );
    }
}