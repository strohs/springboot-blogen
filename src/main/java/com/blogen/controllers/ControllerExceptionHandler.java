package com.blogen.controllers;

import com.blogen.exceptions.NotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

/**
 * Global exception handler for all Controllers of this application
 *
 * @author Cliff
 */
@Slf4j
@ControllerAdvice
public class ControllerExceptionHandler {

    @ResponseStatus( HttpStatus.BAD_REQUEST )
    @ExceptionHandler( NotFoundException.class )
    public ModelAndView handleNotFoundException( Exception exception ) {
        log.error("NotFoundException " + exception.getMessage() );

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName( "404error" );
        modelAndView.addObject( "exception",exception );
        return modelAndView;
    }
}
