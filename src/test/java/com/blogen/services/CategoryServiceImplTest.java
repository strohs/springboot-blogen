package com.blogen.services;

import com.blogen.commands.CategoryCommand;
import com.blogen.commands.CategoryPageCommand;
import com.blogen.commands.mappers.CategoryCommandMapper;
import com.blogen.domain.Category;
import com.blogen.repositories.CategoryRepository;
import com.blogen.services.utils.PageRequestBuilder;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.*;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.junit.Assert.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.BDDMockito.willReturn;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
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
    private static final Long CAT4_ID = 4L;
    private static final String CAT1_NAME = "Business";
    private static final String CAT2_NAME = "Health & Fitness";
    private static final String CAT3_NAME = "Web Development";
    private static final String CAT4_NAME = "Gadgets";

    private static final int TOTAL_COUNT = 3;

    //unit under test
    CategoryService categoryService;

    @Mock
    CategoryRepository categoryRepository;

    @Mock
    PageRequestBuilder pageRequestBuilder;

    CategoryCommandMapper categoryCommandMapper = CategoryCommandMapper.INSTANCE;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks( this );
        categoryService = new CategoryServiceImpl( categoryRepository, CategoryCommandMapper.INSTANCE, pageRequestBuilder );
    }

    @Test
    public void should_ReturnThreeCategories_whenGettingAllCategories() {
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
    public void should_ReturnOneCategory_whenGettingACategoryByName() {
        Category cat1 = buildCategory( CAT1_ID, CAT1_NAME );

        given( categoryRepository.findByName( anyString() ) ).willReturn( cat1 );

        CategoryCommand command = categoryService.getCategoryByName( CAT1_NAME );

        then( categoryRepository ).should().findByName( anyString() );
        assertThat( command.getId(), is( CAT1_ID ) );
        assertThat( command.getName(), is(CAT1_NAME) );
    }


    @Test
    public void should_ReturnOneCategory_whenSearchForCategoryWithNameLike_Health() {
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
    public void should_ReturnTwoCategories_when_SearchingForCategoryWithNameLike() {
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

    @Test
    public void should_returnOnePageWithTwoCategories_when_getAllCategoriesByPage() {
        Category cat1 = buildCategory( CAT1_ID,CAT1_NAME );
        Category cat2 = buildCategory( CAT2_ID,CAT2_NAME );
        List<Category> categories = Arrays.asList( cat1,cat2 );
        Page<Category> page = new PageImpl<>( categories );
        PageRequest pageRequest = PageRequest.of( 0,4,Sort.Direction.ASC,"name" );

        given( pageRequestBuilder.buildCategoryPageRequest( anyInt(),any(),anyString() )).willReturn( pageRequest );
        given( categoryRepository.findAll( any( PageRequest.class ) )).willReturn( page );

        CategoryPageCommand categoryPageCommand = categoryService.getAllCategories( 0 );

        then( categoryRepository ).should().findAll( any(Pageable.class) );
        assertThat( categoryPageCommand, is(notNullValue()) );
        assertThat( categoryPageCommand.getCategories().size(), is(2));
        assertThat( categoryPageCommand.getTotalElements(), is(2L));

    }

//    @Test
//    public void should_returnTwoTotalPagesWithTwoCategoriesPerPage_when_getAllCategoriesByPage() {
//        Category cat1 = buildCategory( CAT1_ID,CAT1_NAME );
//        Category cat2 = buildCategory( CAT2_ID,CAT2_NAME );
//        Category cat3 = buildCategory( CAT3_ID,CAT3_NAME );
//        Category cat4 = buildCategory( CAT4_ID,CAT4_NAME );
//        List<Category> categories = Arrays.asList( cat1,cat2 );
//        PageRequest pageRequest = pageRequestBuilder.buildPageRequest( 0,2,Sort.Direction.ASC,"name" );
//        Page<Category> page = new PageImpl<>( categories );
//
//        given( categoryRepository.findAll( any( Pageable.class) )).willReturn( page );
//        given( pageRequestBuilder.buildCategoryPageRequest( anyInt(),any(Sort.Direction.class),anyString() )).willReturn( pageRequest );
//
//        CategoryPageCommand categoryPageCommand = categoryService.getAllCategories( 0 );
//
//        then( categoryRepository ).should().findAll( any(Pageable.class) );
//        assertThat( categoryPageCommand, is(notNullValue()) );
//        assertThat( categoryPageCommand.getCategories().size(), is(2));
//        assertThat( categoryPageCommand.getTotalElements(), is(2L));
//        assertThat( categoryPageCommand.getTotalPages(), is(2));
//
//    }

    

    private Category buildCategory( Long id, String name) {
        Category cat = new Category();
        cat.setId( id );
        cat.setName( name );
        return cat;
    }

    private CategoryCommand buildCategoryCommand( Long id, String name) {
        CategoryCommand cat = new CategoryCommand();
        cat.setId( id );
        cat.setName( name );
        return cat;
    }

    private CategoryPageCommand buildCategoryPageCommand( int reqPage, long totalElements, int totalPages, List<CategoryCommand> cc ) {
        CategoryPageCommand cpc = new CategoryPageCommand();
        cpc.setTotalPages( totalPages );
        cpc.setTotalElements( totalElements );
        cpc.setRequestedPage( reqPage );
        cpc.setCategories( cc );
        return cpc;
    }
}