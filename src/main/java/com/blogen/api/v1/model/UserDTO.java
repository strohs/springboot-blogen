package com.blogen.api.v1.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data Transfer Object for {@link com.blogen.domain.User}
 *
 * @author Cliff
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {

    private String firstName;

    private String lastName;

    private String userName;

    private String email;

    private String password;

    private String userUrl;

}
