package com.blogen.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Controller for displaying Login page and for managing login functionality
 *
 * @author Cliff
 */
@Slf4j
@Controller
public class LoginController {

    @GetMapping("/login")
    public String showLogin() {
        log.debug( "showing login page" );
        return "login";
    }

    //todo configure security as this may be handled by spring security
    @PostMapping("/doLogin")
    public String login( @RequestParam String username, @RequestParam String password ) {
        log.debug( "LOGIN: " + username + " PW: " + password );

        //redirect to show-posts
        return "posts";
    }
}
