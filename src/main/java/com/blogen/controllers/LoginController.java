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


}
