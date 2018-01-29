package com.blogen.controllers;

import com.blogen.exceptions.NotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.DefaultHandlerExceptionResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Global exception handler for all Controllers of this application
 *
 * @author Cliff
 */
@Slf4j
@ControllerAdvice("com.blogen.controllers")
public class ControllerExceptionHandler {
//todo possibly add global ExceptionHandlers for other Spring MVC Exceptions

    @ResponseStatus( HttpStatus.NOT_FOUND )
    @ExceptionHandler( NotFoundException.class )
    public ModelAndView handleNotFoundException( Exception exception ) {
        log.error("NotFoundException " + exception.getMessage() );

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName( "404error" );
        modelAndView.addObject( "exception",exception );
        return modelAndView;
    }

    @ResponseStatus( HttpStatus.BAD_REQUEST )
    @ExceptionHandler( {NumberFormatException.class,IllegalArgumentException.class, MissingServletRequestParameterException.class} )
    public ModelAndView handleMalformedRequest( Exception exception ){

        log.error("Handling malformed request exception");
        log.error(exception.getMessage());

        ModelAndView modelAndView = new ModelAndView();

        modelAndView.setViewName("400error");
        modelAndView.addObject("exception", exception);


        return modelAndView;
    }
    
}
