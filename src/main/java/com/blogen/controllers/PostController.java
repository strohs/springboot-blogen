package com.blogen.controllers;

import com.blogen.commands.CategoryCommand;
import com.blogen.commands.PageCommand;
import com.blogen.commands.UserCommand;
import com.blogen.services.CategoryService;
import com.blogen.services.PostService;
import com.blogen.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

/**
 * Controller for the Blogen posts page.
 *
 * @author Cliff
 */
@Controller
public class PostController {

    private PostService postService;
    private UserService userService;
    private CategoryService categoryService;

    @Autowired
    public PostController( PostService postService, UserService userService, CategoryService categoryService ) {
        this.postService = postService;
        this.userService = userService;
        this.categoryService = categoryService;
    }

    //addNewPost - create a new post - POST - /posts
    // need to check if PostCommand has parentId set in order to determine if this is a child post

    //delete a post - GET - /posts/{id}/delete

    //editPost - POST - /posts/{id}/edit  - edit an existing post

    //showPost - GET - /posts/{id} - show a specific post

    //showPostByCategory - GET - /posts/category/{id}

    //searchForPosts - POST - /posts/search  form param is 'searchText'

    //showAllPosts - GET /post - get all posts in descending order by date posted, my need to implement paging logic
    @GetMapping("/posts")
    public String showAllPosts( Model model ) {
        //todo hardcode userID for now, replace with spring security
        String userName = "johndoe";
        UserCommand userCommand = userService.getUserByUserName( userName );
        PageCommand pageCommand = postService.getAllPostsByUserForPage( userCommand.getId(), 0 );
        List<CategoryCommand> categories = categoryService.getAllCategories();
        pageCommand.setCategories( categories );
        model.addAttribute( "page",pageCommand );
        model.addAttribute( "user",userCommand );

        return "posts";
    }


}
