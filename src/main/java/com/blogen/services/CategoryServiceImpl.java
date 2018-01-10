package com.blogen.services;

import com.blogen.api.v1.mappers.CategoryMapper;
import com.blogen.api.v1.model.CategoryDTO;
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
    private CategoryMapper categoryMapper;

    @Autowired
    public CategoryServiceImpl( CategoryRepository categoryRepository, CategoryMapper categoryMapper ) {
        this.categoryRepository = categoryRepository;
        this.categoryMapper = categoryMapper;
    }

    /**
     * gets all Blogen {@link Category} from the database and maps them to {@link CategoryDTO}
     *
     * @return a List of CategoryDTOS
     */
    @Override
    public List<CategoryDTO> getAllCategories() {
         return categoryRepository.findAll()
                 .stream()
                 .map( categoryMapper::categoryToCategoryDto )
                 .collect( Collectors.toList());
    }

    /**
     * get a Blogen {@link Category} by name
     * @param name - the Category.name to search for
     * @return a {@link CategoryDTO} containing the Category data
     */
    @Override
    public CategoryDTO getCategoryByName( String name ) {
        return categoryMapper.categoryToCategoryDto( categoryRepository.findByName( name ) );
    }

    /**
     * get all categories with the specified subString in their name
     * @param subStr the sub-string to search for in {@link Category} name should not contain wildcard characters (e.g. %)
     * @return a List of {@link CategoryDTO} containing the subString in their name property
     */
    @Override
    public List<CategoryDTO> getCategoryByNameLike( String subStr ) {
        return categoryRepository.findByNameIgnoreCaseContaining( subStr )
                .stream()
                .map( categoryMapper::categoryToCategoryDto )
                .collect( Collectors.toList());
    }
}
