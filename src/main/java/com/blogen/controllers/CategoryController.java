package com.blogen.controllers;

import com.blogen.commands.CategoryCommand;
import com.blogen.commands.CategoryPageCommand;
import com.blogen.services.CategoryService;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author Cliff
 */
@Log4j
@Controller
public class CategoryController {

    private CategoryService categoryService;

    @Autowired
    public CategoryController( CategoryService categoryService ) {
        this.categoryService = categoryService;
    }

    @GetMapping("/admin/categories/show")
    public String showCategoriesByPage( @RequestParam("page") int pageNum, Model model) {
        log.debug( "show categories for page:" + pageNum );
        CategoryPageCommand command = categoryService.getAllCategories( pageNum );

        model.addAttribute( "page",command );
        model.addAttribute( "categoryCommand",new CategoryCommand() );
        return "/admin/categories";
    }

    @PostMapping("/admin/categories")
    public String addNewCategory( @ModelAttribute CategoryCommand categoryCommand ) {
        log.debug( "posted new category: " + categoryCommand );
        CategoryCommand savedCategory = categoryService.addCategoryByCategoryCommand( categoryCommand );
        return "redirect:/admin/categories/show?page=0";
    }
}
