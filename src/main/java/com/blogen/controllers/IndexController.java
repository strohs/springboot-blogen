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
 * @author Cliff
 * 1/5/18
 */
@Slf4j
@Controller
public class IndexController {

    @Autowired
    PostService postService;

    @Autowired
    UserRepository userRepository;

   @GetMapping({"/{page}"})
    public String returnPage( @PathVariable("page") String page ) {
       log.debug( "returning page:" + page );
       return page;
    }


}
