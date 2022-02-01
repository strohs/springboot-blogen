package com.blogen.commands.mappers;

import com.blogen.commands.CategoryCommand;
import com.blogen.domain.Category;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * Unit Test mapping to/from Category and CategoryCommand
 *
 * @author Cliff
 */
public class CategoryCommandMapperTest {

    private static final Long CAT_ID = 1L;
    private static final String CAT_NAME = "Some Category";

    CategoryCommandMapper categoryCommandMapper = CategoryCommandMapper.INSTANCE;

    @BeforeEach
    public void setUp() throws Exception {
    }

    @Test
    public void categoryToCategoryCommand() {
        Category cat = new Category();
        cat.setId( CAT_ID );
        cat.setName( CAT_NAME );

        CategoryCommand categoryCommand = categoryCommandMapper.categoryToCategoryCommand( cat );

        assertNotNull( categoryCommand );
        assertThat( categoryCommand.getId(), is( CAT_ID) );
        assertThat( categoryCommand.getName(), is( CAT_NAME) );
    }

    @Test
    public void categoryCommandToCategory() {
        CategoryCommand cc = new CategoryCommand();
        cc.setId( CAT_ID );
        cc.setName( CAT_NAME );

        Category category = categoryCommandMapper.categoryCommandToCategory( cc );

        assertNotNull( category );
        assertThat( category.getId(), is( CAT_ID) );
        assertThat( category.getName(), is( CAT_NAME ) );
    }
}