package com.blogen.controllers;

import com.blogen.domain.Post;
import com.blogen.domain.User;
import com.blogen.repositories.PostRepository;
import com.blogen.repositories.UserRepository;
import com.blogen.services.PostService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Controller for the index page
 *
 * @author Cliff
 *
 */
@Slf4j
@Controller
public class IndexController {

    @Autowired
    PostService postService;

    @Autowired
    UserRepository userRepository;

   @GetMapping({"","/"})
    public String showIndex() {
       log.debug( "showing index page");
       return "index";
    }


}
