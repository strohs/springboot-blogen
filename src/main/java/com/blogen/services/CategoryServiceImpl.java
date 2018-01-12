package com.blogen.services;

import com.blogen.api.v1.mappers.CategoryMapper;
import com.blogen.api.v1.model.CategoryDTO;
import com.blogen.commands.CategoryCommand;
import com.blogen.commands.mappers.CategoryCommandMapper;
import com.blogen.domain.Category;
import com.blogen.repositories.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    @Autowired
    public CategoryServiceImpl( CategoryRepository categoryRepository, CategoryCommandMapper categoryCommandMapper ) {
        this.categoryRepository = categoryRepository;
        this.categoryCommandMapper = categoryCommandMapper;
    }

    /**
     * gets all Blogen {@link Category} from the database and maps them to {@link CategoryDTO}
     *
     * @return a List of CategoryDTOS
     */
    @Override
    public List<CategoryCommand> getAllCategories() {
         return categoryRepository.findAll()
                 .stream()
                 .map( categoryCommandMapper::categoryToCategoryCommand )
                 .collect( Collectors.toList());
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
     * @return a List of {@link CategoryDTO} containing the subString in their name property
     */
    @Override
    public List<CategoryCommand> getCategoryByNameLike( String subStr ) {
        return categoryRepository.findByNameIgnoreCaseContaining( subStr )
                .stream()
                .map( categoryCommandMapper::categoryToCategoryCommand )
                .collect( Collectors.toList());
    }
}
