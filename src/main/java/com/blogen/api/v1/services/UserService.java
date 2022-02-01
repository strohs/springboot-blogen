package com.blogen.api.v1.services;

import com.blogen.api.v1.model.UserDTO;
import com.blogen.api.v1.model.UserListDTO;

/**
 * REST operations on {@link com.blogen.domain.User}
 *
 * @author Cliff
 */
public interface UserService {

    UserListDTO getAllUsers();

    UserDTO getUser( Long id );

    UserDTO createNewUser( UserDTO userDTO );

    UserDTO updateUser( Long id, UserDTO userDTO );

    //delete user not supported in this version

}
