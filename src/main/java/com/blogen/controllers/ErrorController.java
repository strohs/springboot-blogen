package com.blogen.controllers;

import lombok.extern.log4j.Log4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.security.Principal;

/**
 * Controller for handling errors thrown by Spring Security
 *
 * @author Cliff
 */
@Log4j
@Controller
@RequestMapping("/error")
public class ErrorController {

    //this error will typically be thrown by Spring Security
    @RequestMapping("/403")
    @ResponseStatus( HttpStatus.FORBIDDEN )
    public String handle403( Principal principal ) {
        log.debug( "user: " + principal.getName() + " forbidden" );
        return "403error";
    }
}
