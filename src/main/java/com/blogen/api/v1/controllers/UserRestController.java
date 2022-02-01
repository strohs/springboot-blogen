package com.blogen.api.v1.controllers;

import com.blogen.api.v1.model.UserDTO;
import com.blogen.api.v1.model.UserListDTO;
import com.blogen.api.v1.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Users", description = "operations on Blogen Users")
@Slf4j
@RestController
public class UserRestController {

    public static final String BASE_URL = "/api/v1/users";

    private UserService userService;

    @Autowired
    public UserRestController(@Qualifier("userRestService") UserService userService) {
        this.userService = userService;
    }

    @Operation(summary = "get a list of all users")
    @GetMapping(value = BASE_URL, produces = "application/json")
    @ResponseStatus(HttpStatus.OK)
    public UserListDTO getAllUsers() {
        log.debug("getAllUsers");
        return userService.getAllUsers();
    }

    @Operation(summary = "get a specific user by id")
    @GetMapping(value = BASE_URL + "/{id}", produces = "application/json")
    @ResponseStatus(HttpStatus.OK)
    public UserDTO getUser(@PathVariable("id") Long id) {
        log.debug("getUser id=" + id);
        return userService.getUser(id);
    }

    @Operation(summary = "create a new user")
    @PostMapping(value = BASE_URL, consumes = "application/json", produces = "application/json")
    @ResponseStatus(HttpStatus.CREATED)
    public UserDTO createNewUser(@RequestBody UserDTO userDTO) {
        log.debug("createNewUser userDTO=" + userDTO);
        return userService.createNewUser(userDTO);
    }

    @Operation(summary = "update field(s) of an existing user")
    @PatchMapping(value = BASE_URL + "/{id}", consumes = "application/json", produces = "application/json")
    @ResponseStatus(HttpStatus.OK)
    public UserDTO updateUser(@PathVariable("id") Long id, @RequestBody UserDTO userDTO) {
        log.debug("update user id=" + id + " userDTO=" + userDTO);
        return userService.updateUser(id, userDTO);
    }
}
