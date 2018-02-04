package com.blogen.api.v1.model;

import io.swagger.annotations.ApiModelProperty;
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

    @ApiModelProperty(value = "first name of the user", example = "Mary")
    private String firstName;

    @ApiModelProperty(value = "last name of the user", example = "Jones")
    private String lastName;

    @ApiModelProperty(value = "user name of the user", example = "superCool2049")
    private String userName;

    @ApiModelProperty(value = "users email address", example = "crazy@gmail.com")
    private String email;

    @ApiModelProperty(value = "users password, between 8 and 30 characters", example="superSecretPassword")
    private String password;

    @ApiModelProperty(value = "url that identifies this user", readOnly = true, example = "/api/v1/users/23")
    private String userUrl;

}
