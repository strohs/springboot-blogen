package com.blogen.api.v1.controllers;

import com.blogen.api.v1.validators.CategoryDtoValidator;
import com.blogen.api.v1.services.CategoryService;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller for REST operations on {@link com.blogen.domain.Category}
 *
 * @author Cliff
 */
@RestController
@Log4j
public class CategoryRestController {

    public static final String BASE_URL = "/api/v1/categories";

    private CategoryService categoryService;
    private CategoryDtoValidator categoryDtoValidator;

    @Autowired
    public CategoryRestController( @Qualifier("categoryRestService") CategoryService categoryService,
                                   CategoryDtoValidator categoryDtoValidator ) {
        this.categoryService = categoryService;
        this.categoryDtoValidator = categoryDtoValidator;
    }

    @InitBinder("categoryDTO")
    public void setUpBinder( WebDataBinder binder ) {
        binder.addValidators( categoryDtoValidator );
    }
}
