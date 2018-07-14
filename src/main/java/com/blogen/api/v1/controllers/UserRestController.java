package com.blogen.api.v1.controllers;

import com.blogen.api.v1.model.UserDTO;
import com.blogen.api.v1.model.UserListDTO;
import com.blogen.api.v1.services.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

/**
 * Controller for REST operations in Blogen {@link com.blogen.domain.User}
 *
 * @author Cliff
 */
@Api
@Slf4j
@RestController
public class UserRestController {

    public static final String BASE_URL = "/api/v1/users";

    private UserService userService;

    @Autowired
    public UserRestController( @Qualifier("userRestService") UserService userService ) {
        this.userService = userService;
    }

    @ApiOperation( value = "get a list of all users", produces = "application/json")
    @GetMapping( BASE_URL )
    @ResponseStatus(HttpStatus.OK)
    public UserListDTO getAllUsers() {
        log.debug( "getAllUsers" );
        return userService.getAllUsers();
    }

    @ApiOperation( value = "get a specific user by id", produces = "application/json")
    @GetMapping( BASE_URL + "/{id}" )
    @ResponseStatus( HttpStatus.OK )
    public UserDTO getUser( @PathVariable("id") Long id ) {
        log.debug( "getUser id=" + id );
        return userService.getUser( id );
    }

    @ApiOperation( value = "create a new user", consumes = "application/json", produces = "application/json")
    @PostMapping( BASE_URL )
    @ResponseStatus( HttpStatus.CREATED )
    public UserDTO createNewUser( @RequestBody UserDTO userDTO ) {
        log.debug( "createNewUser userDTO=" + userDTO );
        return userService.createNewUser( userDTO );
    }

    @ApiOperation( value = "update field(s) of an existing user", consumes = "application/json", produces = "application/json")
    @PatchMapping( BASE_URL + "/{id}" )
    @ResponseStatus( HttpStatus.OK )
    public UserDTO updateUser( @PathVariable("id") Long id, @RequestBody UserDTO userDTO ) {
        log.debug( "update user id=" + id + " userDTO=" + userDTO );
        return userService.updateUser( id, userDTO );
    }
}
