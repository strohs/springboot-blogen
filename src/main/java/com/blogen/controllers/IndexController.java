package com.blogen.controllers;

import lombok.extern.slf4j.Slf4j;
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

   @GetMapping({"/{page}"})
    public String returnPage( @PathVariable("page") String page ) {
       log.debug( "returning page:" + page );
       return page;
    }
}
