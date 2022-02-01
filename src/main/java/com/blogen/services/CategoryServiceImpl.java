package com.blogen.services;

import com.blogen.api.v1.model.CategoryDTO;
import com.blogen.commands.CategoryCommand;
import com.blogen.commands.CategoryPageCommand;
import com.blogen.commands.mappers.CategoryCommandMapper;
import com.blogen.domain.Category;
import com.blogen.repositories.CategoryRepository;
import com.blogen.services.utils.PageRequestBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service for CRUD ops on Blogen {@link Category} data
 *
 * Author: Cliff
 */
@Service
public class CategoryServiceImpl implements CategoryService {

    private CategoryRepository categoryRepository;
    private CategoryCommandMapper categoryCommandMapper;
    private PageRequestBuilder pageRequestBuilder;

    @Autowired
    public CategoryServiceImpl( CategoryRepository categoryRepository, CategoryCommandMapper categoryCommandMapper,
                                PageRequestBuilder pageRequestBuilder ) {
        this.categoryRepository = categoryRepository;
        this.categoryCommandMapper = categoryCommandMapper;
        this.pageRequestBuilder = pageRequestBuilder;
    }

    /**
     * gets all Blogen {@link Category} from the database and maps them to {@link CategoryDTO}
     *
     * @return a List of CategoryCommands
     */
    @Override
    public List<CategoryCommand> getAllCategories() {
         return categoryRepository.findAll()
                 .stream()
                 .map( categoryCommandMapper::categoryToCategoryCommand )
                 .collect( Collectors.toList());
    }

    /**
     *
     * @param pageNum the page of category data to retrieve
     * @return a CategoryPageCommand containing a {@link org.springframework.data.domain.Page} of {@link Category}
     */
    @Override
    public CategoryPageCommand getAllCategories( int pageNum ) {
        CategoryPageCommand command = new CategoryPageCommand();
        List<CategoryCommand> categoryCommands = new ArrayList<>();
        //build a PageRequest for Category data

        PageRequest pageRequest = pageRequestBuilder.buildCategoryPageRequest( pageNum, Sort.Direction.ASC, "name" );
        //retrieve a page worth of categories
        Page<Category> page = categoryRepository.findAll( pageRequest );
        page.forEach( category -> categoryCommands.add( categoryCommandMapper.categoryToCategoryCommand( category ) ) );
        //build the page command object
        command.setCategories( categoryCommands );
        command.setRequestedPage( pageNum );
        command.setTotalElements( page.getTotalElements() );
        command.setTotalPages( page.getTotalPages() );
        return command;
    }


    /**
     * get a Blogen {@link Category} by name
     * @param name - the Category.name to search for
     * @return a {@link CategoryDTO} containing the Category data
     */
    @Override
    public CategoryCommand getCategoryByName( String name ) {
        return categoryCommandMapper.categoryToCategoryCommand( categoryRepository.findByName( name ) );
    }

    /**
     * get all categories with the specified subString in their name
     * @param subStr the sub-string to search for in {@link Category} name should not contain wildcard characters (e.g. %)
     * @return a List of {@link CategoryCommand} containing the subString in their name property
     */
    @Override
    public List<CategoryCommand> getCategoryByNameLike( String subStr ) {
        return categoryRepository.findByNameIgnoreCaseContaining( subStr )
                .stream()
                .map( categoryCommandMapper::categoryToCategoryCommand )
                .collect( Collectors.toList());
    }

    /**
     * saves a new {@link Category} into the Database.
     * Only ADMINs are allowed to add new categories.
     *
     * @param command command object of Category data to create in the DB
     * @return {@link CategoryCommand} representing the newly saved category
     */
    @Override
    @PreAuthorize( "hasAuthority('ADMIN')" )
    public CategoryCommand addCategoryByCategoryCommand( CategoryCommand command ) {
        Category categoryToSave = categoryCommandMapper.categoryCommandToCategory( command );
        Category savedCategory = categoryRepository.save( categoryToSave );
        return categoryCommandMapper.categoryToCategoryCommand( savedCategory );
    }
}
