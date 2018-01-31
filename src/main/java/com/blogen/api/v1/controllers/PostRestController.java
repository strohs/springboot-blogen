package com.blogen.api.v1.controllers;

import com.blogen.api.v1.model.PostDTO;
import com.blogen.api.v1.model.PostListDTO;
import com.blogen.api.v1.services.PostService;
import com.blogen.api.v1.validators.PostDtoValidator;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.websocket.server.PathParam;

/**
 * REST Controller for working with {@link com.blogen.domain.Post}
 *
 * @author Cliff
 */
@Log4j
@RestController
public class PostRestController {

    public static final String BASE_URL = "/api/v1/posts";

    private PostService postService;
    private PostDtoValidator postDtoValidator;

    @Autowired
    public PostRestController( @Qualifier("postRestService") PostService postService, PostDtoValidator postDtoValidator ) {
        this.postService = postService;
        this.postDtoValidator = postDtoValidator;
    }

    @InitBinder("postDTO")
    public void setupBinder( WebDataBinder binder ) {
        binder.addValidators( postDtoValidator );
    }

    @GetMapping( BASE_URL + "{limit}")
    @ResponseStatus(HttpStatus.OK)
    public PostListDTO getPosts( @RequestParam(value = "limit", defaultValue = "5") int limit ) {
        log.debug( "getPosts limit=" + limit );
        return postService.getPosts( limit );
    }

    @GetMapping( BASE_URL + "/{id}" )
    @ResponseStatus( HttpStatus.OK )
    public PostDTO getPost( @PathVariable("id") Long id) {
        log.debug( "gePost id=" + id );
        return postService.getPost( id );
    }

    @PostMapping( BASE_URL )
    @ResponseStatus( HttpStatus.CREATED )
    public PostDTO createPost( @Valid @RequestBody PostDTO postDTO ) {
        log.debug( "createPost: " + postDTO );
        return postService.createNewPost( postDTO );
    }

    @PostMapping( BASE_URL + "/{id}")
    @ResponseStatus( HttpStatus.CREATED )
    public PostDTO createChildPost( @PathVariable("id") Long parentId, @Valid @RequestBody PostDTO postDTO ) {
        log.debug( "createChildPost id=" + parentId + "\n" + postDTO );
        return postService.createNewChildPost( parentId, postDTO );
    }

    @PutMapping( BASE_URL + "/{id}" )
    @ResponseStatus( HttpStatus.OK )
    public PostDTO updatePost( @PathVariable("id") Long id, @Valid @RequestBody PostDTO postDTO ) {
        log.debug( "updatePost id=" + id + " postDTO:\n" + postDTO );
        return postService.saveUpdatePost( id, postDTO );
    }

    @PatchMapping( BASE_URL + "/{id}" )
    @ResponseStatus( HttpStatus.OK )
    public PostDTO patchPost( @PathVariable( "id" ) Long id, @RequestBody PostDTO postDTO ) {
        log.debug( "patchPost id=" + id + "\n" + postDTO);
        return postService.saveUpdatePost( id, postDTO );
    }

    @DeleteMapping( BASE_URL + "/{id}")
    @ResponseStatus( HttpStatus.OK )
    public void deletePost( @PathVariable("id") Long id ) {
        log.debug( "deletePost id=" + id );
        postService.deletePost( id );
    }
}
