package com.blogen.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Controller for the Blogen posts page.
 *
 * @author Cliff
 */
@Controller
public class PostController {

    //addNewPost - create a new post - POST - /post
    // need to check if PostCommand has parentId set in order to determine if this is a child post

    //delete a post - GET - /post/{id}/delete

    //editPost - POST - /post/{id}/edit  - edit an existing post

    //showPost - GET - /post/{id} - show a specific post

    //showAllPosts - GET /post - get all posts in descending order by date posted, my need to implement paging logic
    @GetMapping("/post")
    public String showAllPosts() {
        return "posts";
    }


}
