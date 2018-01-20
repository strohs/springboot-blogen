package com.blogen.controllers;

import com.blogen.commands.PageCommand;
import com.blogen.commands.PostCommand;
import com.blogen.commands.UserCommand;
import com.blogen.domain.User;
import com.blogen.services.CategoryService;
import com.blogen.services.PageRequestBuilder;
import com.blogen.services.PostService;
import com.blogen.services.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

;

/**
 * Controller for the Blogen "posts" page.
 *
 * @author Cliff
 */
@Slf4j
@Controller
public class PostController {

    private PostService postService;
    private UserService userService;

    @Autowired
    public PostController( PostService postService, UserService userService ) {
        this.postService = postService;
        this.userService = userService;
    }


    //editPost - POST - /posts/{id}/edit  - edit an existing post

    //showPost - GET - /posts/{id} - show a specific post

    //showPostByCategory - GET - /posts/category/{id}

    //searchForPosts - POST - /posts/search  form param is 'searchText'

    //showAllPosts - GET /post - get all posts in descending order by date posted
    @GetMapping("/posts")
    public String showAllPosts( Model model, Principal principal ) {
        log.debug( "show all posts" );
        String userName = principal.getName();
        log.debug( "user logged in: " + userName );

        return "redirect:/posts/show?cat=0&page=0";
    }

    //addNewPost - create a new post - POST - /posts
    @PostMapping("/posts")
    public String addNewPost( @ModelAttribute("postCommand") PostCommand postCommand, Principal principal ) {
        log.debug( "received new post: \n" + postCommand );
        PostCommand savedPost = postService.savePostCommand( postCommand );
        log.debug( "new post saved: \n" + savedPost );
        return "redirect:/posts";
    }


    //delete a post - GET - /posts/{id}/delete
    @GetMapping("/posts/{id}/delete")
    public String deletePost( @PathVariable("id") Long postId ) {
        log.debug( "deleting post: " + postId );
        //get details of the post to be deleted
        PostCommand postCommand = postService.getPost( postId );
        postService.deletePost( postCommand );
        return "redirect:/posts";
    }

    //showPostForPage - GET - /posts/show?cat=1&page=2
    @GetMapping("/posts/show")
    public String showPostsForPage( @RequestParam("cat") Long categoryId, @RequestParam("page") Integer page,
                                    Principal principal, Model model ) {
        log.debug( "showing posts for category: " + categoryId + " page:" + page );

        PageCommand pageCommand = postService.getAllPostsByCategoryForPage( categoryId, page );
        UserCommand userCommand = userService.getUserByUserName( principal.getName() );

        model.addAttribute( "page",pageCommand );
        model.addAttribute( "user",userCommand );
        model.addAttribute( "postCommand", new PostCommand() );
        return "userPosts";

    }
}
