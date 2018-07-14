package com.blogen.controllers;

import com.blogen.commands.PostCommand;
import com.blogen.services.PostService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

/**
 * Controller for the index page
 *
 * @author Cliff
 *
 */
@Slf4j
@Controller
public class IndexController {

    private PostService postService;

    @Autowired
    public IndexController( PostService postService ) {
        this.postService = postService;
    }

    @GetMapping({"","/"})
    public String showIndex(Model model) {
       log.debug( "showing index page");
       List<PostCommand> recentPosts = postService.getTenRecentPosts();
       model.addAttribute( "posts",recentPosts );

       return "index";
    }




}
