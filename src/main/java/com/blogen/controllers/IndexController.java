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

    //TODO delete this after testing
    @GetMapping("/test/db")
    public String testDB() {
        log.debug( "in testDB" );
        User john = userRepository.findOne( 2L );
        Post parent = postService.getPost( 1L );
        Post child = postService.getPost( 3L );
        log.debug( parent.toString() );
        log.debug( child.toString() );
        postService.deletePost( child );


        return "index";
    }
}
