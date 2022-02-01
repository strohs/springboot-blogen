package com.blogen.api.v1.model;

import io.swagger.v3.oas.annotations.media.Schema;
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

    @Schema(description = "first name of the user", example = "Mary")
    private String firstName;

    @Schema(description = "last name of the user", example = "Jones")
    private String lastName;

    @Schema(description = "user name (handle) of the user", example = "zeroCool2000")
    private String userName;

    @Schema(description = "user's email address", example = "zeroc@example.com")
    private String email;

    @Schema(description = "users password, between 8 and 30 characters", example = "superSecretPassword")
    private String password;

    @Schema(description = "url to the user within Blogen", example = "/api/v1/users/23", accessMode = Schema.AccessMode.READ_ONLY)
    private String userUrl;

}
