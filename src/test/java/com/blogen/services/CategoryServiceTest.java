package com.blogen.services;

import com.blogen.api.v1.mapper.CategoryMapper;
import com.blogen.api.v1.model.CategoryDTO;
import com.blogen.domain.Category;
import com.blogen.repositories.CategoryRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;

/**
 * Unit Tests for the {@link CategoryService}
 *
 * @author Cliff
 */
public class CategoryServiceTest {

    private static final Long ID1 = 22L;
    private static final Long ID2 = 33L;
    private static final String NAME1 = "Health & Wellness";
    private static final String NAME2 = "Business";

    private static final int TOTAL_COUNT = 2;

    //unit under test
    CategoryService categoryService;

    @Mock
    CategoryRepository categoryRepository;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks( this );
        categoryService = new CategoryServiceImpl( categoryRepository, CategoryMapper.INSTANCE );
    }

    @Test
    public void getAllCategories_shouldReturnTwoCategories() {
        List<Category> categories = Arrays.asList( new Category(), new Category() );

        given( categoryRepository.findAll() ).willReturn( categories );

        //when
        List<CategoryDTO> categoryDTOS = categoryService.getAllCategories();

        //then
        assertThat( categoryDTOS.size(), is(2) );
    }

    @Test
    public void getCategoryByName_shouldReturnACategory() {
        Category cat1 = new Category();
        cat1.setName( NAME1 );
        cat1.setId( ID1 );

        given( categoryRepository.findByName( anyString() ) ).willReturn( cat1 );

        CategoryDTO returnedCatDTO = categoryService.getCategoryByName( NAME1 );

        assertThat( returnedCatDTO.getId(), is( ID1) );
        assertThat( returnedCatDTO.getName(), is(NAME1) );
    }
    

    @Test
    public void getCategoryByNameLike_shouldReturnOneCategory() {
        Category cat1 = new Category();
        cat1.setName( NAME1 );
        cat1.setId( ID1 );
        String searchStr = "Health";
        List<Category> categories = Arrays.asList( cat1 );

        given( categoryRepository.findByNameIgnoreCaseContaining( anyString() ) ).willReturn( categories );

        List<CategoryDTO> categoryDTOS = categoryService.getCategoryByNameLike( searchStr );

        assertThat( categoryDTOS.size(), is(1));
        assertThat( categoryDTOS.get( 0 ).getName().toUpperCase().contains( searchStr.toUpperCase() ), is( true ) );
    }

    @Test
    public void getCategoryByNameLike_shouldReturnTwoCategories() {
        Category cat1 = new Category();
        cat1.setName( NAME1 );
        cat1.setId( ID1 );
        Category cat2 = new Category();
        cat2.setId( ID2 );
        cat2.setName( NAME2 );
        String searchStr = "ness";
        List<Category> categories = Arrays.asList( cat1,cat2 );

        given( categoryRepository.findByNameIgnoreCaseContaining( anyString() ) ).willReturn( categories );

        List<CategoryDTO> categoryDTOS = categoryService.getCategoryByNameLike( searchStr );

        assertThat( categoryDTOS.size(), is(2));
        assertThat( categoryDTOS.get( 0 ).getName().toUpperCase().contains( searchStr.toUpperCase() ), is( true ) );
        assertThat( categoryDTOS.get( 1 ).getName().toUpperCase().contains( searchStr.toUpperCase() ), is( true ) );
    }
}