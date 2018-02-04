package com.blogen.api.v1.services;

import com.blogen.api.v1.controllers.CategoryRestController;
import com.blogen.api.v1.mappers.CategoryMapper;
import com.blogen.api.v1.model.CategoryDTO;
import com.blogen.api.v1.model.CategoryListDTO;
import com.blogen.domain.Category;
import com.blogen.exceptions.BadRequestException;
import com.blogen.repositories.CategoryRepository;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * CategoryService for REST API
 * For Categories:
 *   we can get a list of all categories
 *   get a specific category
 *   create a new category
 *
 * Deleting or Updating a category is not supported in this API
 * @author Cliff
 */
@Service("categoryRestService")
@Log4j
public class CategoryServiceImpl implements CategoryService {

    private CategoryRepository categoryRepository;
    private CategoryMapper categoryMapper;

    @Autowired
    public CategoryServiceImpl( CategoryRepository categoryRepository, CategoryMapper categoryMapper ) {
        this.categoryRepository = categoryRepository;
        this.categoryMapper = categoryMapper;
    }

    @Override
    public CategoryListDTO getAllCategories() {
        List<Category> categories = categoryRepository.findAll();
        List<CategoryDTO> dtos = categories
                .stream()
                .map( category -> {
                    CategoryDTO dto = categoryMapper.categoryToCategoryDto( category );
                    dto.setCategoryUrl( buildCategoryUrl( category ) );
                    return dto;
                } ).collect( Collectors.toList());
        return new CategoryListDTO( dtos );
    }

    @Override
    public CategoryDTO getCategory( Long id ) {
        Category category = categoryRepository.findOne( id );
        if ( category == null ) throw new BadRequestException( "category with id: " + id + " does not exist" );
        CategoryDTO categoryDTO = categoryMapper.categoryToCategoryDto( category );
        categoryDTO.setCategoryUrl( buildCategoryUrl( category ) );
        return categoryDTO;
    }

    @Override
    public CategoryDTO createNewCategory( CategoryDTO categoryDTO ) {
        Category categoryToSave = categoryMapper.categoryDtoToCategory( categoryDTO );
        Category savedCategory = categoryRepository.save( categoryToSave );
        CategoryDTO savedDTO = categoryMapper.categoryToCategoryDto( savedCategory );
        savedDTO.setCategoryUrl( buildCategoryUrl( savedCategory ) );
        return savedDTO;
    }



    private String buildCategoryUrl( Category category ) {
        return CategoryRestController.BASE_URL + "/" + category.getId();
    }
}
