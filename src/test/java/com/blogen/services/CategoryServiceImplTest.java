package com.blogen.services;

import com.blogen.commands.CategoryCommand;
import com.blogen.commands.mappers.CategoryCommandMapper;
import com.blogen.domain.Category;
import com.blogen.repositories.CategoryRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.junit.Assert.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Matchers.anyString;

/**
 * Unit Tests for {@link CategoryServiceImpl}
 *
 * @author Cliff
 */
public class CategoryServiceImplTest {

    private static final Long CAT1_ID = 1L;
    private static final Long CAT2_ID = 2L;
    private static final Long CAT3_ID = 3L;
    private static final String CAT1_NAME = "Business";
    private static final String CAT2_NAME = "Health & Fitness";
    private static final String CAT3_NAME = "Web Development";

    private static final int TOTAL_COUNT = 3;

    //unit under test
    CategoryService categoryService;

    @Mock
    CategoryRepository categoryRepository;

    CategoryCommandMapper categoryCommandMapper = CategoryCommandMapper.INSTANCE;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks( this );
        categoryService = new CategoryServiceImpl( categoryRepository, CategoryCommandMapper.INSTANCE );
    }

    @Test
    public void shouldReturnThreeCategories_whenGettingAllCategories() {
        Category cat1 = buildCategory( CAT1_ID, CAT1_NAME );
        Category cat2 = buildCategory( CAT2_ID, CAT2_NAME );
        Category cat3 = buildCategory( CAT3_ID, CAT3_NAME );
        List<Category> categories = Arrays.asList( cat1, cat2, cat3 );

        given( categoryRepository.findAll() ).willReturn( categories );

        //when
        List<CategoryCommand> commands = categoryService.getAllCategories();

        //then
        then( categoryRepository).should().findAll();
        assertThat( commands.size(), is(3) );
    }

    @Test
    public void shouldReturnOneCategory_whenGettingACategoryByName() {
        Category cat1 = buildCategory( CAT1_ID, CAT1_NAME );

        given( categoryRepository.findByName( anyString() ) ).willReturn( cat1 );

        CategoryCommand command = categoryService.getCategoryByName( CAT1_NAME );

        then( categoryRepository ).should().findByName( anyString() );
        assertThat( command.getId(), is( CAT1_ID ) );
        assertThat( command.getName(), is(CAT1_NAME) );
    }


    @Test
    public void shouldReturnOneCategory_whenSearchForCategoryWithNameLike_Health() {
        Category cat1 = buildCategory( CAT2_ID, CAT2_NAME );
        String searchStr = "Health";
        List<Category> categories = Arrays.asList( cat1 );

        given( categoryRepository.findByNameIgnoreCaseContaining( anyString() ) ).willReturn( categories );

        List<CategoryCommand> command = categoryService.getCategoryByNameLike( searchStr );

        then( categoryRepository ).should().findByNameIgnoreCaseContaining( anyString() );
        assertThat( command.size(), is(1));
        assertThat( command.get( 0 ).getName().toUpperCase().contains( searchStr.toUpperCase() ), is( true ) );
    }

    @Test
    public void shouldReturnTwoCategories_whenSearchingForCategoryWithNameLike_ness() {
        Category cat1 = buildCategory( CAT1_ID, CAT1_NAME );
        Category cat2 = buildCategory( CAT2_ID, CAT2_NAME );
        String searchStr = "ness";
        List<Category> categories = Arrays.asList( cat1,cat2 );

        given( categoryRepository.findByNameIgnoreCaseContaining( anyString() ) ).willReturn( categories );

        List<CategoryCommand> commands = categoryService.getCategoryByNameLike( searchStr );

        then( categoryRepository ).should().findByNameIgnoreCaseContaining( anyString() );
        assertThat( commands.size(), is(2));
        assertThat( commands.get( 0 ).getName().toUpperCase().contains( searchStr.toUpperCase() ), is( true ) );
        assertThat( commands.get( 1 ).getName().toUpperCase().contains( searchStr.toUpperCase() ), is( true ) );
    }

    private Category buildCategory( Long id, String name) {
        Category cat = new Category();
        cat.setId( id );
        cat.setName( name );
        return cat;
    }
}