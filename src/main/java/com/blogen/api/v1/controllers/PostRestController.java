package com.blogen.api.v1.controllers;

import com.blogen.api.v1.model.PostListDTO;
import com.blogen.api.v1.services.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST Controller for working with {@link com.blogen.domain.Post}
 *
 * @author Cliff
 */
@RestController
public class PostRestController {

    public static final String BASE_URL = "/api/v1/posts";

    private PostService postService;

    @Autowired
    public PostRestController( @Qualifier("postRestService") PostService postService ) {
        this.postService = postService;
    }

    @GetMapping( BASE_URL + "{limit}")
    @ResponseStatus(HttpStatus.OK)
    public PostListDTO getPosts( @RequestParam(value = "limit", defaultValue = "0") int limit ) {
        return postService.getPosts( limit );
    }
}
